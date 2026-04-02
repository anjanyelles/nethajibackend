import EnrollmentTabs from "@/components/shared/enrollment/EnrollmentTabs";
import PageWrapper from "@/components/shared/wrappers/PageWrapper";
import ThemeController from "@/components/shared/others/ThemeController";

export const metadata = {
  title: "Inter Student Enrollment | Nethaji College",
  description: "Inter Student Enrollment Form | Nethaji College Management System",
};

const EnrollmentPage = () => {
  return (
    <PageWrapper>
      <main className="min-h-screen bg-gray-50 dark:bg-gray-900 py-12">
        <div className="container mx-auto px-4">
          <EnrollmentTabs />
        </div>
        <ThemeController />
      </main>
    </PageWrapper>
  );
};

export default EnrollmentPage;
