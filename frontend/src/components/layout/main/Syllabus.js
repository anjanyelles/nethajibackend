import CoursesPrimary from "@/components/sections/courses/CoursesPrimary";
import HeroPrimary from "@/components/sections/hero-banners/HeroPrimary";

const Syllabus = () => {
  return (
    <>
      <HeroPrimary path={"Syllabus & Curriculum"} title={"Syllabus & Curriculum"} />
      <CoursesPrimary isNotSidebar={true} />
    </>
  );
};

export default Syllabus;
