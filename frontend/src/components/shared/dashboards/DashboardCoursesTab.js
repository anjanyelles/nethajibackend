"use client";

import { useEffect, useState } from "react";
import { getProgramHierarchy } from "@/services";

export default function DashboardCoursesTab() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    async function load() {
      try {
        setLoading(true);
        const json = await getProgramHierarchy();

        if (Array.isArray(json)) {
          setData(json);
        } else {
          console.error("Invalid API response:", json);
          setError("Invalid API response from server");
        }
      } catch (err) {
        console.error("API Error:", err);
        setError("Failed to load academic data");
      } finally {
        setLoading(false);
      }
    }
    load();
  }, []);

  if (loading) return <div className="p-6">Loading courses...</div>;
  if (error) return <div className="p-6 text-red-600">{error}</div>;

  return (
    <div className="p-6 bg-white shadow rounded">
      <h1 className="text-3xl font-bold mb-8">Academic Programs & Courses</h1>

      {data.map((program) => (
        <div key={program.id} className="mb-12 border-b pb-10">

          {/* ✅ PROGRAM */}
          <h2 className="text-2xl font-bold text-blue-700 mb-6">
            {program.name} ({program.programCode})
          </h2>

          {/* ✅ DEPARTMENTS */}
          {(program.departments || []).map((dept) => (
            <div
              key={dept.id}
              className="mb-10 pl-4 border-l-4 border-[#1f5a6c] hover:border-[#174652] transition"
            >
              {/* ✅ DEPARTMENT */}
              <h3 className="text-xl font-semibold text-green-700 mb-4">
                Department: {dept.departmentName} ({dept.departmentCode})
              </h3>

              {/* ✅ SEMESTERS — EACH ROW */}
              <div className="space-y-6">

                {(dept.departMentSemestersDtoList || []).map((sem) => (
                  <div
                    key={sem.id}
                    className="border rounded p-4 bg-gray-50"
                  >
                    {/* ✅ SEMESTER HEADER */}
                    <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between mb-2">
                      <h4 className="text-lg font-bold text-[#1f5a6c]/90">
                        Semester {sem.semester}
                      </h4>

                      {/* ✅ TOTALS DISPLAY */}
                      <div className="text-sm font-medium text-gray-700 mt-1 sm:mt-0">
                        Total Theory:{" "}
                        <span className="text-blue-700">
                          {sem.semesterSubjects ?? 0}
                        </span>
                        {"  |  "}
                        Total Labs:{" "}
                        <span className="text-red-600">
                          {sem.semesterTotalLabs ?? 0}
                        </span>
                      </div>
                    </div>

                    {/* ✅ OPTIONAL DATES & YEAR */}
                    {(sem.semesterStartDate || sem.semesterEndDate || sem.semesterYear) && (
                      <div className="text-xs text-gray-500 mb-3">
                        {sem.semesterYear && <>Year: {sem.semesterYear} | </>}
                        {sem.semesterStartDate && <>Start: {formatDate(sem.semesterStartDate)} | </>}
                        {sem.semesterEndDate && <>End: {formatDate(sem.semesterEndDate)}</>}
                      </div>
                    )}

                    {/* ✅ COURSE LIST */}
                    <ul className="list-disc ml-6 space-y-1 text-sm">

                      {(sem.courseListInfo || []).map((course) => (
                        <li key={course.id}>
                          <span className="font-medium">{course.name}</span>
                          {" "}
                          <span className="text-gray-500 text-xs">
                            ({course.courseCode})
                          </span>
                          {course.courseType && (
                            <span
                              className={`ml-2 px-2 py-[1px] rounded text-xs font-semibold ${
                                course.courseType === "LAB"
                                  ? "bg-red-100 text-red-700"
                                  : "bg-blue-100 text-blue-700"
                              }`}
                            >
                              {course.courseType}
                            </span>
                          )}
                        </li>
                      ))}

                    </ul>
                  </div>
                ))}

              </div>
            </div>
          ))}
        </div>
      ))}
    </div>
  );
}

/* ✅ Helper to format date safely */
function formatDate(dateStr) {
  try {
    const d = new Date(dateStr);
    return d.toLocaleDateString();
  } catch {
    return dateStr;
  }
}



