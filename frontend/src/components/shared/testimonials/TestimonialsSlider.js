"use client";
import React from "react";
import testimonialImage1 from "@/assets/images/testimonial/360_F_626637222_vTb3dAhrpb47oskhaCQjXFGPAygwDaD8.jpg";
import { Swiper, SwiperSlide } from "swiper/react";
import TestimonialSlide from "./TestimonialSlide";
import { Navigation } from "swiper/modules";

const TestimonialsSlider = () => {
  const testimonials = [
    {
      name: "Priya Reddy",
      image: testimonialImage1,
      desig: "B.Sc Data Science Student",
      desc: "Nethaji Degree College has transformed my learning journey. The faculty is incredibly supportive and the curriculum is very relevant to today’s industry needs.",
    },
    {
      name: "Ravi Kumar",
      image: testimonialImage1,
      desig: "Parent of Bs.c Student",
      desc: "As a parent, I’m very satisfied with the quality of education and safety provided by the college. My son is more confident and focused since joining.",
    },
    {
      name: "Sneha Patil",
      image: testimonialImage1,
      desig: "B.Com Business Analytics",
      desc: "The blend of theoretical knowledge and practical exposure has been excellent. I’ve gained a strong foundation in business and analytics.",
    },
    {
      name: "Anil Raj",
      image: testimonialImage1,
      desig: "Alumni (BBA - AI)",
      desc: "The AI program helped me land a great internship and later a job at a tech startup. Thanks to the guidance and placement support from Nethaji!",
    },
    {
      name: "Divya Sharma",
      image: testimonialImage1,
      desig: "B.Sc Food Science Student",
      desc: "The lab facilities and experienced faculty made learning hands-on and exciting. I now feel well-prepared for my future career in food technology.",
    },
    {
      name: "Karthik Velugu",
      image: testimonialImage1,
      desig: "Current Student, B.sc",
      desc: "The campus environment is peaceful and student-friendly. I enjoy the interactive teaching methods and regular workshops.",
    },
    { 
      name: "Suma Rao",
      image: testimonialImage1,
      desig: "Alumni (B.Com Computer Applications)",
      desc: "Thanks to Nethaji, I gained the skills and confidence to start my career in the IT industry. The support from staff was excellent.",
    },
    {
      name: "Rahul Yadav",
      image: testimonialImage1,
      desig: "Student, B.Sc Data Science",
      desc: "The college offers great exposure through seminars, coding sessions, and industrial visits. It's been a great decision to join here.",
    },
  ];

  return (
    <Swiper
      className="mySwiper"
      slidesPerView={1}
      breakpoints={{
        768: {
          slidesPerView: 2,
        },
      }}
      loop={true}
      navigation={true}
      modules={[Navigation]}
    >
      {testimonials.map((testimonial, idx) => (
        <SwiperSlide key={idx}>
          <TestimonialSlide testimonial={testimonial} />
        </SwiperSlide>
      ))}
    </Swiper>
  );
};

export default TestimonialsSlider;
