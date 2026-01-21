import Placements from "@/app/placements/page";
import About11 from "@/components/sections/abouts/About11";
import Brands from "@/components/sections/brands/Brands";
import FeatureCourses from "@/components/sections/featured-courses/FeatureCourses";
import HeroPrimary from "@/components/sections/hero-banners/HeroPrimary";
import Overview from "@/components/sections/overviews/Overview";
import Placementsdata from "@/components/sections/placementsdata/Placementsdata";
import Testimonials from "@/components/sections/testimonials/Testimonials";

const Placementmain = () => {
  return (
    <>
      <HeroPrimary title="Training & Placements" path={"Placements"} />
      <Placementsdata />
      {/* <Overview /> */}
      {/* <Testimonials /> */}
      <Brands />
    </>
  );
};

export default Placementmain;
