"use client";
import React, { useEffect, useMemo, useState } from "react";
import DropdownCart from "./DropdownCart";
import Link from "next/link";
import MobileMenuOpen from "@/components/shared/buttons/MobileMenuOpen";
import useIsTrue from "@/hooks/useIsTrue";
import LoginButton from "./LoginButton";
import { useRouter } from "next/navigation";
const NavbarRight = () => {
  const isHome4 = useIsTrue("/home-4");
  const isHome4Dark = useIsTrue("/home-4-dark");
  const isHome5 = useIsTrue("/home-5");
  const isHome5Dark = useIsTrue("/home-5-dark");
  const isHome2Dark = useIsTrue("/home-2-dark");

  const router = useRouter();
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  useEffect(() => {
    const token =
      sessionStorage.getItem("accessToken") ||
      localStorage.getItem("accessToken");
    setIsLoggedIn(Boolean(token));
  }, []);

  const loginButtonText = useMemo(() => {
    if (isHome2Dark) return "Get Started Free";
    if (isHome4 || isHome4Dark || isHome5 || isHome5Dark) return "Get Start Here";
    return "Login";
  }, [isHome2Dark, isHome4, isHome4Dark, isHome5, isHome5Dark]);

  const handleLogout = () => {
    const keys = ["accessToken", "refreshToken", "userId", "userRole", "userStatus"];
    keys.forEach((k) => {
      try {
        sessionStorage.removeItem(k);
      } catch (e) {}
      try {
        localStorage.removeItem(k);
      } catch (e) {}
    });
    setIsLoggedIn(false);
    router.push("/login");
  };

  return (
    <div className="lg:col-start-10 lg:col-span-3">
      <ul className="relative nav-list flex justify-end items-center">
 
        {isHome4 || isHome4Dark || isHome5 || isHome5Dark ? (
          ""
        ) : (
          <li className="hidden lg:block">
            {/* <LoginButton /> */}
          </li>
        )}
        <li className="hidden lg:block">
          {isLoggedIn ? (
            <button
              type="button"
              onClick={handleLogout}
              className="text-size-12 2xl:text-size-15 text-whiteColor bg-primaryColor block border-primaryColor border hover:text-primaryColor hover:bg-white px-15px py-2 rounded-standard dark:hover:bg-whiteColor-dark dark: dark:hover:text-whiteColor"
            >
              Logout
            </button>
          ) : (
            <Link
              href="/login"
              className="text-size-12 2xl:text-size-15 text-whiteColor bg-primaryColor block border-primaryColor border hover:text-primaryColor hover:bg-white px-15px py-2 rounded-standard dark:hover:bg-whiteColor-dark dark: dark:hover:text-whiteColor"
            >
              {loginButtonText}
            </Link>
          )}
        </li>
        <li className="block lg:hidden">
          <MobileMenuOpen />
        </li>
      </ul>
    </div>
  );
};

export default NavbarRight;
