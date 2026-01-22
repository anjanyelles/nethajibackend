"use client";
import DashboardCoursesTab from "@/components/shared/dashboards/DashboardCoursesTab";
const StudentEnrolledCourses = () => {
  return (
    <div className="p-10px md:px-10 md:py-50px mb-30px bg-whiteColor dark:bg-whiteColor-dark shadow-accordion dark:shadow-accordion-dark rounded-5">
      <DashboardCoursesTab />
    </div>
  );
};

export default StudentEnrolledCourses;
