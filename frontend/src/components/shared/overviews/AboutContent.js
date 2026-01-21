"use client";
import Image from "next/image";
import overviewImage from "@/assets/images/about/uuuuuuuuuuuuu.jpg";
import overviewKidImage from "@/assets/images/about/overview_kg.png";
import useIsTrue from "@/hooks/useIsTrue";

const AboutContent = () => {
  const isHome9 = useIsTrue("/home-9");
  const isHome9Dark = useIsTrue("/home-9-dark");
  const isAbout = useIsTrue("/about");
  const isAboutDark = useIsTrue("/about-dark");

  return (
    <div>
      <p className="text-contentColor dark:text-contentColor-dark mb-25px">
        Nethaji Degree Siricilla is dedicated to providing high-quality education to help students discover and achieve their full potential. We aim to create a strong foundation for every student, empowering them with the skills and confidence to succeed in their future careers.
      </p>

      {isAbout || isAboutDark ? (
        <>
          <h4 className="text-xl font-medium text-blackColor dark:text-blackColor-dark">
            Top-Quality Education – Specializing in Computer Science
          </h4>
          <p className="text-contentColor dark:text-contentColor-dark mb-25px">
            Our computer science programs are designed to meet the latest industry standards. We provide practical training, project-based learning, and expert guidance to help students become skilled developers, engineers, and IT professionals.
          </p>

          <h4 className="text-xl font-medium text-blackColor dark:text-blackColor-dark">
            Web & User Interface Design – Practical Development Skills
          </h4>
          <p className="text-contentColor dark:text-contentColor-dark mb-30px">
            At Nethaji College, we focus on real-world skills like web design and development. Students learn to build modern websites and user interfaces using the latest technologies, preparing them for high-demand careers.
          </p>

          <h4 className="text-xl font-medium text-blackColor dark:text-blackColor-dark">
            Creative Learning – Design & Animation Skills
          </h4>
          <p className="text-contentColor dark:text-contentColor-dark mb-30px">
            Our programs encourage creativity and innovation. With hands-on projects in design and animation, students can explore their artistic talents while developing professional-level digital skills.
          </p>
        </>
      ) : (
        <Image
          src={isHome9 || isHome9Dark ? overviewKidImage : overviewImage}
          alt="About Overview"
          className="w-full"
          placeholder="blur"
        />
      )}
    </div>
  );
};

export default AboutContent;
