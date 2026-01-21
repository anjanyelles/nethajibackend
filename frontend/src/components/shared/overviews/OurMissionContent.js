import Image from "next/image";
import missionImage from "@/assets/images/about/Untitled-2-4.jpg";

const OurMissionContent = () => {
  return (
    <div>
      <p className="text-contentColor dark:text-contentColor-dark mb-25px">
        At <strong>Nethaji Degree College</strong>, our mission is to empower students through transformative education that nurtures critical thinking, innovation, and integrity. We are committed to providing quality academic programs that prepare students for real-world challenges while fostering a spirit of curiosity and lifelong learning.
      </p>

      <h4 className="text-xl font-medium text-blackColor dark:text-blackColor-dark mb-2">
        Commitment to Excellence
      </h4>
      <p className="text-contentColor dark:text-contentColor-dark mb-25px">
        We strive to offer the best in education by combining academic rigor with practical skill development. Our faculty and facilities are dedicated to ensuring that every student receives the support and guidance needed to reach their highest potential.
      </p>

      <h4 className="text-xl font-medium text-blackColor dark:text-blackColor-dark mb-2">
        Modern Learning Environment
      </h4>
      <p className="text-contentColor dark:text-contentColor-dark mb-30px">
        Our campus blends traditional values with innovative digital tools to create a learning environment that is dynamic, inclusive, and future-ready. Through a mix of online and offline courses, we cater to the diverse learning needs of our students.
      </p>

      <Image src={missionImage} alt="Mission of Nethaji Degree College" className="w-full" placeholder="blur" />
    </div>
  );
};

export default OurMissionContent;
