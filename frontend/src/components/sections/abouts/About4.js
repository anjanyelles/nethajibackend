import React from "react";
import About1 from "./About1";
import aboutImage17 from "@/assets/images/about/Whatappimage.jpeg";
const About4 = () => {
  return (
    <About1 image={aboutImage17} hideCounter={true}>
Welcome to{" "}
<span className="relative after:w-full after:h-[7px] z-0 after:bg-secondaryColor after:absolute after:left-0 after:bottom-3 after:-z-1 md:after:bottom-5">
  Nethaji Degree College
</span>{" "}
    

    </About1>
  );
};

export default About4;
