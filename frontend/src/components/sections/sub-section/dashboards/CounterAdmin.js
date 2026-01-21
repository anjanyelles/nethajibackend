"use client";

import { useEffect, useState } from "react";
import counter1 from "@/assets/images/counter/counter__1.png";
import counter2 from "@/assets/images/counter/counter__2.png";
import counter3 from "@/assets/images/counter/counter__3.png";
import counter4 from "@/assets/images/counter/counter__4.png";
import CounterDashboard from "@/components/shared/dashboards/CounterDashboard";
import HeadingDashboard from "@/components/shared/headings/HeadingDashboard";
import { getAllCourses, getAllStudents, getAllLecturers, getAllDepartments, getAllPrograms } from "@/services";

const CounterAdmin = () => {
  const [counts, setCounts] = useState([
    {
      name: "Total Courses",
      image: counter1,
      data: 0,
      symbol: "",
    },
    {
      name: "Total Students",
      image: counter2,
      data: 0,
      symbol: "",
    },
    {
      name: "Total Lecturers",
      image: counter3,
      data: 0,
      symbol: "",
    },
    {
      name: "Total Departments",
      image: counter4,
      data: 0,
      symbol: "",
    },
    {
      name: "Total Programs",
      image: counter3,
      data: 0,
      symbol: "",
    },
  ]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        setLoading(true);
        const [courses, students, lecturers, departments, programs] = await Promise.all([
          getAllCourses().catch(() => []),
          getAllStudents(0, 1).catch(() => ({ totalElements: 0 })),
          getAllLecturers().catch(() => []),
          getAllDepartments().catch(() => []),
          getAllPrograms().catch(() => []),
        ]);

        setCounts([
          {
            name: "Total Courses",
            image: counter1,
            data: Array.isArray(courses) ? courses.length : 0,
            symbol: "",
          },
          {
            name: "Total Students",
            image: counter2,
            data: students?.totalElements || students?.length || 0,
            symbol: "",
          },
          {
            name: "Total Lecturers",
            image: counter3,
            data: Array.isArray(lecturers) ? lecturers.length : 0,
            symbol: "",
          },
          {
            name: "Total Departments",
            image: counter4,
            data: Array.isArray(departments) ? departments.length : 0,
            symbol: "",
          },
          {
            name: "Total Programs",
            image: counter3,
            data: Array.isArray(programs) ? programs.length : 0,
            symbol: "",
          },
        ]);
      } catch (error) {
        console.error("Error fetching stats:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchStats();
  }, []);

  if (loading) {
    return (
      <div className="p-6">
        <HeadingDashboard>Dashboard</HeadingDashboard>
        <div className="text-center text-gray-500">Loading statistics...</div>
      </div>
    );
  }

  return (
    <CounterDashboard counts={counts}>
      <HeadingDashboard>Dashboard</HeadingDashboard>
    </CounterDashboard>
  );
};

export default CounterAdmin;
