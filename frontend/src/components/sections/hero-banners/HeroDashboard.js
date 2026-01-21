"use client";
import dashboardImage2 from "@/assets/images/dashbord/dashbord__2.jpg";
import teacherImage2 from "@/assets/images/teacher/teacher__2.png";
import Image from "next/image";
import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import { useEffect, useState } from "react";

const HeroDashboard = () => {
  const router = useRouter();
  const pathname = usePathname();
  const partOfPathNaem = pathname.split("/")[2].split("-")[0];
  const isAdmin = partOfPathNaem === "admin" ? true : false;
  const isInstructor = partOfPathNaem === "instructor" ? true : false;


   const [userRole, setUserRole] = useState(null);
  const [userName, setUserName] = useState("");
  const [courseCount, setCourseCount] = useState(0);
  const [certificateCount, setCertificateCount] = useState(0);

  useEffect(() => {
    setUserRole(sessionStorage.getItem("userRole"));
    setUserName(sessionStorage.getItem("userName") || "User");

    // optional (for students)
    setCourseCount(Number(sessionStorage.getItem("courseCount")) || 0);
    setCertificateCount(Number(sessionStorage.getItem("certificateCount")) || 0);
  }, []);

  const handleLogout = () => {
    localStorage.clear();
    sessionStorage.clear();
    router.push("/");
  };
  return (
    <section>
      <div className="container-fluid-2">
        <div
          className={`${
            isAdmin
              ? "bg-primaryColor"
              : isInstructor
              ? "bg-naveBlue"
              : "bg-skycolor"
          } p-5 md:p-10 rounded-5 flex justify-center md:justify-between items-center flex-wrap gap-2`}
        >
          <div className="flex items-center flex-wrap justify-center sm:justify-start">
            <div className="mr-10px lg:mr-5">
              <Image
                src={isAdmin || isInstructor ? dashboardImage2 : teacherImage2}
                alt=""
                className="w-27 h-27 md:w-22 md:h-22 lg:w-27 lg:h-27 rounded-full p-1 border-2 border-darkdeep7 box-content"
              />
            </div>{" "}
       {isAdmin || isInstructor ? (
        <div className="text-whiteColor font-bold text-center sm:text-start">
          <h5 className="text-xl leading-1.2 mb-5px">
            Hello
          </h5>
          <h2 className="text-2xl leading-1.24">
            {userRole}
          </h2>
        </div>
      ) : (
        <div className="text-whiteColor font-bold text-center sm:text-start">
          <h5 className="text-2xl leading-1.24 mb-5px">
            {userRole}
          </h5>

          <ul className="flex items-center gap-15px">
            <li className="text-sm font-normal flex items-center gap-1">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="18"
                height="18"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
                className="feather feather-book-open"
              >
                <path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z" />
                <path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z" />
              </svg>
              {courseCount} Courses Enrolled
            </li>

            <li className="text-sm font-normal flex items-center gap-1">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="18"
                height="18"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
                className="feather feather-award"
              >
                <circle cx="12" cy="8" r="7" />
                <polyline points="8.21 13.89 7 23 12 20 17 23 15.79 13.88" />
              </svg>
              {certificateCount} Certificates
            </li>
          </ul>
        </div>
      )}

          </div>
          {isAdmin || isInstructor ? (
            <div className="text-center">
              <div className="text-yellow flex items-center justify-center gap-2">
                {" "}
                <i className="icofont-star"></i>{" "}
                <i className="icofont-star"></i>{" "}
                <i className="icofont-star"></i>{" "}
                <i className="icofont-star"></i>{" "}
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  width="16"
                  height="24"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  className="feather feather-star inline-block"
                >
                  <polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"></polygon>
                </svg>
                <button
                  type="button"
                  onClick={handleLogout}
                  className="ml-2 inline-flex items-center rounded bg-whiteColor/20 px-3 py-1 text-sm font-medium text-whiteColor hover:bg-whiteColor/30"
                >
                  Logout
                </button>
              </div>
              {/* <p className="text-whiteColor">4.0 (120 Reviews)</p> */}
            </div>
          ) : (
            ""
          )}
        </div>
      </div>
    </section>
  );
};

export default HeroDashboard;
