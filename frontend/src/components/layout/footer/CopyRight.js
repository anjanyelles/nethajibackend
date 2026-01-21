import Image from "next/image";
import React from "react";
import logoImage from "@/assets/images/logo/Nethaji Degree College.png";
import useIsSecondary from "@/hooks/useIsSecondary";

const CopyRight = () => {
  const { isSecondary } = useIsSecondary();
  return (
    <div>
      {isSecondary ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-5 pt-10 lg:mt-4 items-center border-t border-darkcolor">
          <div>
            <p className="text-base text-center sm:text-start text-darkgray">
              © 2024 Powered by{" "}
              <a href="https://nethajicollege.edu.in" className="hover:text-primaryColor">
                Nethaji Degree College
              </a>{" "}
              . All Rights Reserved.    
            </p>
          </div>

          <div>
            <ul className="flex items-center justify-center sm:justify-end">
              <li>
                <a
                  href="/terms"
                  className="text-base text-darkgray hover:text-primaryColor pr-4 border-r border-darkgray leading-1"
                >
                  Terms of Use
                </a>
              </li>
              <li>
                <a
                  href="/privacy"
                  className="text-base text-darkgray hover:text-primaryColor pl-4"
                >
                  Privacy Policy
                </a>
              </li>
            </ul>
          </div>
        </div>
      ) : (
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-5 lg:gap-30px pt-10 items-center">
          <div className="lg:col-start-1 lg:col-span-3">
            <a href="/">
              <Image src={logoImage} alt="Nethaji College Logo" placeholder="blur" />
            </a>
          </div>

          <div className="lg:col-start-4 lg:col-span-6">
            <p className="text-whiteColor">
              Copyright © <span className="text-primaryColor">2024 </span> by{" "}
              Nethaji Degree College. All Rights Reserved.
            </p>
          </div>

          <div className="lg:col-start-10 lg:col-span-3">
            <ul className="flex gap-3 lg:gap-2 2xl:gap-3 lg:justify-end">
              <li>
                <a
                  href="https://www.facebook.com/nethajicollege"
                  className="w-40.19px lg:w-35px 2xl:w-40.19px h-37px lg:h-35px 2xl:h-37px leading-37px lg:leading-35px 2xl:leading-37px text-whiteColor bg-whiteColor bg-opacity-10 hover:bg-primaryColor text-center"
                >
                  <i className="icofont-facebook"></i>
                </a>
              </li>
              <li>
                <a
                  href="https://www.twitter.com/nethajicollege"
                  className="w-40.19px lg:w-35px 2xl:w-40.19px h-37px lg:h-35px 2xl:h-37px leading-37px lg:leading-35px 2xl:leading-37px text-whiteColor bg-whiteColor bg-opacity-10 hover:bg-primaryColor text-center"
                >
                  <i className="icofont-twitter"></i>
                </a>
              </li>
              <li>
                <a
                  href="https://www.youtube.com/nethajicollege"
                  className="w-40.19px lg:w-35px 2xl:w-40.19px h-37px lg:h-35px 2xl:h-37px leading-37px lg:leading-35px 2xl:leading-37px text-whiteColor bg-whiteColor bg-opacity-10 hover:bg-primaryColor text-center"
                >
                  <i className="icofont-youtube-play"></i>
                </a>
              </li>
              <li>
                <a
                  href="https://www.linkedin.com/school/nethajicollege"
                  className="w-40.19px lg:w-35px 2xl:w-40.19px h-37px lg:h-35px 2xl:h-37px leading-37px lg:leading-35px 2xl:leading-37px text-whiteColor bg-whiteColor bg-opacity-10 hover:bg-primaryColor text-center"
                >
                  <i className="icofont-linkedin"></i>
                </a>
              </li>
            </ul>
          </div>
        </div>
      )}
    </div>
  );
};

export default CopyRight;
