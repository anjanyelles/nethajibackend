import AdminEnrollmentsContent from "@/components/shared/admin/AdminEnrollmentsContent";
import PageWrapper from "@/components/shared/wrappers/PageWrapper";
import ThemeController from "@/components/shared/others/ThemeController";

export const metadata = {
  title: "Admin Enrollments | Nethaji College",
  description: "Manage Student Enrollments | Nethaji College Management System",
};

const AdminEnrollmentsPage = () => {
  return (
    <PageWrapper>
      <main>
        <AdminEnrollmentsContent />
        <ThemeController />
      </main>
    </PageWrapper>
  );
};

export default AdminEnrollmentsPage;
