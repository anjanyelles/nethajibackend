import Image from "next/image";
import vissionImage from "@/assets/images/about/vision-2.jpg";

const OurVissionContent = () => {
  return (
    <div>
      <p className="text-contentColor dark:text-contentColor-dark mb-25px">
        <strong>Our Vision:</strong> At Nethaji Degree  we aim to continuously improve and grow by discovering, learning, and sharing knowledge. We offer quality higher education that meets global standards and helps young men and women become responsible and successful citizens.
      </p>
      <p className="text-contentColor dark:text-contentColor-dark mb-25px">
        We are committed to shaping our students into individuals who are:
        spiritually strong, emotionally balanced, self-reliant, morally upright, and socially responsible.
      </p>

      <ul className="space-y-3 grid grid-cols-1 lg:grid-cols-2 mb-25px">
        <li className="flex items-center group">
          <i className="icofont-check px-2 py-2 text-primaryColor bg-whitegrey3 bg-opacity-40 group-hover:bg-primaryColor group-hover:text-white group-hover:opacity-100 mr-15px dark:bg-whitegrey1-dark"></i>
          <p className="text-sm md:text-base font-medium text-blackColor dark:text-blackColor-dark">
            25+ Certified Teachers for academic excellence
          </p>
        </li>
        <li className="flex items-center group">
          <i className="icofont-check px-2 py-2 text-primaryColor bg-whitegrey3 bg-opacity-40 group-hover:bg-primaryColor group-hover:text-white group-hover:opacity-100 mr-15px dark:bg-whitegrey1-dark"></i>
          <p className="text-sm md:text-base font-medium text-blackColor dark:text-blackColor-dark">
            Online & Offline Course options for flexible learning
          </p>
        </li>
        <li className="flex items-center group">
          <i className="icofont-check px-2 py-2 text-primaryColor bg-whitegrey3 bg-opacity-40 group-hover:bg-primaryColor group-hover:text-white group-hover:opacity-100 mr-15px dark:bg-whitegrey1-dark"></i>
          <p className="text-sm md:text-base font-medium text-blackColor dark:text-blackColor-dark">
            Access to a well-stocked Book Library
          </p>
        </li>
        <li className="flex items-center group">
          <i className="icofont-check px-2 py-2 text-primaryColor bg-whitegrey3 bg-opacity-40 group-hover:bg-primaryColor group-hover:text-white group-hover:opacity-100 mr-15px dark:bg-whitegrey1-dark"></i>
          <p className="text-sm md:text-base font-medium text-blackColor dark:text-blackColor-dark">
            Hot Deals and New Arrivals in study materials
          </p>
        </li>
        <li className="flex items-center group">
          <i className="icofont-check px-2 py-2 text-primaryColor bg-whitegrey3 bg-opacity-40 group-hover:bg-primaryColor group-hover:text-white group-hover:opacity-100 mr-15px dark:bg-whitegrey1-dark"></i>
          <p className="text-sm md:text-base font-medium text-blackColor dark:text-blackColor-dark">
            Student-centered approach to teaching and mentoring
          </p>
        </li>
        <li className="flex items-center group">
          <i className="icofont-check px-2 py-2 text-primaryColor bg-whitegrey3 bg-opacity-40 group-hover:bg-primaryColor group-hover:text-white group-hover:opacity-100 mr-15px dark:bg-whitegrey1-dark"></i>
          <p className="text-sm md:text-base font-medium text-blackColor dark:text-blackColor-dark">
            Dedicated areas for university-level learning
          </p>
        </li>
        <li className="flex items-center group">
          <i className="icofont-check px-2 py-2 text-primaryColor bg-whitegrey3 bg-opacity-40 group-hover:bg-primaryColor group-hover:text-white group-hover:opacity-100 mr-15px dark:bg-whitegrey1-dark"></i>
          <p className="text-sm md:text-base font-medium text-blackColor dark:text-blackColor-dark">
            Continuous Teacher Development Programs
          </p>
        </li>
        <li className="flex items-center group">
          <i className="icofont-check px-2 py-2 text-primaryColor bg-whitegrey3 bg-opacity-40 group-hover:bg-primaryColor group-hover:text-white group-hover:opacity-100 mr-15px dark:bg-whitegrey1-dark"></i>
          <p className="text-sm md:text-base font-medium text-blackColor dark:text-blackColor-dark">
            Explore a variety of fresh educational methods
          </p>
        </li>
      </ul>

      <Image
        src={vissionImage}
        alt="Vision of Nethaji Degree College"
        className="w-full"
        placeholder="blur"
      />
    </div>
  );
};

export default OurVissionContent;
