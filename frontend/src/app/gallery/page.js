import ContactMain from "@/components/layout/main/ContactMain";
import Gallerymain from "@/components/layout/main/Gallery";
import ImageGallery from "@/components/sections/sub-section/ImageGallery";

import ThemeController from "@/components/shared/others/ThemeController";
import PageWrapper from "@/components/shared/wrappers/PageWrapper";

export const metadata = {
  title: "Contact | Edurock - Education LMS Template",
  description: "Contact | Edurock - Education LMS Template",
};

const Gallery = async () => {
  return (
    <PageWrapper>
      <main>
        <Gallerymain />
        
        <ImageGallery />
      </main>
    </PageWrapper>
  );
};

export default Gallery;
