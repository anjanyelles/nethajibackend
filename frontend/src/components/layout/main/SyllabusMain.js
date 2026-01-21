import CoursesOffered from "@/components/sections/courses/CoursesOffered";
import CoursesPrimary from "@/components/sections/courses/CoursesPrimary";
import HeroPrimary from "@/components/sections/hero-banners/HeroPrimary";
import React from "react";

const SyllabusMain = () => {
  return (
    <>
      <HeroPrimary path={"Featured Courses"} title={"Featured Course"} />
      <CoursesOffered />
    </>
  );
};

export default SyllabusMain;
