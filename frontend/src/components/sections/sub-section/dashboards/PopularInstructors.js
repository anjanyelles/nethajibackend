"use client";

import { useEffect, useState } from "react";
import teacherImage1 from "@/assets/images/teacher/teacher__1.png";
import HeadingDashboard from "@/components/shared/headings/HeadingDashboard";
import Image from "next/image";
import Link from "next/link";
import { getAllLecturers } from "@/services";

const PopularInstructors = () => {
  const [instructors, setInstructors] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchInstructors = async () => {
      try {
        setLoading(true);
        const data = await getAllLecturers();
        // Get first 5 lecturers
        const popularInstructors = Array.isArray(data) ? data.slice(0, 5) : [];
        setInstructors(popularInstructors);
      } catch (error) {
        console.error("Error fetching instructors:", error);
        setInstructors([]);
      } finally {
        setLoading(false);
      }
    };

    fetchInstructors();
  }, []);

  return (
    <div className="p-10px md:px-10 md:py-50px mb-30px bg-whiteColor dark:bg-whiteColor-dark shadow-accordion dark:shadow-accordion-dark rounded-5 max-h-137.5 overflow-auto">
      <HeadingDashboard path={`/instructors`}>
        Popular Instructors
      </HeadingDashboard>

      {loading ? (
        <div className="text-center text-gray-500 py-10">Loading instructors...</div>
      ) : instructors.length === 0 ? (
        <div className="text-center text-gray-500 py-10">No instructors available</div>
      ) : (
        <ul>
          {instructors.map((instructor, idx) => (
            <li
              key={instructor.id || idx}
              className={`flex items-center flex-wrap ${
                idx === instructors.length - 1
                  ? "pt-15px"
                  : "py-15px border-b border-borderColor dark:border-borderColor-dark"
              }`}
            >
              {/* avatar */}
              <div className="max-w-full md:max-w-1/5 pr-10px">
                <Image 
                  src={teacherImage1} 
                  alt={instructor.firstName || "Instructor"} 
                  className="w-full rounded-full"
                  width={60}
                  height={60}
                />
              </div>
              {/* details */}
              <div className="max-w-full md:max-w-4/5 pr-10px">
                <div>
                  <h5 className="text-lg leading-1 font-bold text-contentColor dark:text-contentColor-dark mb-5px">
                    <Link
                      className="hover:text-primaryColor"
                      href={`/instructors/${instructor.id}`}
                    >
                      {instructor.firstName} {instructor.lastName}
                    </Link>
                  </h5>
                  <div className="flex flex-wrap items-center text-sm text-darkblack dark:text-darkblack-dark gap-x-15px gap-y-10px leading-1.8">
                    <p>
                      <i className="icofont-email"></i> {instructor.email || "N/A"}
                    </p>
                    <p>
                      <i className="icofont-phone"></i> {instructor.mobileNumber || "N/A"}
                    </p>
                    <p>
                      <i className="icofont-badge"></i> {instructor.enrollmentNumber || "N/A"}
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

export default PopularInstructors;
