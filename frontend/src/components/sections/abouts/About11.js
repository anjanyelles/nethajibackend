import Image from "next/image";

import aboutImage8 from "@/assets/images/about/about_8 copy.png";
import aboutImage14 from "@/assets/images/about/about_14-2.png";
import aboutImage15 from "@/assets/images/about/about_15.png";
import SectionName from "@/components/shared/section-names/SectionName";

import HeadingSecondary from "@/components/shared/headings/HeadingSecondary";
import ButtonPrimary from "@/components/shared/buttons/ButtonPrimary";
import TiltWrapper from "@/components/shared/wrappers/TiltWrapper";

const About11 = () => {
  return (
    <section>
      <div className="container py-50px md:py-70px lg:py-20 2xl:py-100px">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-30px">
          {/* about left */}
          <div data-aos="fade-up">
            <TiltWrapper>
              <div className="tilt relative overflow-hidden z-0">
                <Image
                  className="absolute left-0 top-0 lg:top-4 right-0 mx-auto -z-1"
                  src={aboutImage8}
                  alt=""
                />
                <Image className="w-full" src={aboutImage14} alt="" />
              </div>
            </TiltWrapper>
          </div>

          {/* about right */}
          <div data-aos="fade-up" className="2xl:ml-65px">
            <SectionName>About Us</SectionName>
            <HeadingSecondary>
              Welcome to Nethaji Degree College
            </HeadingSecondary>
            <p className="text-sm md:text-base leading-7 text-contentColor dark:text-contentColor-dark mb-25px">
              At Nethaji Degree College, we are committed to creating the right career path for a better tomorrow.
              We offer a variety of innovative programs such as B.Sc in Data Science, Food Science, M.P.Cs,
              B.Com (CA), B.Com (Business Analytics), and BBA (Artificial Intelligence) — all designed to blend
              academic excellence with industry relevance.
            </p>
            <p className="flex items-center gap-x-4 text-lg text-blackColor dark:text-blackColor-dark mb-25px">
              <Image loading="lazy" src={aboutImage15} alt="about" />
              <span>
                <b>Empowering Students</b> with Knowledge, Skills & Success-driven Education.
              </span>
            </p>
            <p className="text-sm md:text-base leading-7 text-contentColor dark:text-contentColor-dark">
              We provide free coaching for ICET, PG Entrance, Constable & Group exams, job-oriented computer
              classes, and spoken English with a US accent. Our modern labs, digital classrooms, and vibrant events
              ensure holistic development. We also offer scholarships and campus placement opportunities to help
              students transition smoothly into the professional world.
            </p>

            <div className="mt-30px">
              <ButtonPrimary path="/about" arrow={true}>
                Learn More
              </ButtonPrimary>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default About11;
