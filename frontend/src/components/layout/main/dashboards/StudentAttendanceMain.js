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

  const stats = useMemo(() => {
    const list = filteredRows;
    const total = list.length;
    const present = list.filter((r) => String(r?.status || "").toUpperCase() === "PRESENT").length;
    const absent = list.filter((r) => String(r?.status || "").toUpperCase() === "ABSENT").length;
    const pct = total > 0 ? Math.round((present / total) * 100) : 0;
    return { total, present, absent, pct };
  }, [filteredRows]);

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

          <div className="overflow-x-auto">
            <table className="w-full text-left border border-borderColor dark:border-borderColor-dark rounded">
              <thead className="bg-gray-50 dark:bg-whiteColor-dark">
                <tr className="border-b border-borderColor dark:border-borderColor-dark">
                  <th className="py-3 px-4 text-sm font-semibold">Date</th>
                  <th className="py-3 px-4 text-sm font-semibold">Course</th>
                  <th className="py-3 px-4 text-sm font-semibold">Status</th>
                  <th className="py-3 px-4 text-sm font-semibold">Remarks</th>
                </tr>
              </thead>
              <tbody>
                {filteredRows.map((r, idx) => (
                  <tr
                    key={r.id || idx}
                    className={`border-b border-borderColor dark:border-borderColor-dark ${
                      idx % 2 === 0 ? "bg-whiteColor dark:bg-whiteColor-dark" : "bg-gray-50/50 dark:bg-whiteColor-dark"
                    }`}
                  >
                    <td className="py-3 px-4 whitespace-nowrap">{formatDate(r.attendanceDate)}</td>
                    <td className="py-3 px-4">{r.courseName || "-"}</td>
                    <td className="py-3 px-4">
                      <span className={`inline-flex px-2 py-1 rounded-full text-xs font-semibold ${statusBadgeClass(r.status)}`}>
                        {r.status || "-"}
                      </span>
                    </td>
                    <td className="py-3 px-4">{r.remarks || "-"}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </>
      )}
    </>
  );
};

export default StudentAttendanceMain;
