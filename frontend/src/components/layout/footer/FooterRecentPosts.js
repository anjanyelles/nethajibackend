import Image from "next/image";
import React from "react";
import post1 from "@/assets/images/footer/footer__1.png";
import post2 from "@/assets/images/footer/footer__2.png";
import post3 from "@/assets/images/footer/footer__3.png";
import FooterHeading from "@/components/shared/headings/FooterHeading";
import FooterRecentPost from "./FooterRecentPost";

const FooterRecentPosts = () => {
  const posts = [
    {
      title: "New BSc Data Science Course Launch",
      image: post1,
      date: "15 May 2025",
      id: 1,
    },
    {
      title: "Annual Cultural Day Celebration",
      image: post2,
      date: "10 May 2025",
      id: 2,
    },
    {
      title: "Campus Placement Drive Success",
      image: post3,
      date: "05 May 2025",
      id: 3,
    },
  ];

  return (
    <div
      className="sm:col-start-1 sm:col-span-12 md:col-start-7 md:col-span-6 lg:col-start-10 lg:col-span-3 pl-0 2xl:pl-50px"
      data-aos="fade-up"
    >
      {/* <FooterHeading className="text-size-22 font-bold text-whiteColor mb-3">
        Latest News & Events
      </FooterHeading>
      <ul className="flex flex-col gap-y-5">
        {posts.map((post, idx) => (
          <FooterRecentPost key={idx} post={post} />
        ))}
      </ul> */}
    </div>
  );
};

export default FooterRecentPosts;