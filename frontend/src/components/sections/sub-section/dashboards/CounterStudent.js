"use client";

import { useEffect, useState } from "react";
import counter1 from "@/assets/images/counter/counter__1.png";
import counter2 from "@/assets/images/counter/counter__2.png";
import counter3 from "@/assets/images/counter/counter__3.png";
import CounterDashboard from "@/components/shared/dashboards/CounterDashboard";
import HeadingDashboard from "@/components/shared/headings/HeadingDashboard";
import { getStudentAssignments, getAttendanceByStudent, getGradesByStudent } from "@/services";

const CounterStudent = () => {
  const [counts, setCounts] = useState([
    {
      name: "My Assignments",
      image: counter1,
      data: 0,
      symbol: "",
    },
    {
      name: "Attendance Records",
      image: counter2,
      data: 0,
      symbol: "",
    },
    {
      name: "Courses Enrolled",
      image: counter3,
      data: 0,
      symbol: "",
    },
  ]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStudentStats = async () => {
      try {
        setLoading(true);
        const studentId = typeof window !== "undefined" ? sessionStorage.getItem("userId") : null;
        
        if (!studentId) {
          setLoading(false);
          return;
        }

        const [assignments, attendance, grades] = await Promise.all([
          getStudentAssignments(studentId).catch(() => []),
          getAttendanceByStudent(studentId).catch(() => []),
          getGradesByStudent(studentId).catch(() => []),
        ]);

        // Get unique courses from attendance
        const uniqueCourses = new Set();
        if (Array.isArray(attendance)) {
          attendance.forEach((att) => {
            if (att.courseId) uniqueCourses.add(att.courseId);
          });
        }

        setCounts([
          {
            name: "My Assignments",
            image: counter1,
            data: Array.isArray(assignments) ? assignments.length : 0,
            symbol: "",
          },
          {
            name: "Attendance Records",
            image: counter2,
            data: Array.isArray(attendance) ? attendance.length : 0,
            symbol: "",
          },
          {
            name: "Courses Enrolled",
            image: counter3,
            data: uniqueCourses.size || (Array.isArray(grades) ? grades.length : 0),
            symbol: "",
          },
        ]);
      } catch (error) {
        console.error("Error fetching student stats:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchStudentStats();
  }, []);

  if (loading) {
    return (
      <div className="p-6">
        <HeadingDashboard>Summary</HeadingDashboard>
        <div className="text-center text-gray-500">Loading statistics...</div>
      </div>
    );
  }

  return (
    <CounterDashboard counts={counts}>
      <HeadingDashboard>Summary</HeadingDashboard>
    </CounterDashboard>
  );
};

export default CounterStudent;
