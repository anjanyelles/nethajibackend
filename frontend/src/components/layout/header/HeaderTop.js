"use client";
import useIsTrue from "@/hooks/useIsTrue";
import React from "react";

const HeaderTop = () => {
  const isHome1 = useIsTrue("/");
  const isHome1Dark = useIsTrue("/home-1-dark");
  const isHome4 = useIsTrue("/home-4");
  const isHome4Dark = useIsTrue("/home-4-dark");
  const isHome5 = useIsTrue("/home-5");
  const isHome5Dark = useIsTrue("/home-5-dark");

  return (
<div className="bg-[#001F3F] text-white hidden lg:block">
  <div className="container mx-auto text-white text-sm py-2 px-4 flex justify-between items-center">
    {/* Contact Info */}
    <div>
      <p>
        📞 Call Us: <span className="font-semibold">834 000 7081</span> &nbsp; | &nbsp;
        📧 Mail Us: <span className="font-semibold">nethajidc@gmail.com</span>
      </p>
    </div>

    {/* Location & Social Media */}
    <div className="flex gap-6 items-center">
      {/* Location */}
      <div className="flex items-center">
        <i className="icofont-location-pin text-yellow-400 mr-2"></i>
        <span>Auto Nagar, Karimnagar Road, Sircilla</span>
      </div>

      {/* Social Links */}
      <ul className="flex gap-4 text-lg">
        <li>
          <a href="https://facebook.com/nethajicollege" className="hover:text-yellow-300">
            <i className="icofont-facebook"></i>
          </a>
        </li>
        <li>
          <a href="https://twitter.com/nethajicollege" className="hover:text-yellow-300">
            <i className="icofont-twitter"></i>
          </a>
        </li>
        <li>
          <a href="https://instagram.com/nethajicollege" className="hover:text-yellow-300">
            <i className="icofont-instagram"></i>
          </a>
        </li>
        <li>
          <a href="https://youtube.com/nethajicollege" className="hover:text-yellow-300">
            <i className="icofont-youtube-play"></i>
          </a>
        </li>
      </ul>
    </div>
  </div>
</div>

  );
};

export default HeaderTop;
