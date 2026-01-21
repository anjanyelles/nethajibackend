"use client";

import { useEffect } from "react";
import theme from "@/libs/theme";
const ThemeController = () => {
  useEffect(() => {
    theme();
  }, []);
  return (
    <div className="fixed top-[100px] 3xl:top-[300px] transition-all duration-300 right-[-50px] hover:right-0 z-xl">
      <button className="theme-controller w-0px h-0 bg-primaryColor dark:bg-whiteColor-dark rounded-l-lg2 text-whiteColor px-10px flex items-center dark:shadow-theme-controller">
    
      </button>
    </div>
  );
};

export default ThemeController;
