import Image from "next/image";

import aboutImage8 from "@/assets/images/about/about_8 copy.png";
// import aboutImage14 from "@/assets/images/about/Untitled-1-2.jpg";
import aboutImage14 from "@/assets/images/about/Untitled-5-3.jpg";
import aboutImage10 from "@/assets/images/about/Untitled-7-3.jpg";
import aboutImage15 from "@/assets/images/about/about_15.png";
import aboutImage16 from "@/assets/images/about/Untitled-3-2.jpg";


import SectionName from "@/components/shared/section-names/SectionName";

import HeadingSecondary from "@/components/shared/headings/HeadingSecondary";
import ButtonPrimary from "@/components/shared/buttons/ButtonPrimary";
import TiltWrapper from "@/components/shared/wrappers/TiltWrapper";

const Placementsdata = () => {
  return (
    <>
    <section>
      <div className="container py-50px md:py-70px lg:py-20 2xl:py-100px">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-30px">
          {/* about left */}
          <div data-aos="fade-up">
            <TiltWrapper>
              <div className="tilt relative overflow-hidden z-0">
          
                <Image className="w-full" src={aboutImage14} alt="" />
              </div>
            </TiltWrapper>
          </div>

          {/* about right */}
          <div data-aos="fade-up" className="2xl:ml-65px">
            <SectionName>Placement Cell </SectionName>
            <HeadingSecondary>
            About Placement Cell – Nethaji Degree College
            </HeadingSecondary>
            <p className="text-sm md:text-base leading-7 text-contentColor dark:text-contentColor-dark mb-25px">
            The Training and Placement Cell at Nethaji Degree College, Sircilla serves as a vital link between academic learning and industry expectations. Recognizing the importance of career readiness, the college places a strong emphasis on Industrial Training and Practical Exposure from the early stages of each program.
            </p>
            <p className="flex items-center gap-x-4 text-lg text-blackColor dark:text-blackColor-dark mb-25px">
              <Image loading="lazy" src={aboutImage15} alt="about" />
              <span>
                <b>Empowering Students</b> with Knowledge, Skills & Success-driven Education.
              </span>
            </p>
            <p className="text-sm md:text-base leading-7 text-contentColor dark:text-contentColor-dark">
            Our students gain hands-on experience through workshops, factory visits, industrial installations, and real-time training sessions that bridge the gap between theory and application.

The Placement Cell centrally manages campus recruitment activities for all undergraduate programs including B.Sc (Data Science, Food Science, M.P.Cs), B.Com (Business Analytics, Computer Applications), and BBA (Artificial Intelligence).
            </p>

            <div className="mt-30px">
              <ButtonPrimary path="/placements" arrow={true}>
                Learn More
              </ButtonPrimary>
            </div>
          </div>
        </div>
      </div>
    </section>

<section>
<div className="container py-50px md:py-70px lg:py-20 2xl:py-100px">
  <div className="grid grid-cols-1 lg:grid-cols-2 gap-30px">
    {/* about left */}
   

    {/* about right */}
    <div data-aos="fade-up" className="2xl:ml-65px">

      <HeadingSecondary>
      Comprehensive Career Guidance & Placement Training – Nethaji Degree College
      </HeadingSecondary>
      <p className="text-sm md:text-base leading-7 text-contentColor dark:text-contentColor-dark mb-25px">
      At Nethaji Degree College, we believe that career readiness begins from day one. That’s why we offer a structured and continuous career guidance program integrated with the academic curriculum from the first year itself. Our goal is to empower students with the skills, confidence, and knowledge required to excel in today’s competitive job market.
      </p>
   
      <p className="text-sm md:text-base leading-7 text-contentColor dark:text-contentColor-dark">
       Company-Specific Training
Tailored sessions that prepare students for the hiring patterns, interview processes, and expectations of top recruiters.
      </p>



      <p className="text-sm md:text-base leading-7 text-contentColor dark:text-contentColor-dark">
      Technical-Oriented Training
In-depth technical sessions aligned with the students' core subjects to ensure they are industry-ready and confident in facing technical interviews.
      </p>


      <div className="mt-30px">
        <ButtonPrimary path="/placements" arrow={true}>
          Learn More
        </ButtonPrimary>
      </div>
    </div>

    <div data-aos="fade-up">
      <TiltWrapper>
        <div className="tilt relative overflow-hidden z-0">
       
          <Image className="w-full" src={aboutImage16} alt="" />
        </div>
      </TiltWrapper>
    </div>
  </div>
</div>
</section>

   
<section>
      <div className="container py-50px md:py-70px lg:py-20 2xl:py-100px">
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-30px">
          {/* about left */}
          <div data-aos="fade-up">
            <TiltWrapper>
              <div className="tilt relative overflow-hidden z-0">
                <Image
                  className="absolute left-0 top-0 lg:top-4 right-0 mx-auto -z-1"
                  src={aboutImage10}
                  alt=""
                />
                <Image className="w-full" src={aboutImage10} alt=""   />
              </div>
            </TiltWrapper>
          </div>

          {/* about right */}
          <div data-aos="fade-up" className="2xl:ml-65px">
        
            <HeadingSecondary>
            Mock Interviews & Skill Assessment – Nethaji Degree College
            </HeadingSecondary>
            <p className="text-sm md:text-base leading-7 text-contentColor dark:text-contentColor-dark mb-25px">
            At Nethaji Degree College, we ensure our students are fully prepared to face real-world interviews with confidence and competence. To achieve this, we conduct regular mock interview sessions and skill assessments designed to simulate actual placement scenarios and enhance overall performance.
            </p>
            <p className="flex items-center gap-x-4 text-lg text-blackColor dark:text-blackColor-dark mb-25px">
              <Image loading="lazy" src={aboutImage15} alt="about" />
              <span>
                <b>Understand the interview process</b> 
              </span>
            </p>
            <p className="flex items-center gap-x-4 text-lg text-blackColor dark:text-blackColor-dark mb-25px">
              <Image loading="lazy" src={aboutImage15} alt="about" />
              <span>
                <b>Gain confidence in communication and presentation</b> 
              </span>
            </p>

            <p className="flex items-center gap-x-4 text-lg text-blackColor dark:text-blackColor-dark mb-25px">
              <Image loading="lazy" src={aboutImage15} alt="about" />
              <span>
                <b>Identify strengths and work on areas of improvement</b> 
              </span>
            </p>
          

   
          </div>
        </div>
      </div>
    </section>
</>
  );
};

export default Placementsdata;
