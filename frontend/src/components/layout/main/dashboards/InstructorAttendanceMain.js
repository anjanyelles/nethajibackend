"use client";

import { useEffect, useMemo, useState } from "react";
import { FaCalendar, FaCheck, FaTimes } from "react-icons/fa";
import {
  getLecturerCourses,
  getLecturerCourseRoster,
  markLecturerBulkAttendance,
} from "@/services/staffService";

const InstructorAttendanceMain = () => {
  const [courses, setCourses] = useState([]);
  const [selectedCourse, setSelectedCourse] = useState("");
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split("T")[0]);
  const [roster, setRoster] = useState([]);
  const [attendanceData, setAttendanceData] = useState({});
  const [loadingCourses, setLoadingCourses] = useState(false);
  const [loadingRoster, setLoadingRoster] = useState(false);
  const [submitting, setSubmitting] = useState(false);
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
        setAttendanceData({});
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
          if (s?.studentId) init[s.studentId] = "ABSENT";
        });
        setAttendanceData(init);
      } catch (e) {
        setRoster([]);
        setAttendanceData({});
        setError(e?.message || "Failed to load student roster");
      } finally {
        setLoadingRoster(false);
      }
    };

    loadRoster();
  }, [lecturerId, selectedCourse]);

  const setAll = (status) => {
    const next = { ...attendanceData };
    roster.forEach((s) => {
      if (s?.studentId) next[s.studentId] = status;
    });
    setAttendanceData(next);
  };

  const handleAttendanceChange = (studentId, status) => {
    setAttendanceData((prev) => ({
      ...prev,
      [studentId]: status,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!lecturerId) {
      setError("Please login again.");
      return;
    }

    if (!selectedCourse) {
      setError("Please select a course");
      return;
    }

    if (!selectedDate) {
      setError("Please select a date");
      return;
    }

    if (!roster.length) {
      setError("No students found for this course");
      return;
    }

    setSubmitting(true);
    setError("");
    setSuccess("");

    try {
      const students = roster.map((s) => ({
        studentId: s.studentId,
        status: attendanceData[s.studentId] || "ABSENT",
        remarks: "",
      }));

      await markLecturerBulkAttendance(lecturerId, selectedCourse, {
        courseId: selectedCourse,
        attendanceDate: selectedDate,
        markedBy: lecturerId,
        students,
      });

      setSuccess("Attendance marked successfully!");
      setTimeout(() => setSuccess(""), 2500);
    } catch (e) {
      setError(e?.message || "Failed to mark attendance");
    } finally {
      setSubmitting(false);
    }
  };

  const getCourseLabel = (c) => {
    const name = c?.courseName || c?.name || "";
    const code = c?.courseCode || "";
    return code ? `${name} (${code})` : name;
  };

  return (
    <div className="p-6 bg-whiteColor dark:bg-whiteColor-dark shadow-accordion dark:shadow-accordion-dark rounded-5">
      <div className="mb-6 pb-5 border-b-2 border-borderColor dark:border-borderColor-dark flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        <h2 className="text-2xl font-bold text-blackColor dark:text-blackColor-dark">Attendance</h2>

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
            <div className="text-sm text-blackColor dark:text-blackColor-dark">Date</div>
            <div className="relative">
              <input
                type="date"
                value={selectedDate}
                onChange={(e) => setSelectedDate(e.target.value)}
                className="border border-borderColor dark:border-borderColor-dark rounded px-3 py-2 bg-transparent text-sm"
              />
              <FaCalendar className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400 pointer-events-none" />
            </div>
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
        <form onSubmit={handleSubmit}>
          <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-3 mb-4">
            <div className="text-sm text-blackColor dark:text-blackColor-dark">
              Students: <span className="font-semibold">{roster.length}</span>
            </div>

            <div className="flex items-center gap-2">
              <button
                type="button"
                onClick={() => setAll("PRESENT")}
                className="px-3 py-2 rounded border border-borderColor dark:border-borderColor-dark text-sm"
              >
                <FaCheck className="inline mr-2" /> Mark all present
              </button>
              <button
                type="button"
                onClick={() => setAll("ABSENT")}
                className="px-3 py-2 rounded border border-borderColor dark:border-borderColor-dark text-sm"
              >
                <FaTimes className="inline mr-2" /> Mark all absent
              </button>
            </div>
          </div>

          <div className="overflow-x-auto">
            <table className="w-full text-left border border-borderColor dark:border-borderColor-dark rounded">
              <thead className="bg-gray-50 dark:bg-whiteColor-dark">
                <tr className="border-b border-borderColor dark:border-borderColor-dark">
                  <th className="py-3 px-4 text-sm font-semibold">Student</th>
                  <th className="py-3 px-4 text-sm font-semibold">Enrollment</th>
                  <th className="py-3 px-4 text-sm font-semibold">Section</th>
                  <th className="py-3 px-4 text-sm font-semibold">Present</th>
                  <th className="py-3 px-4 text-sm font-semibold">Absent</th>
                </tr>
              </thead>
              <tbody>
                {roster.map((s, idx) => {
                  const studentId = s.studentId;
                  const studentName = `${s.firstName || ""} ${s.lastName || ""}`.trim() || "-";
                  const enrollment = s.enrollmentNumber || "-";
                  const section = s.sectionName || "-";
                  const status = attendanceData[studentId] || "ABSENT";

                  return (
                    <tr
                      key={studentId || idx}
                      className={`border-b border-borderColor dark:border-borderColor-dark ${
                        idx % 2 === 0 ? "bg-whiteColor dark:bg-whiteColor-dark" : "bg-gray-50/50 dark:bg-whiteColor-dark"
                      }`}
                    >
                      <td className="py-3 px-4">{studentName}</td>
                      <td className="py-3 px-4">{enrollment}</td>
                      <td className="py-3 px-4">{section}</td>
                      <td className="py-3 px-4">
                        <input
                          type="radio"
                          name={`att_${studentId}`}
                          checked={status === "PRESENT"}
                          onChange={() => handleAttendanceChange(studentId, "PRESENT")}
                        />
                      </td>
                      <td className="py-3 px-4">
                        <input
                          type="radio"
                          name={`att_${studentId}`}
                          checked={status === "ABSENT"}
                          onChange={() => handleAttendanceChange(studentId, "ABSENT")}
                        />
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>

          <div className="mt-4 flex justify-end">
            <button
              type="submit"
              disabled={submitting}
              className="px-6 py-2 bg-[#1f5a6c] hover:bg-[#174652] text-white rounded-lg disabled:opacity-50"
            >
              {submitting ? "Saving..." : "Save Attendance"}
            </button>
          </div>
        </form>
      )}
    </div>
  );
};

export default InstructorAttendanceMain;
