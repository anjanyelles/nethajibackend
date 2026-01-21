"use client";
import awardImage1 from "@/assets/images/about/award-1.jpg";
import awardImage2 from "@/assets/images/about/award-2.jpg";
import awardImage3 from "@/assets/images/about/award-3.jpg";
import awardImage4 from "@/assets/images/about/award-4.jpg";

import AwardSingle from "./AwardSingle";
import useIsTrue from "@/hooks/useIsTrue";

const AwardContent = () => {
  const isAbout = useIsTrue("/about");
  const isAboutDark = useIsTrue("/about-dark");

  const allAwards = [
    {
      id: 1,
      image: awardImage1,
      title: "Best College for Emerging Technologies – 2023",
    },
    {
      id: 2,
      image: awardImage2,
      title: "Award for Excellence in Student Development – 2022",
    },
    {
      id: 3,
      image: awardImage3,
      title: "Top Ranked Institution in Data Science Programs",
    },
    {
      id: 4,
      image: awardImage4,
      title: "Innovative Teaching Practices Recognition – 2021",
    },
    {
      id: 5,
      image: awardImage1,
      title: "Best Infrastructure for Digital Learning – 2023",
    },
    {
      id: 6,
      image: awardImage2,
      title: "Excellence in Curriculum Innovation – 2022",
    },
  ];

  const displayedAwards =
    isAbout || isAboutDark ? allAwards.slice(0, 4) : allAwards;

  return (
    <div className="grid grid-cols-1 lg:grid-cols-2 gap-30px items-center">
      {displayedAwards.map((award) => (
        <AwardSingle key={award.id} award={award} />
      ))}
    </div>
  );
};

export default AwardContent;
