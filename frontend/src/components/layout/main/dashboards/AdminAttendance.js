"use client";

import { useEffect, useState } from "react";
import { FaPlus, FaCalendar, FaBook } from "react-icons/fa";
import {
  markBulkAttendance,
  getAttendanceViewByCourseAndDate,
  getCourseAttendanceReportByDateRange,
} from "@/services";
import { getAllCourses } from "@/services/academicService";

export default function AdminAttendance() {
  const [courses, setCourses] = useState([]);
  const [students, setStudents] = useState([]);
  const [selectedCourse, setSelectedCourse] = useState("");
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);
  const [attendanceData, setAttendanceData] = useState({});
  const [statusFilter, setStatusFilter] = useState("");
  const [searchQuery, setSearchQuery] = useState("");
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [attendanceRecords, setAttendanceRecords] = useState([]);
  const [showReport, setShowReport] = useState(false);

  useEffect(() => {
    fetchCourses();
  }, []);

  useEffect(() => {
    if (selectedCourse) {
      fetchAttendanceView();
    }
  }, [selectedCourse, selectedDate, statusFilter, searchQuery]);

  const fetchCourses = async () => {
    try {
      const data = await getAllCourses();
      setCourses(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error("Error fetching courses:", err);
    }
  };

  const fetchAttendanceView = async () => {
    if (!selectedCourse || !selectedDate) return;

    setLoading(true);
    setError("");
    try {
      const rows = await getAttendanceViewByCourseAndDate(
        selectedCourse,
        selectedDate,
        statusFilter || undefined,
        searchQuery || undefined
      );
      const arr = Array.isArray(rows) ? rows : [];
      setStudents(arr);

      const prefill = {};
      arr.forEach((r) => {
        if (r?.studentId) {
          prefill[r.studentId] = r.status || "NOT_MARKED";
        }
      });
      setAttendanceData(prefill);
    } catch (err) {
      console.error("Error fetching attendance view:", err);
      setStudents([]);
    } finally {
      setLoading(false);
    }
  };

  const handleAttendanceChange = (studentId, status) => {
    setAttendanceData({
      ...attendanceData,
      [studentId]: status,
    });
  };

  const handleSubmitAttendance = async (e) => {
    e.preventDefault();
    
    if (!selectedCourse) {
      setError("Please select a course");
      return;
    }

    if (!selectedDate) {
      setError("Please select a date");
      return;
    }

    if (students.length === 0) {
      setError("No students found for this course");
      return;
    }

    setSubmitting(true);
    setError("");
    setSuccess("");

    try {
      const userId = typeof window !== "undefined" 
        ? sessionStorage.getItem("userId") 
        : null;

      if (!userId) {
        setError("User not logged in");
        return;
      }

      const studentsList = students
        .map((row) => {
          const studentId = row.studentId || row.id || row.student?.id;
          const status = attendanceData[studentId] ?? row.status ?? "NOT_MARKED";
          return {
            studentId,
            status,
            remarks: "",
          };
        })
        .filter((s) => s.studentId && s.status && s.status !== "NOT_MARKED");

      if (studentsList.length === 0) {
        setError("Please mark attendance for at least one student");
        return;
      }

      await markBulkAttendance({
        courseId: selectedCourse,
        attendanceDate: selectedDate,
        markedBy: userId,
        students: studentsList,
      });

      setSuccess("Attendance marked successfully!");
      fetchAttendanceView();
      setTimeout(() => setSuccess(""), 3000);
    } catch (err) {
      setError(err.response?.data?.message || err.message || "Failed to mark attendance");
      console.error("Mark attendance error:", err);
    } finally {
      setSubmitting(false);
    }
  };

  const handleViewReport = async () => {
    if (!selectedCourse) {
      setError("Please select a course");
      return;
    }

    try {
      const defaultEnd = selectedDate;
      const defaultStart = selectedDate;

      const startDate = window.prompt("Enter report start date (YYYY-MM-DD)", defaultStart);
      if (!startDate) return;

      const endDate = window.prompt("Enter report end date (YYYY-MM-DD)", defaultEnd);
      if (!endDate) return;

      const report = await getCourseAttendanceReportByDateRange(selectedCourse, startDate, endDate, 75);
      setAttendanceRecords(Array.isArray(report) ? report : []);
      setShowReport(true);
    } catch (err) {
      setError("Failed to load attendance report");
      console.error(err);
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case "PRESENT":
        return "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-300";
      case "ABSENT":
        return "bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-300";
      case "LATE":
        return "bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-300";
      case "EXCUSED":
        return "bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-300";
      default:
        return "bg-gray-100 text-gray-800 dark:bg-gray-700 dark:text-gray-300";
    }
  };

  return (
    <div className="p-6 bg-white dark:bg-gray-900 min-h-screen">
      <div className="max-w-7xl mx-auto">
        {/* Header */}
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-3xl font-bold text-gray-900 dark:text-white">
            Attendance Management
          </h1>
          {selectedCourse && (
            <button
              onClick={handleViewReport}
              className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg flex items-center gap-2"
            >
              <FaCalendar /> View Report
            </button>
          )}
        </div>

        {/* Messages */}
        {error && (
          <div className="mb-4 p-4 bg-red-100 dark:bg-red-900 text-red-700 dark:text-red-300 rounded-lg">
            {error}
          </div>
        )}
        {success && (
          <div className="mb-4 p-4 bg-green-100 dark:bg-green-900 text-green-700 dark:text-green-300 rounded-lg">
            {success}
          </div>
        )}

        {/* Filters */}
        <div className="bg-gray-50 dark:bg-gray-800 p-4 rounded-lg mb-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium mb-2">
                <FaBook className="inline mr-2" /> Select Course *
              </label>
              <select
                value={selectedCourse}
                onChange={(e) => setSelectedCourse(e.target.value)}
                className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                required
              >
                <option value="">Select a course</option>
                {courses.map((course) => (
                  <option key={course.id} value={course.id}>
                    {course.name} ({course.courseCode})
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium mb-2">
                <FaCalendar className="inline mr-2" /> Select Date *
              </label>
              <input
                type="date"
                value={selectedDate}
                onChange={(e) => setSelectedDate(e.target.value)}
                className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-2">Search Student</label>
              <input
                type="text"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                placeholder="Search by name or enrollment"
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-2">Status Filter</label>
              <select
                value={statusFilter}
                onChange={(e) => setStatusFilter(e.target.value)}
                className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
              >
                <option value="">All</option>
                <option value="PRESENT">Present</option>
                <option value="ABSENT">Absent</option>
                <option value="NOT_MARKED">Not Marked</option>
              </select>
            </div>
          </div>
        </div>

        {/* Attendance Form */}
        {selectedCourse && (
          <form onSubmit={handleSubmitAttendance}>
            <div className="bg-white dark:bg-gray-800 shadow-lg rounded-lg overflow-hidden">
              <div className="p-4 bg-gray-100 dark:bg-gray-700">
                <h2 className="text-xl font-semibold text-gray-900 dark:text-white">
                  Mark Attendance - {courses.find(c => c.id === selectedCourse)?.name || "Course"}
                </h2>
                <p className="text-sm text-gray-600 dark:text-gray-400">
                  Date: {selectedDate} | Students: {students.length}
                </p>
              </div>

              {loading ? (
                <div className="p-10 text-center text-gray-500">Loading students...</div>
              ) : students.length === 0 ? (
                <div className="p-10 text-center text-gray-500">No students found for this course</div>
              ) : (
                <div className="overflow-x-auto">
                  <table className="w-full">
                    <thead className="bg-gray-200 dark:bg-gray-700">
                      <tr>
                        <th className="px-6 py-3 text-left">Student Name</th>
                        <th className="px-6 py-3 text-left">Enrollment</th>
                        <th className="px-6 py-3 text-center">Present</th>
                        <th className="px-6 py-3 text-center">Absent</th>
                        <th className="px-6 py-3 text-center">Late</th>
                        <th className="px-6 py-3 text-center">Excused</th>
                      </tr>
                    </thead>
                    <tbody>
                      {students.map((row, idx) => {
                        const studentId = row.studentId || row.id || row.student?.id;
                        const studentName =
                          row.studentName ||
                          (row.firstName
                            ? `${row.firstName} ${row.lastName || ""}`.trim()
                            : row.student
                            ? `${row.student.firstName} ${row.student.lastName || ""}`.trim()
                            : "Unknown");
                        const enrollment = row.enrollmentNumber || row.student?.enrollmentNumber || "N/A";
                        const currentStatus = attendanceData[studentId] ?? row.status ?? "NOT_MARKED";

                        return (
                          <tr
                            key={studentId || idx}
                            className="border-b dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-800"
                          >
                            <td className="px-6 py-4 font-medium">{studentName}</td>
                            <td className="px-6 py-4">
                              {enrollment}
                              {currentStatus === "NOT_MARKED" ? (
                                <div className="text-xs text-gray-500">Not Marked</div>
                              ) : null}
                            </td>
                            <td className="px-6 py-4 text-center">
                              <input
                                type="radio"
                                name={`attendance_${studentId}`}
                                value="PRESENT"
                                checked={currentStatus === "PRESENT"}
                                onChange={() => handleAttendanceChange(studentId, "PRESENT")}
                                className="w-4 h-4"
                              />
                            </td>
                            <td className="px-6 py-4 text-center">
                              <input
                                type="radio"
                                name={`attendance_${studentId}`}
                                value="ABSENT"
                                checked={currentStatus === "ABSENT"}
                                onChange={() => handleAttendanceChange(studentId, "ABSENT")}
                                className="w-4 h-4"
                              />
                            </td>
                            <td className="px-6 py-4 text-center">
                              <input
                                type="radio"
                                name={`attendance_${studentId}`}
                                value="LATE"
                                checked={currentStatus === "LATE"}
                                onChange={() => handleAttendanceChange(studentId, "LATE")}
                                className="w-4 h-4"
                              />
                            </td>
                            <td className="px-6 py-4 text-center">
                              <input
                                type="radio"
                                name={`attendance_${studentId}`}
                                value="EXCUSED"
                                checked={currentStatus === "EXCUSED"}
                                onChange={() => handleAttendanceChange(studentId, "EXCUSED")}
                                className="w-4 h-4"
                              />
                            </td>
                          </tr>
                        );
                      })}
                    </tbody>
                  </table>
                </div>
              )}

              <div className="p-4 bg-gray-100 dark:bg-gray-700 flex justify-end">
                <button
                  type="submit"
                  disabled={submitting || students.length === 0}
                  className="px-6 py-2 bg-[#1f5a6c] hover:bg-[#174652] text-white rounded-lg disabled:opacity-50 flex items-center gap-2"
                >
                  <FaPlus /> {submitting ? "Saving..." : "Save Attendance"}
                </button>
              </div>
            </div>
          </form>
        )}

        {/* Attendance Report Modal */}
        {showReport && attendanceRecords.length > 0 && (
          <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50 mt-10">
            <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-xl w-full max-w-4xl max-h-[80vh] overflow-y-auto">
              <div className="flex justify-between items-center mb-4">
                <h2 className="text-2xl font-bold text-gray-900 dark:text-white">
                  Attendance Report
                </h2>
                <button
                  onClick={() => setShowReport(false)}
                  className="text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200"
                >
                  ✕
                </button>
              </div>
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead className="bg-gray-200 dark:bg-gray-700">
                    <tr>
                      <th className="px-4 py-2">Student</th>
                      <th className="px-4 py-2">Enrollment</th>
                      <th className="px-4 py-2">Total</th>
                      <th className="px-4 py-2">Present</th>
                      <th className="px-4 py-2">Absent</th>
                      <th className="px-4 py-2">Not Marked</th>
                      <th className="px-4 py-2">%</th>
                      <th className="px-4 py-2">Low</th>
                    </tr>
                  </thead>
                  <tbody>
                    {attendanceRecords.map((record, idx) => (
                      <tr key={idx} className="border-b dark:border-gray-700">
                        <td className="px-4 py-2">{record.studentName || "N/A"}</td>
                        <td className="px-4 py-2">{record.enrollmentNumber || "N/A"}</td>
                        <td className="px-4 py-2">{record.totalClasses ?? 0}</td>
                        <td className="px-4 py-2">{record.presentCount ?? 0}</td>
                        <td className="px-4 py-2">{record.absentCount ?? 0}</td>
                        <td className="px-4 py-2">{record.notMarkedCount ?? 0}</td>
                        <td className="px-4 py-2">{typeof record.attendancePercentage === "number" ? record.attendancePercentage.toFixed(2) : "0.00"}</td>
                        <td className="px-4 py-2">{record.lowAttendance ? "Yes" : "No"}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

