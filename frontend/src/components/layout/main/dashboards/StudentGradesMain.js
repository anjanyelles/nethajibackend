"use client";

import React, { useEffect, useMemo, useState } from "react";
import { getGradesByStudent } from "@/services/gradesService";

const StudentGradesMain = () => {
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [courseFilter, setCourseFilter] = useState("ALL");

  const normalize = (v) => (v ? String(v).trim() : "");

  const gradeBadgeClass = (gradeRaw) => {
    const g = String(gradeRaw || "").toUpperCase();
    if (g === "A+" || g === "A") return "bg-green-100 text-green-700";
    if (g === "B+" || g === "B") return "bg-blue-100 text-blue-700";
    if (g === "C" || g === "D") return "bg-yellow-100 text-yellow-700";
    if (g === "F") return "bg-red-100 text-red-700";
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

        const data = await getGradesByStudent(userId);
        setRows(Array.isArray(data) ? data : []);
      } catch (e) {
        setRows([]);
        setError(e?.message || "Failed to load grades");
      } finally {
        setLoading(false);
      }
    };

    load();
  }, []);

  return (
    <div className="p-10px md:px-10 md:py-50px mb-30px bg-whiteColor dark:bg-whiteColor-dark shadow-accordion dark:shadow-accordion-dark rounded-5">
      <StudentGradesContent
        courseFilter={courseFilter}
        error={error}
        gradeBadgeClass={gradeBadgeClass}
        loading={loading}
        normalize={normalize}
        rows={rows}
        setCourseFilter={setCourseFilter}
      />
    </div>
  );
};

const StudentGradesContent = ({
  courseFilter,
  error,
  gradeBadgeClass,
  loading,
  normalize,
  rows,
  setCourseFilter,
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
    const subjects = list.length;
    const avgPct = subjects
      ? Math.round(
          (list.reduce((sum, r) => sum + Number(r?.percentage ?? 0), 0) / subjects) * 100
        ) / 100
      : 0;
    const avgTotal = subjects
      ? Math.round(
          (list.reduce((sum, r) => sum + Number(r?.totalMarks ?? 0), 0) / subjects) * 100
        ) / 100
      : 0;
    return { subjects, avgPct, avgTotal };
  }, [filteredRows]);

  return (
    <>
      <div className="mb-6 pb-5 border-b-2 border-borderColor dark:border-borderColor-dark flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        <h2 className="text-2xl font-bold text-blackColor dark:text-blackColor-dark">Grades</h2>

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
        <div>No grades found.</div>
      ) : (
        <>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 mb-6">
            <div className="p-4 rounded border border-borderColor dark:border-borderColor-dark">
              <div className="text-sm text-blackColor dark:text-blackColor-dark opacity-70">Subjects</div>
              <div className="text-2xl font-bold text-blackColor dark:text-blackColor-dark">{stats.subjects}</div>
            </div>
            <div className="p-4 rounded border border-borderColor dark:border-borderColor-dark">
              <div className="text-sm text-blackColor dark:text-blackColor-dark opacity-70">Average %</div>
              <div className="text-2xl font-bold text-blackColor dark:text-blackColor-dark">{stats.avgPct}%</div>
            </div>
            <div className="p-4 rounded border border-borderColor dark:border-borderColor-dark">
              <div className="text-sm text-blackColor dark:text-blackColor-dark opacity-70">Average Total</div>
              <div className="text-2xl font-bold text-blackColor dark:text-blackColor-dark">{stats.avgTotal}</div>
            </div>
          </div>

          <div className="overflow-x-auto">
            <table className="w-full text-left border border-borderColor dark:border-borderColor-dark rounded">
              <thead className="bg-gray-50 dark:bg-whiteColor-dark">
                <tr className="border-b border-borderColor dark:border-borderColor-dark">
                  <th className="py-3 px-4 text-sm font-semibold">Course</th>
                  <th className="py-3 px-4 text-sm font-semibold">Internal</th>
                  <th className="py-3 px-4 text-sm font-semibold">Mid</th>
                  <th className="py-3 px-4 text-sm font-semibold">Final</th>
                  <th className="py-3 px-4 text-sm font-semibold">Total</th>
                  <th className="py-3 px-4 text-sm font-semibold">Grade</th>
                  <th className="py-3 px-4 text-sm font-semibold">%</th>
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
                    <td className="py-3 px-4">{r.courseName || "-"}</td>
                    <td className="py-3 px-4 whitespace-nowrap">{r.internalMarks ?? "-"}</td>
                    <td className="py-3 px-4 whitespace-nowrap">{r.midtermMarks ?? "-"}</td>
                    <td className="py-3 px-4 whitespace-nowrap">{r.finalMarks ?? "-"}</td>
                    <td className="py-3 px-4 whitespace-nowrap">{r.totalMarks ?? "-"}</td>
                    <td className="py-3 px-4">
                      <span className={`inline-flex px-2 py-1 rounded-full text-xs font-semibold ${gradeBadgeClass(r.grade)}`}>
                        {r.grade || "-"}
                      </span>
                    </td>
                    <td className="py-3 px-4 whitespace-nowrap">{r.percentage ?? "-"}</td>
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

export default StudentGradesMain;
