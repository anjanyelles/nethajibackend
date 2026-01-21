import CourseGridMain from "@/components/layout/main/CourseGridMain";
import SyllabusMain from "@/components/layout/main/SyllabusMain";
import ThemeController from "@/components/shared/others/ThemeController";
import PageWrapper from "@/components/shared/wrappers/PageWrapper";

export const metadata = {
  title: "Course Grid | Edurock - Education LMS Template",
  description: "Course Grid | Edurock - Education LMS Template",
};

const Syllabus = async () => {
  return (
    <PageWrapper>
      <main>
        <SyllabusMain />
        {/* <ThemeController /> */}
      </main>
    </PageWrapper>
  );
};

export default Syllabus;
