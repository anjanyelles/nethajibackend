"use client";
import { useWishlistContext } from "@/contexts/WshlistContext";
import Image from "next/image";
import courseImage1 from "@/assets/images/course/Course1.jpeg"
import courseImage2 from "@/assets/images/course/Course2.jpeg"
import courseImage3 from "@/assets/images/course/Course3.jpeg"
import courseImage4 from "@/assets/images/course/Course4.jpeg"
import Link from "next/link";
import React from "react";
let insId = 0;
const CourseCard = ({ course, type }) => {
  const courseImages = [courseImage1, courseImage2, courseImage3, courseImage4];
  const { addProductToWishlist } = useWishlistContext();
  const {
    id,
    title,
    lesson,
    duration,
    image,
    price,
    isFree,
    insName,
    insImg,
    categories,
    filterOption,
    isActive,
    isCompleted,
    completedParchent,
  } = course;
  const depBgs = [


  
  ];

  return (

    <>
    {courseImages.map((image , index)=>(<>
      <div className={`  ${type === "primaryMd" ? "" : "sm:px-15px  mb-30px"}`}>
        <div className="p-15px bg-whiteColor shadow-brand dark:bg-darkdeep3-dark dark:shadow-brand-dark" key={index}>
          {/* card image */}
          <div className="relative mb-2">
            <Link
              href={`/courses/${id}`}
              className="w-full overflow-hidden rounded"
            >
              <Image
                src={image}
                alt=""
                priority={true}
                className="w-full transition-all duration-300 group-hover:scale-110"
              />
            </Link>
            <div className="absolute left-0 top-1 flex justify-between w-full items-center px-2">
              <div>
             
              </div>
              {/* <button
                onClick={() =>
                  addProductToWishlist({
                    ...course,
                    isCourse: true,
                    quantity: 1,
                  })
                }
                className="text-white bg-black bg-opacity-15 rounded hover:bg-primaryColor"
              >
                <i className="icofont-heart-alt text-base py-1 px-2"></i>
              </button> */}
            </div>
          </div>
          {/* card content */}
        
        </div>
    </div>
    </>))}</>
    
  );
};

export default CourseCard;
