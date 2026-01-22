"use client";

import { useEffect, useMemo, useState } from "react";
import { FaCalendar, FaCalculator, FaSave } from "react-icons/fa";
import {
  calculateLecturerGradesForCourse,
  enterLecturerBulkMarks,
  getLecturerCourseRoster,
  getLecturerCourses,
} from "@/services/staffService";

const EXAM_TYPES = [
  "INTERNAL",
  "MIDTERM",
  "FINAL",
  "ASSIGNMENT",
  "QUIZ",
  "COLLAGEEXAM",
  "SEMESTEREXAMES",
];

const InstructorMarksGradesMain = () => {
  const [courses, setCourses] = useState([]);
  const [selectedCourse, setSelectedCourse] = useState("");
  const [examType, setExamType] = useState("INTERNAL");
  const [examDate, setExamDate] = useState(new Date().toISOString().split("T")[0]);
  const [maxMarks, setMaxMarks] = useState("100");

  const [roster, setRoster] = useState([]);
  const [marksByStudent, setMarksByStudent] = useState({});

  const [grades, setGrades] = useState([]);

  const [loadingCourses, setLoadingCourses] = useState(false);
  const [loadingRoster, setLoadingRoster] = useState(false);
  const [submittingMarks, setSubmittingMarks] = useState(false);
  const [calculatingGrades, setCalculatingGrades] = useState(false);

  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const lecturerId = useMemo(() => {
    if (typeof window === "undefined") return null;
    return sessionStorage.getItem("userId");
  }, []);

  useEffect(() => {
    const loadCourses = async () => {
      if (!lecturerId) return;
      setLoadingCourses(true);
      try {
        setError("");
        const data = await getLecturerCourses(lecturerId);
        setCourses(Array.isArray(data) ? data : []);
      } catch (e) {
        setCourses([]);
        setError(e?.message || "Failed to load courses");
      } finally {
        setLoadingCourses(false);
      }
    };

    loadCourses();
  }, [lecturerId]);

  useEffect(() => {
    const loadRoster = async () => {
      if (!lecturerId || !selectedCourse) {
        setRoster([]);
        setMarksByStudent({});
        setGrades([]);
        return;
      }

      setLoadingRoster(true);
      try {
        setError("");
        const data = await getLecturerCourseRoster(lecturerId, selectedCourse);
        const list = Array.isArray(data) ? data : [];
        setRoster(list);

        const init = {};
        list.forEach((s) => {
          if (s?.studentId) init[s.studentId] = "";
        });
        setMarksByStudent(init);
        setGrades([]);
      } catch (e) {
        setRoster([]);
        setMarksByStudent({});
        setGrades([]);
        setError(e?.message || "Failed to load student roster");
      } finally {
        setLoadingRoster(false);
      }
    };

    loadRoster();
  }, [lecturerId, selectedCourse]);

  const getCourseLabel = (c) => {
    const name = c?.courseName || c?.name || "";
    const code = c?.courseCode || "";
    return code ? `${name} (${code})` : name;
  };

  useEffect(() => {
    const max = Number(maxMarks);
    if (!Number.isFinite(max) || max <= 0) return;

    setMarksByStudent((prev) => {
      const next = { ...prev };
      Object.keys(next).forEach((studentId) => {
        const raw = next[studentId];
        if (raw === "" || raw === null || raw === undefined) return;
        const num = Number(raw);
        if (!Number.isFinite(num)) return;
        if (num > max) next[studentId] = String(max);
        if (num < 0) next[studentId] = "0";
      });
      return next;
    });
  }, [maxMarks]);

  const handleMarkChange = (studentId, value) => {
    if (value === "") {
      setMarksByStudent((prev) => ({ ...prev, [studentId]: "" }));
      return;
    }

    if (!/^\d*\.?\d*$/.test(value)) {
      return;
    }

    const max = Number(maxMarks);
    if (Number.isFinite(max) && max > 0) {
      const num = Number(value);
      if (Number.isFinite(num)) {
        if (num > max) {
          setMarksByStudent((prev) => ({ ...prev, [studentId]: String(max) }));
          return;
        }
        if (num < 0) {
          setMarksByStudent((prev) => ({ ...prev, [studentId]: "0" }));
          return;
        }
      }
    }

    setMarksByStudent((prev) => ({ ...prev, [studentId]: value }));
  };

  const handleSubmitMarks = async (e) => {
    e.preventDefault();

    if (!lecturerId) {
      setError("Please login again.");
      return;
    }

    if (!selectedCourse) {
      setError("Please select a course");
      return;
    }

    const max = Number(maxMarks);
    if (!Number.isFinite(max) || max <= 0) {
      setError("Please enter valid max marks");
      return;
    }

    const students = roster
      .filter((s) => s?.studentId)
      .map((s) => {
        const raw = marksByStudent[s.studentId];
        const val = raw === "" || raw === null || raw === undefined ? null : Number(raw);
        return {
          studentId: s.studentId,
          marksObtained: val,
          remarks: "",
        };
      })
      .filter((x) => x.marksObtained !== null && Number.isFinite(x.marksObtained));

    const invalid = students.find((s) => s.marksObtained < 0 || s.marksObtained > max);
    if (invalid) {
      setError(`Marks must be between 0 and ${max}`);
      return;
    }

    if (students.length === 0) {
      setError("Enter marks for at least one student");
      return;
    }

    setSubmittingMarks(true);
    setError("");
    setSuccess("");

    try {
      await enterLecturerBulkMarks(lecturerId, selectedCourse, {
        courseId: selectedCourse,
        examType,
        maxMarks: max,
        examDate,
        evaluatedBy: lecturerId,
        students,
      });

      setSuccess("Marks saved successfully!");
      setTimeout(() => setSuccess(""), 2500);
    } catch (e) {
      setError(e?.message || "Failed to save marks");
    } finally {
      setSubmittingMarks(false);
    }
  };

  const handleCalculateGrades = async () => {
    if (!lecturerId) {
      setError("Please login again.");
      return;
    }

    if (!selectedCourse) {
      setError("Please select a course");
      return;
    }

    setCalculatingGrades(true);
    setError("");
    setSuccess("");

    try {
      const data = await calculateLecturerGradesForCourse(lecturerId, selectedCourse);
      setGrades(Array.isArray(data) ? data : []);
      setSuccess("Grades calculated successfully!");
      setTimeout(() => setSuccess(""), 2500);
    } catch (e) {
      setGrades([]);
      setError(e?.message || "Failed to calculate grades");
    } finally {
      setCalculatingGrades(false);
    }
  };

  return (
    <div className="p-6 bg-whiteColor dark:bg-whiteColor-dark shadow-accordion dark:shadow-accordion-dark rounded-5">
      <div className="mb-6 pb-5 border-b-2 border-borderColor dark:border-borderColor-dark flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        <h2 className="text-2xl font-bold text-blackColor dark:text-blackColor-dark">Marks & Grades</h2>

        <div className="flex flex-col md:flex-row gap-3 md:items-center">
          <div className="flex items-center gap-2">
            <div className="text-sm text-blackColor dark:text-blackColor-dark">Course</div>
            <select
              value={selectedCourse}
              onChange={(e) => setSelectedCourse(e.target.value)}
              className="border border-borderColor dark:border-borderColor-dark rounded px-3 py-2 bg-transparent text-sm"
              disabled={loadingCourses}
            >
              <option value="">Select course</option>
              {courses.map((c) => (
                <option key={c.courseId || c.id} value={c.courseId || c.id}>
                  {getCourseLabel(c)}
                </option>
              ))}
            </select>
          </div>

          <div className="flex items-center gap-2">
            <div className="text-sm text-blackColor dark:text-blackColor-dark">Exam</div>
            <select
              value={examType}
              onChange={(e) => setExamType(e.target.value)}
              className="border border-borderColor dark:border-borderColor-dark rounded px-3 py-2 bg-transparent text-sm"
            >
              {EXAM_TYPES.map((t) => (
                <option key={t} value={t}>
                  {t}
                </option>
              ))}
            </select>
          </div>

          <div className="flex items-center gap-2">
            <div className="text-sm text-blackColor dark:text-blackColor-dark">Date</div>
            <div className="relative">
              <input
                type="date"
                value={examDate}
                onChange={(e) => setExamDate(e.target.value)}
                className="border border-borderColor dark:border-borderColor-dark rounded px-3 py-2 bg-transparent text-sm"
              />
              <FaCalendar className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 pointer-events-none" />
            </div>
          </div>

          <div className="flex items-center gap-2">
            <div className="text-sm text-blackColor dark:text-blackColor-dark">Max</div>
            <input
              value={maxMarks}
              onChange={(e) => setMaxMarks(e.target.value)}
              className="border border-borderColor dark:border-borderColor-dark rounded px-3 py-2 bg-transparent text-sm w-[90px]"
              inputMode="decimal"
            />
          </div>
        </div>
      </div>

      {error ? <div className="text-red-600 mb-4">{error}</div> : null}
      {success ? <div className="text-green-600 mb-4">{success}</div> : null}

      {!lecturerId ? (
        <div>Please login again.</div>
      ) : !selectedCourse ? (
        <div>Select a course to load students.</div>
      ) : loadingRoster ? (
        <div>Loading students...</div>
      ) : roster.length === 0 ? (
        <div>No students found for this course.</div>
      ) : (
        <>
          <form onSubmit={handleSubmitMarks}>
            <div className="overflow-x-auto">
              <table className="w-full text-left border border-borderColor dark:border-borderColor-dark rounded">
                <thead className="bg-gray-50 dark:bg-whiteColor-dark">
                  <tr className="border-b border-borderColor dark:border-borderColor-dark">
                    <th className="py-3 px-4 text-sm font-semibold">Student</th>
                    <th className="py-3 px-4 text-sm font-semibold">Enrollment</th>
                    <th className="py-3 px-4 text-sm font-semibold">Marks</th>
                  </tr>
                </thead>
                <tbody>
                  {roster.map((s, idx) => {
                    const studentId = s.studentId;
                    const studentName = `${s.firstName || ""} ${s.lastName || ""}`.trim() || "-";
                    const enrollment = s.enrollmentNumber || "-";

                    return (
                      <tr
                        key={studentId || idx}
                        className={`border-b border-borderColor dark:border-borderColor-dark ${
                          idx % 2 === 0 ? "bg-whiteColor dark:bg-whiteColor-dark" : "bg-gray-50/50 dark:bg-whiteColor-dark"
                        }`}
                      >
                        <td className="py-3 px-4">{studentName}</td>
                        <td className="py-3 px-4">{enrollment}</td>
                        <td className="py-3 px-4">
                          <input
                            type="number"
                            value={marksByStudent[studentId] ?? ""}
                            onChange={(e) => handleMarkChange(studentId, e.target.value)}
                            className="border border-borderColor dark:border-borderColor-dark rounded px-3 py-2 bg-transparent text-sm w-[140px]"
                            inputMode="decimal"
                            placeholder="e.g. 78"
                            min={0}
                            max={Number.isFinite(Number(maxMarks)) ? Number(maxMarks) : undefined}
                          />
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>

            <div className="mt-4 flex justify-end gap-2">
              <button
                type="button"
                onClick={handleCalculateGrades}
                disabled={calculatingGrades}
                className="px-6 py-2 border border-borderColor dark:border-borderColor-dark rounded-lg disabled:opacity-50"
              >
                <FaCalculator className="inline mr-2" />
                {calculatingGrades ? "Calculating..." : "Generate Grades"}
              </button>

              <button
                type="submit"
                disabled={submittingMarks}
                className="px-6 py-2 bg-[#1f5a6c] hover:bg-[#174652] text-white rounded-lg disabled:opacity-50"
              >
                <FaSave className="inline mr-2" />
                {submittingMarks ? "Saving..." : "Save Marks"}
              </button>
            </div>
          </form>

          {grades.length ? (
            <div className="mt-8">
              <h3 className="text-lg font-semibold text-blackColor dark:text-blackColor-dark mb-3">Grades</h3>
              <div className="overflow-x-auto">
                <table className="w-full text-left border border-borderColor dark:border-borderColor-dark rounded">
                  <thead className="bg-gray-50 dark:bg-whiteColor-dark">
                    <tr className="border-b border-borderColor dark:border-borderColor-dark">
                      <th className="py-3 px-4 text-sm font-semibold">Student</th>
                      <th className="py-3 px-4 text-sm font-semibold">Total</th>
                      <th className="py-3 px-4 text-sm font-semibold">% </th>
                      <th className="py-3 px-4 text-sm font-semibold">Grade</th>
                      <th className="py-3 px-4 text-sm font-semibold">GP</th>
                    </tr>
                  </thead>
                  <tbody>
                    {grades.map((g, idx) => (
                      <tr
                        key={g.id || idx}
                        className={`border-b border-borderColor dark:border-borderColor-dark ${
                          idx % 2 === 0 ? "bg-whiteColor dark:bg-whiteColor-dark" : "bg-gray-50/50 dark:bg-whiteColor-dark"
                        }`}
                      >
                        <td className="py-3 px-4">{g.studentName || "-"}</td>
                        <td className="py-3 px-4">{g.totalMarks ?? "-"}</td>
                        <td className="py-3 px-4">{g.percentage ? Number(g.percentage).toFixed(2) : "-"}</td>
                        <td className="py-3 px-4">{g.grade || "-"}</td>
                        <td className="py-3 px-4">{g.gradePoint ?? "-"}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          ) : null}
        </>
      )}
    </div>
  );
};

export default InstructorMarksGradesMain;
