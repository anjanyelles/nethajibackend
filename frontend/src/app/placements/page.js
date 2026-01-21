import AboutMain from "@/components/layout/main/AboutMain";
import Placementmain from "@/components/layout/main/Placementmain";

import ThemeController from "@/components/shared/others/ThemeController";
import PageWrapper from "@/components/shared/wrappers/PageWrapper";

export const metadata = {
  title: "About | Edurock - Education LMS Template",
  description: "About | Edurock - Education LMS Template",
};

const Placements = async () => {
  return (
    <PageWrapper>
      <main>
        <Placementmain />
        {/* <ThemeController /> */}
      </main>
    </PageWrapper>
  );
};

export default Placements;
