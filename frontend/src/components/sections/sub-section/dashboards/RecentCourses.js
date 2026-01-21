"use client";

import { useEffect, useState } from "react";
import HeadingDashboard from "@/components/shared/headings/HeadingDashboard";
import Image from "next/image";
import Link from "next/link";
import { getAllCourses } from "@/services";
import coursePlaceholder from "@/assets/images/course/Course1.jpeg";

const RecentCourses = () => {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchCourses = async () => {
      try {
        setLoading(true);
        const data = await getAllCourses();
        // Get latest 4 courses
        const recentCourses = Array.isArray(data) ? data.slice(0, 4) : [];
        setCourses(recentCourses);
      } catch (error) {
        console.error("Error fetching courses:", error);
        setCourses([]);
      } finally {
        setLoading(false);
      }
    };

    fetchCourses();
  }, []);

  return (
    <div className="p-10px md:px-10 md:py-50px mb-30px bg-whiteColor dark:bg-whiteColor-dark shadow-accordion dark:shadow-accordion-dark rounded-5 max-h-137.5 overflow-auto">
      <HeadingDashboard path={"/courses"}>Recent Courses</HeadingDashboard>
      
      {loading ? (
        <div className="text-center text-gray-500 py-10">Loading courses...</div>
      ) : courses.length === 0 ? (
        <div className="text-center text-gray-500 py-10">No courses available</div>
      ) : (
        <ul>
          {courses.map((course, idx) => (
            <li
              key={course.id || idx}
              className={`flex items-center flex-wrap ${
                idx === courses.length - 1
                  ? "pt-5"
                  : "py-5 border-b border-borderColor dark:border-borderColor-dark"
              }`}
            >
              {/* avatar */}
              <div className="w-full md:w-30% md:pr-5">
                <Link className="w-full" href={`/courses/${course.id}`}>
                  <Image
                    src={coursePlaceholder}
                    alt={course.name || "Course"}
                    className="w-full rounded"
                    width={200}
                    height={120}
                  />
                </Link>
              </div>
              {/* details */}
              <div className="w-full md:w-70% md:pr-5">
                <div>
                  <h5 className="text-lg leading-1.5 font-medium text-contentColor dark:text-contentColor-dark mb-5px">
                    <Link
                      className="hover:text-primaryColor"
                      href={`/courses/${course.id}`}
                    >
                      {course.name || "Untitled Course"}
                    </Link>
                  </h5>
                  <div className="flex flex-wrap items-center justify-between text-sm text-darkblack dark:text-darkblack-dark gap-x-15px gap-y-10px leading-1.8">
                    <p>
                      <i className="icofont-book-alt"></i> {course.courseCode || "N/A"}
                    </p>
                    <p>
                      <i className="icofont-certificate"></i> {course.credits || 0} Credits
                    </p>
                    <p>
                      <i className="icofont-tag"></i> {course.courseType || "N/A"}
                    </p>
                  </div>
                </div>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default RecentCourses;
