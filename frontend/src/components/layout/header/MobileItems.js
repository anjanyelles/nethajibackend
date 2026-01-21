import React from "react";
import AccordionHome from "./AccordionHome";
import AccordionContainer from "@/components/shared/containers/AccordionContainer";
import MobileMenuItem from "./MobileItem";
import AccordionPages from "./AccordionPages";
import AccordionCourses from "./AccordionCourses";
import AccordionDashboard from "./AccordionDashboard";
import AccordionEcommerce from "./AccordionEcommerce";
import DropdownDashboard from "./DropdownDashboard";

const MobileMenuItems = () => {
  const items = [
    {
      id: 1,
      name: "HOME",
      path: "/",
      accordion: "accordion",
      // children: <AccordionHome />,
    },
    {
      id: 1,
      name: "ABOUT US",
      path: "/about",
      accordion: "accordion",
      // children: <AccordionHome />,
    },
    {
      id: 1,
      name: "GALLARY",
      path: "/gallery",
      accordion: "accordion",
      // children: <AccordionHome />,
    },
    {
      id: 1,
      name: "ACADEMIC",
      path: "/academic",
      accordion: "accordion",
      // children: <AccordionHome />,
    },
    // {
    //   id: 2,
    //   name: "Pages",
    //   path: "/about",
    //   accordion: "accordion",
    //   children: <AccordionPages />,
    // },
    // {
    //   id: 3,
    //   name: "Courses",
    //   path: "/courses",
    //   accordion: "accordion",
    //   children: <AccordionCourses />,
    // },
    // {
    //   id: 4,
    //   name: "Dashboard",
    //   path: "/dashboards/instructor-dashboard",
    //   accordion: "accordion",
    //   children: <AccordionDashboard />,
    // },
    // {
    //   id: 5,
    //   name: "Ecommerce",
    //   path: "/ecommerce/shop",
    //   accordion: "accordion",
    //   children: <AccordionEcommerce />,
    // },


  

    {
      id: 1,
      name: "COURSES",
      path: "/course-grid",
      accordion: "accordion",
      // children: <AccordionHome />,
    },
    {
      id: 1,
      name: "SYLLABUS",
      path: "/syllabus",
      accordion: "accordion",
      // children: <AccordionHome />,
    },
    {
      id: 1,
      name: "Placements",
      path: "/placements",
      accordion: "accordion",
      // children: <AccordionHome />,
    },

  ];

  return (
    <div className="pt-8 pb-6 border-b border-borderColor dark:border-borderColor-dark">
      <AccordionContainer>
        {items.map((item, idx) => (
          <MobileMenuItem key={idx} item={item} />
        ))}
      </AccordionContainer>
    </div>
  );
};

export default MobileMenuItems;
