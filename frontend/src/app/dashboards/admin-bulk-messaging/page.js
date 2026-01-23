import AdminBulkMessagingMain from "@/components/layout/main/dashboards/AdminBulkMessagingMain";
import DashboardContainer from "@/components/shared/containers/DashboardContainer";
import ThemeController from "@/components/shared/others/ThemeController";
import DsahboardWrapper from "@/components/shared/wrappers/DsahboardWrapper";
import PageWrapper from "@/components/shared/wrappers/PageWrapper";

export const metadata = {
  title: "Admin Bulk Messaging | Nethaji Degree College",
  description: "Send bulk messages to students",
};

const Admin_Bulk_Messaging = () => {
  return (
    <PageWrapper>
      <main>
        <DsahboardWrapper>
          <DashboardContainer>
            <AdminBulkMessagingMain />
          </DashboardContainer>
        </DsahboardWrapper>
        <ThemeController />
      </main>
    </PageWrapper>
  );
};

export default Admin_Bulk_Messaging;
