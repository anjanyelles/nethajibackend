"use client";
import { useRouter } from "next/navigation";
import React, { useState } from "react";
import { loginWithEmailPassword } from "@/services/authService";

const LoginForm = () => {
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    rememberMe: false
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value
    }));
  };

const router = useRouter();

const handleSubmit = async () => {
  setError("");
  setLoading(true);

  try {
    const data = await loginWithEmailPassword(formData.email, formData.password);

    if (data.status === "Login Successful") {
      // ✅ Store values in sessionStorage
      sessionStorage.setItem("accessToken", data.accessToken);
      sessionStorage.setItem("refreshToken", data.refreshToken);
      sessionStorage.setItem("userId", data.userId);
      sessionStorage.setItem("userRole", data.userRole);
      sessionStorage.setItem("userStatus", data.userStatus);

      // ✅ Navigate based on role
      if (data.userRole === "ADMIN" || data.userRole === "SUPER_ADMIN") {
        router.push("/dashboards/admin-dashboard");
      } else if (data.userRole === "LECTURER") {
        router.push("/dashboards/instructor-dashboard");
      } else {
        router.push("/dashboards/student-dashboard");
      }
    } else {
      setError(data.status || "Login failed. Please try again.");
    }
  } catch (error) {
    console.error("Login error:", error);
    const msg =
      error?.status ||
      error?.response?.data?.status ||
      error?.response?.data?.message ||
      error?.message ||
      "Network error. Please check your connection and try again.";
    setError(msg);
  } finally {
    setLoading(false);
  }
};

  const handleKeyPress = (e) => {
    if (e.key === 'Enter') {
      handleSubmit();
    }
  };

  return (
    <div className="opacity-100 transition-opacity duration-150 ease-linear">
      {/* heading */}
      <div className="text-center">
        <h3 className="text-size-32 font-bold text-blackColor dark:text-blackColor-dark mb-2 leading-normal">
          Login
        </h3>
        {/* <p className="text-contentColor dark:text-contentColor-dark mb-15px">
          Don't have an account yet?
          <a
            href="login.html"
            className="hover:text-primaryColor relative after:absolute after:left-0 after:bottom-0.5 after:w-0 after:h-0.5 after:bg-primaryColor after:transition-all after:duration-300 hover:after:w-full ml-1"
          >
            Sign up for free
          </a>
        </p> */}
      </div>

      <div className="pt-25px">
        {error && (
          <div className="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded">
            {error}
          </div>
        )}

        <div className="mb-25px">
          <label className="text-contentColor dark:text-contentColor-dark mb-10px block">
            Username or email
          </label>
          <input
            type="text"
            name="email"
            value={formData.email}
            onChange={handleInputChange}
            onKeyPress={handleKeyPress}
            placeholder="Your username or email"
            className="w-full h-52px leading-52px pl-5 bg-transparent text-sm focus:outline-none text-contentColor dark:text-contentColor-dark border border-borderColor dark:border-borderColor-dark placeholder:text-placeholder placeholder:opacity-80 font-medium rounded"
          />
        </div>

        <div className="mb-25px">
          <label className="text-contentColor dark:text-contentColor-dark mb-10px block">
            Password
          </label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleInputChange}
            onKeyPress={handleKeyPress}
            placeholder="Password"
            className="w-full h-52px leading-52px pl-5 bg-transparent text-sm focus:outline-none text-contentColor dark:text-contentColor-dark border border-borderColor dark:border-borderColor-dark placeholder:text-placeholder placeholder:opacity-80 font-medium rounded"
          />
        </div>

        <div className="text-contentColor dark:text-contentColor-dark flex items-center justify-between">
          <div className="flex items-center">
            <input
              type="checkbox"
              id="remember"
              name="rememberMe"
              checked={formData.rememberMe}
              onChange={handleInputChange}
              className="w-18px h-18px mr-2 block box-content"
            />
            <label htmlFor="remember"> Remember me</label>
          </div>
          <div>
            <a
              href="#"
              className="hover:text-primaryColor relative after:absolute after:left-0 after:bottom-0.5 after:w-0 after:h-0.5 after:bg-primaryColor after:transition-all after:duration-300 hover:after:w-full"
            >
              Forgot your password?
            </a>
          </div>
        </div>

        <div className="my-25px text-center">
          <button
            onClick={handleSubmit}
            disabled={loading}
            className="text-size-15 text-whiteColor bg-primaryColor px-25px py-10px w-full border border-primaryColor hover:text-primaryColor hover:bg-whiteColor inline-block rounded group dark:hover:text-whiteColor dark:hover:bg-whiteColor-dark disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {loading ? "Logging in..." : "Log in"}
          </button>
        </div>

        {/* other login */}
        {/* <p></p> */}
        {/* <div>
          <p className="text-contentColor dark:text-contentColor-dark text-center relative mb-15px before:w-2/5 before:h-1px before:bg-borderColor4 dark:before:bg-borderColor2-dark before:absolute before:left-0 before:top-4 after:w-2/5 after:h-1px after:bg-borderColor4 dark:after:bg-borderColor2-dark after:absolute after:right-0 after:top-4">
            or Log-in with
          </p>
        </div> */}

        {/* <div className="text-center flex gap-x-1 md:gap-x-15px lg:gap-x-25px gap-y-5 items-center justify-center flex-wrap">
          <button
            className="text-size-15 text-whiteColor bg-primaryColor px-11 py-10px border border-primaryColor hover:text-primaryColor hover:bg-whiteColor inline-block rounded group dark:hover:text-whiteColor dark:hover:bg-whiteColor-dark"
          >
            <i className="icofont-facebook"></i> Facebook
          </button>
          <button
            className="text-size-15 text-whiteColor bg-primaryColor px-11 py-10px border border-primaryColor hover:text-primaryColor hover:bg-whiteColor inline-block rounded group dark:hover:text-whiteColor dark:hover:bg-whiteColor-dark"
          >
            <i className="icofont-google-plus"></i> Google
          </button>
        </div> */}
      </div>
    </div>
  );
};

export default LoginForm;