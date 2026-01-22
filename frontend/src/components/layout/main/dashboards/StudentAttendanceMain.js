"use client";

import React, { useEffect, useMemo, useState } from "react";
import { getAttendanceByStudent } from "@/services/attendanceService";

const StudentAttendanceMain = () => {
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [courseFilter, setCourseFilter] = useState("ALL");

  const normalize = (v) => (v ? String(v).trim() : "");

  const formatDate = (value) => {
    if (!value) return "-";
    try {
      const d = new Date(value);
      if (Number.isNaN(d.getTime())) return String(value);
      return d.toLocaleDateString();
    } catch {
      return String(value);
    }
  };

  const statusBadgeClass = (statusRaw) => {
    const s = String(statusRaw || "").toUpperCase();
    if (s === "PRESENT") return "bg-green-100 text-green-700";
    if (s === "ABSENT") return "bg-red-100 text-red-700";
    if (s === "LATE") return "bg-yellow-100 text-yellow-700";
    if (s === "EXCUSED") return "bg-blue-100 text-blue-700";
    return "bg-gray-100 text-gray-700";
  };

  useEffect(() => {
    const load = async () => {
      try {
        setError("");
        const userId = typeof window !== "undefined" ? sessionStorage.getItem("userId") : null;
        if (!userId) {
          setRows([]);
          setError("Please login again.");
          return;
        }

        const data = await getAttendanceByStudent(userId);
        setRows(Array.isArray(data) ? data : []);
      } catch (e) {
        setRows([]);
        setError(e?.message || "Failed to load attendance");
      } finally {
        setLoading(false);
      }
    };

    load();
  }, []);

  return (
    <div className="p-10px md:px-10 md:py-50px mb-30px bg-whiteColor dark:bg-whiteColor-dark shadow-accordion dark:shadow-accordion-dark rounded-5">
      <StudentAttendanceContent
        courseFilter={courseFilter}
        error={error}
        formatDate={formatDate}
        loading={loading}
        normalize={normalize}
        rows={rows}
        setCourseFilter={setCourseFilter}
        statusBadgeClass={statusBadgeClass}
      />
    </div>
  );
};

const StudentAttendanceContent = ({
  courseFilter,
  error,
  formatDate,
  loading,
  normalize,
  rows,
  setCourseFilter,
  statusBadgeClass,
}) => {
  const toDateKey = (value) => {
    if (!value) return "";
    if (typeof value === "string" && /^\d{4}-\d{2}-\d{2}$/.test(value)) return value;
    const d = new Date(value);
    if (Number.isNaN(d.getTime())) return "";
    return d.toISOString().slice(0, 10);
  };

  const keyToDate = (key) => {
    if (!key) return null;
    const d = new Date(`${key}T00:00:00Z`);
    return Number.isNaN(d.getTime()) ? null : d;
  };

  const formatKey = (key) => {
    const d = keyToDate(key);
    if (!d) return "-";
    return d.toLocaleDateString(undefined, { weekday: "long", year: "numeric", month: "long", day: "numeric" });
  };

  const [monthAnchor, setMonthAnchor] = useState(() => new Date());
  const [monthTouched, setMonthTouched] = useState(false);
  const [selectedKey, setSelectedKey] = useState("");

  const courseOptions = useMemo(() => {
    const set = new Set();
    (Array.isArray(rows) ? rows : []).forEach((r) => {
      const name = normalize(r?.courseName);
      if (name) set.add(name);
    });
    return ["ALL", ...Array.from(set).sort((a, b) => a.localeCompare(b))];
  }, [rows, normalize]);

  const filteredRows = useMemo(() => {
    const list = Array.isArray(rows) ? rows : [];
    if (courseFilter === "ALL") return list;
    return list.filter((r) => normalize(r?.courseName) === courseFilter);
  }, [rows, courseFilter, normalize]);

  const byDay = useMemo(() => {
    const map = new Map();
    (Array.isArray(filteredRows) ? filteredRows : []).forEach((r) => {
      const key = toDateKey(r?.attendanceDate);
      if (!key) return;
      if (!map.has(key)) map.set(key, []);
      map.get(key).push(r);
    });
    for (const [k, list] of map.entries()) {
      list.sort((a, b) => String(a?.courseName || "").localeCompare(String(b?.courseName || "")));
      map.set(k, list);
    }
    return map;
  }, [filteredRows]);

  const dayStats = useMemo(() => {
    const out = new Map();
    for (const [key, list] of byDay.entries()) {
      const total = list.length;
      const present = list.filter((r) => String(r?.status || "").toUpperCase() === "PRESENT").length;
      const absent = list.filter((r) => String(r?.status || "").toUpperCase() === "ABSENT").length;
      const late = list.filter((r) => String(r?.status || "").toUpperCase() === "LATE").length;
      const excused = list.filter((r) => String(r?.status || "").toUpperCase() === "EXCUSED").length;
      out.set(key, { total, present, absent, late, excused });
    }
    return out;
  }, [byDay]);

  useEffect(() => {
    if (monthTouched) return;
    if (!filteredRows || filteredRows.length === 0) return;
    const keys = filteredRows
      .map((r) => toDateKey(r?.attendanceDate))
      .filter(Boolean)
      .sort();
    if (keys.length === 0) return;
    const latestKey = keys[keys.length - 1];
    const d = keyToDate(latestKey);
    if (!d) return;
    setMonthAnchor(new Date(d.getFullYear(), d.getMonth(), 1));
    setSelectedKey(latestKey);
  }, [filteredRows, monthTouched]);

  const stats = useMemo(() => {
    const list = filteredRows;
    const total = list.length;
    const present = list.filter((r) => String(r?.status || "").toUpperCase() === "PRESENT").length;
    const absent = list.filter((r) => String(r?.status || "").toUpperCase() === "ABSENT").length;
    const pct = total > 0 ? Math.round((present / total) * 100) : 0;
    return { total, present, absent, pct };
  }, [filteredRows]);

  const monthLabel = useMemo(() => {
    const d = monthAnchor instanceof Date ? monthAnchor : new Date();
    return d.toLocaleDateString(undefined, { month: "long", year: "numeric" });
  }, [monthAnchor]);

  const calendarDays = useMemo(() => {
    const anchor = monthAnchor instanceof Date ? monthAnchor : new Date();
    const year = anchor.getFullYear();
    const month = anchor.getMonth();

    const firstOfMonth = new Date(year, month, 1);
    const shift = (firstOfMonth.getDay() + 6) % 7;
    const gridStart = new Date(year, month, 1);
    gridStart.setDate(gridStart.getDate() - shift);

    const days = [];
    for (let i = 0; i < 42; i += 1) {
      const d = new Date(gridStart);
      d.setDate(gridStart.getDate() + i);
      const key = d.toISOString().slice(0, 10);
      const inMonth = d.getMonth() === month;
      days.push({ key, date: d, inMonth });
    }
    return { year, month, days };
  }, [monthAnchor]);

  const selectedList = useMemo(() => {
    if (!selectedKey) return [];
    return byDay.get(selectedKey) || [];
  }, [byDay, selectedKey]);

  const goMonth = (delta) => {
    const anchor = monthAnchor instanceof Date ? monthAnchor : new Date();
    const d = new Date(anchor.getFullYear(), anchor.getMonth() + delta, 1);
    setMonthTouched(true);
    setMonthAnchor(d);
  };

  return (
    <>
      <div className="mb-6 pb-5 border-b-2 border-borderColor dark:border-borderColor-dark flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        <h2 className="text-2xl font-bold text-blackColor dark:text-blackColor-dark">Attendance</h2>

        <div className="flex items-center gap-3">
          <div className="text-sm text-blackColor dark:text-blackColor-dark">Course</div>
          <select
            value={courseFilter}
            onChange={(e) => setCourseFilter(e.target.value)}
            className="border border-borderColor dark:border-borderColor-dark rounded px-3 py-2 bg-transparent text-sm"
          >
            {courseOptions.map((c) => (
              <option key={c} value={c}>
                {c === "ALL" ? "All" : c}
              </option>
            ))}
          </select>
        </div>
      </div>

      {error ? <div className="text-red-600 mb-4">{error}</div> : null}

      {loading ? (
        <div>Loading...</div>
      ) : filteredRows.length === 0 ? (
        <div>No attendance records found.</div>
      ) : (
        <>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
            <div className="p-4 rounded border border-borderColor dark:border-borderColor-dark">
              <div className="text-sm text-blackColor dark:text-blackColor-dark opacity-70">Total Classes</div>
              <div className="text-2xl font-bold text-blackColor dark:text-blackColor-dark">{stats.total}</div>
            </div>
            <div className="p-4 rounded border border-borderColor dark:border-borderColor-dark">
              <div className="text-sm text-blackColor dark:text-blackColor-dark opacity-70">Present</div>
              <div className="text-2xl font-bold text-blackColor dark:text-blackColor-dark">{stats.present}</div>
            </div>
            <div className="p-4 rounded border border-borderColor dark:border-borderColor-dark">
              <div className="text-sm text-blackColor dark:text-blackColor-dark opacity-70">Absent</div>
              <div className="text-2xl font-bold text-blackColor dark:text-blackColor-dark">{stats.absent}</div>
            </div>
            <div className="p-4 rounded border border-borderColor dark:border-borderColor-dark">
              <div className="text-sm text-blackColor dark:text-blackColor-dark opacity-70">Attendance %</div>
              <div className="text-2xl font-bold text-blackColor dark:text-blackColor-dark">{stats.pct}%</div>
            </div>
          </div>

          <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
            <div className="lg:col-span-7 border border-borderColor dark:border-borderColor-dark rounded p-4">
              <div className="flex items-center justify-between gap-3 mb-4">
                <button
                  type="button"
                  onClick={() => goMonth(-1)}
                  className="px-3 py-2 rounded border border-borderColor dark:border-borderColor-dark text-sm"
                >
                  Prev
                </button>
                <div className="text-lg font-bold text-blackColor dark:text-blackColor-dark">{monthLabel}</div>
                <button
                  type="button"
                  onClick={() => goMonth(1)}
                  className="px-3 py-2 rounded border border-borderColor dark:border-borderColor-dark text-sm"
                >
                  Next
                </button>
              </div>

              <div className="grid grid-cols-7 gap-2 text-xs font-semibold text-blackColor dark:text-blackColor-dark opacity-70 mb-2">
                {["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"].map((d) => (
                  <div key={d} className="px-2">
                    {d}
                  </div>
                ))}
              </div>

              <div className="grid grid-cols-7 gap-2">
                {calendarDays.days.map(({ key, date, inMonth }) => {
                  const s = dayStats.get(key);
                  const has = !!s;
                  const isSelected = selectedKey === key;
                  const dayNumber = date.getDate();
                  const borderTone = !has
                    ? "border-borderColor dark:border-borderColor-dark"
                    : s.absent > 0
                      ? "border-red-200"
                      : s.present > 0
                        ? "border-green-200"
                        : "border-borderColor dark:border-borderColor-dark";
                  const bgTone = isSelected ? "bg-gray-50 dark:bg-whiteColor-dark" : "bg-transparent";

                  return (
                    <button
                      key={key}
                      type="button"
                      onClick={() => setSelectedKey(key)}
                      className={`text-left rounded border ${borderTone} ${bgTone} p-2 min-h-[74px] hover:bg-gray-50/70 dark:hover:bg-whiteColor-dark/60 transition`}
                    >
                      <div className={`text-sm font-bold ${inMonth ? "text-blackColor dark:text-blackColor-dark" : "text-gray-400"}`}>{dayNumber}</div>

                      {has ? (
                        <div className="mt-2 space-y-1">
                          <div className="flex items-center justify-between text-[11px]">
                            <span className="text-green-700">P</span>
                            <span className="text-blackColor dark:text-blackColor-dark">{s.present}</span>
                          </div>
                          <div className="flex items-center justify-between text-[11px]">
                            <span className="text-red-700">A</span>
                            <span className="text-blackColor dark:text-blackColor-dark">{s.absent}</span>
                          </div>
                        </div>
                      ) : (
                        <div className="mt-2 text-[11px] text-gray-400">No class</div>
                      )}
                    </button>
                  );
                })}
              </div>
            </div>

            <div className="lg:col-span-5 border border-borderColor dark:border-borderColor-dark rounded p-4">
              <div className="mb-3">
                <div className="text-sm font-semibold text-blackColor dark:text-blackColor-dark opacity-70">Selected Day</div>
                <div className="text-lg font-bold text-blackColor dark:text-blackColor-dark">{selectedKey ? formatKey(selectedKey) : "Select a day"}</div>
              </div>

              {selectedKey && selectedList.length === 0 ? (
                <div className="text-sm text-gray-500">No classes on this day.</div>
              ) : selectedList.length > 0 ? (
                <div className="space-y-3">
                  {selectedList.map((r, idx) => (
                    <div key={r.id || `${selectedKey}-${idx}`} className="rounded border border-borderColor dark:border-borderColor-dark p-3">
                      <div className="flex items-start justify-between gap-3">
                        <div>
                          <div className="text-sm font-semibold text-blackColor dark:text-blackColor-dark">{r.courseName || "-"}</div>
                          <div className="text-xs text-gray-500">{formatDate(r.attendanceDate)}</div>
                        </div>
                        <span className={`inline-flex px-2 py-1 rounded-full text-xs font-semibold ${statusBadgeClass(r.status)}`}>
                          {r.status || "-"}
                        </span>
                      </div>
                      <div className="mt-2 text-sm text-blackColor dark:text-blackColor-dark">Remarks: {r.remarks || "-"}</div>
                    </div>
                  ))}
                </div>
              ) : (
                <div className="text-sm text-gray-500">Select a day to see class-wise attendance.</div>
              )}
            </div>
          </div>
        </>
      )}
    </>
  );
};

export default StudentAttendanceMain;
