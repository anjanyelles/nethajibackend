import React from "react";
import FooterNavItems from "./FooterNavItems";
import FooterAbout from "./FooterAbout";
import FooterRecentPosts from "./FooterRecentPosts";

const FooterNavList = () => {
  const lists = [
    {
      heading: "Quick Links",
      items: [
        {
          name: "Home",
          path: "/",
        },
        {
          name: "About Us",
          path: "/about",
        },
        {
          name: "Gallery",
          path: "/gallery",
        },
    
        {
          name: "Contact Us",
          path: "/contact",
        },
      ],
    },
    {
      heading: "Academic",
      items: [
        {
          name: "COURSES",
          path: "/course-grid",
        },
        {
          name: "SYLLABUS",
          path: "/syllabus",
        },
        {
          name: "Placements",
          path: "/placements",
        },
      ],
    },
  ];

  return (
    <section>
      <div className="grid grid-cols-1 sm:grid-cols-12 md:grid-cols-2 lg:grid-cols-12 gap-30px md:gap-y-5 lg:gap-y-0 pt-60px pb-50px md:pt-30px md:pb-30px lg:pt-110px lg:pb-20">
        {/* left */}
        <FooterAbout />
        {/* nav area */}
        {lists.map((list, idx) => (
          <FooterNavItems key={idx} list={list} idx={idx} />
        ))}
        {/* right */}
        <FooterRecentPosts />
      </div>
    </section>
  );
};

export default FooterNavList;