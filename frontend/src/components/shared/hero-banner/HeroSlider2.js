"use client";
import React, { useState } from "react";
import { Navigation, Thumbs } from "swiper/modules";
import { Swiper, SwiperSlide } from "swiper/react";
import HeroSlide2 from "./HeroSlide2";
import universityImage1 from "@/assets/images/herobanner/university_1.jpg";
import universityImage2 from "@/assets/images/herobanner/university_2.jpg";
import universityImage3 from "@/assets/images/herobanner/university_3.jpg";

import universityImage11 from "@/assets/images/herobanner/Untitled-12.jpg";
import universityImage12 from "@/assets/images/herobanner/university_2.jpg";
import universityImage13 from "@/assets/images/herobanner/Untitled-11.jpg";

import Image from "next/image";
const HeroSlider2 = () => {
  const [thumbsSwiper, setThumbsSwiper] = useState(null);
  const slides = [
    {
      title: (
        <>
          Next-Gen <span className="text-secondaryColor">Education</span> at{" "}
          <br className="hidden 2xl:block" />
          Nethaji Degree College
        </>
      ),
      tag: "EMPOWERING FUTURES",
    },
    
    {
      title: (
        <>
          Degree that’s right for your <br />
          Bright future
        </>
      ),
      tag: "Career Solution",
    },
    {
      title: (
        <>
          Build your future <br />
          with Nethaji Degree College
        </>
      ),
      tag: "Smart Education",
    },

  ];

  const thumbsImages = [universityImage1, universityImage2, universityImage3 ,universityImage11 , universityImage12, universityImage13];
  return (
    <>
      <Swiper
        navigation={true}
        grabCursor={true}
        thumbs={{ swiper: thumbsSwiper }}
        modules={[Navigation, Thumbs]}
        className="ecommerce-slider2 w-full"
      >
        {slides.map((slide, idx) => (
          <SwiperSlide key={idx}>
            <HeroSlide2 slide={slide} idx={idx} />
          </SwiperSlide>
        ))}
      </Swiper>
      <Swiper
        modules={[Thumbs]}
        watchSlidesProgress
        onSwiper={setThumbsSwiper}
        className="absolute bottom-5 left-1/2 -translate-x-1/2 w-auto"
      >
        {/* {thumbsImages.map((image, idx) => (
          <SwiperSlide
            className={`swiper-slide cursor-pointer max-w-150px rounded-lg2 ${
              idx === 2 ? "" : "mr-10px"
            } `}
            key={idx}
          >
            <Image
              src={image}
              alt=""
              placeholder="blur"
              className="w-full rounded-lg2"
            />
          </SwiperSlide>
        ))} */}
      </Swiper>
    </>
  );
};

export default HeroSlider2;

