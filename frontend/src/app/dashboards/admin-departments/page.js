import AdminProfileMain from "@/components/layout/main/dashboards/AdminProfileMain";
import AdminDepartment from "@/components/sections/sub-section/dashboards/AdminDepartment";
import DashboardContainer from "@/components/shared/containers/DashboardContainer";

import ThemeController from "@/components/shared/others/ThemeController";
import DsahboardWrapper from "@/components/shared/wrappers/DsahboardWrapper";
import PageWrapper from "@/components/shared/wrappers/PageWrapper";
export const metadata = {
  title: "Admin Profile | Edurock - Education LMS Template",
  description: "Admin Profile | Edurock - Education LMS Template",
};
const Admin_Departments = () => {
  return (
    <PageWrapper>
      <main>
        <DsahboardWrapper>
          <DashboardContainer>
            <AdminDepartment />
          </DashboardContainer>
        </DsahboardWrapper>
        <ThemeController />
      </main>
    </PageWrapper>
  );
};

export default Admin_Departments;
