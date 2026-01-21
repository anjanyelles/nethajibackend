import React, { useState } from "react";

const SignUpForm = () => {
  const [step, setStep] = useState(1); // 1: Initial Form, 2: OTP Verification
  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    mobileNumber: "",
    password: "",
    confirmPassword: "",
    acceptTerms: false,
    emailOtp: "",
    primaryType: "STUDENT" // Default role
  });
  const [otpData, setOtpData] = useState({
    emailOtpSession: "",
    salt: ""
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value
    }));
  };

  const validateStep1 = () => {
    if (!formData.firstName.trim()) {
      setError("First name is required");
      return false;
    }
    if (!formData.lastName.trim()) {
      setError("Last name is required");
      return false;
    }
    if (!formData.email.trim() || !/\S+@\S+\.\S+/.test(formData.email)) {
      setError("Valid email is required");
      return false;
    }
    if (!formData.mobileNumber.trim() || !/^\d{10}$/.test(formData.mobileNumber)) {
      setError("Valid 10-digit mobile number is required");
      return false;
    }
    if (formData.password.length < 6) {
      setError("Password must be at least 6 characters");
      return false;
    }
    if (formData.password !== formData.confirmPassword) {
      setError("Passwords do not match");
      return false;
    }
    if (!formData.acceptTerms) {
      setError("Please accept the Terms and Privacy Policy");
      return false;
    }
    return true;
  };

  const handleSendOTP = async () => {
    setError("");
    setSuccess("");
    
    if (!validateStep1()) {
      return;
    }

    setLoading(true);

    try {
      const response = await fetch(
        "http://13.200.240.128:9029/api/nethaji-service/auth/registration",
        {
          method: "POST",
          headers: {
            "accept": "*/*",
            "Content-Type": "application/json"
          },
          body: JSON.stringify({
            email: formData.email,
            mobileNumber: formData.mobileNumber,
            firstName: formData.firstName,
            lastName: formData.lastName
          })
        }
      );

      const data = await response.json();

      if (response.status === 200 && data.emailOtpSession) {
        // Store OTP session data
        setOtpData({
          emailOtpSession: data.emailOtpSession,
          salt: data.salt
        });
        setSuccess("OTP sent successfully to your email!");
        setStep(2); // Move to OTP verification step
      } else if (response.status === 409) {
        setError("Email or mobile number already registered. Please use different credentials.");
      } else {
        setError(data.message || "Failed to send OTP. Please try again.");
      }
    } catch (err) {
      setError("Network error. Please check your connection and try again.");
      console.error("Send OTP error:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleCompleteRegistration = async () => {
    setError("");
    setSuccess("");

    if (!formData.emailOtp.trim()) {
      setError("Please enter the OTP sent to your email");
      return;
    }

    setLoading(true);

    try {
      const response = await fetch(
        "http://13.200.240.128:9029/api/nethaji-service/auth/registration",
        {
          method: "POST",
          headers: {
            "accept": "*/*",
            "Content-Type": "application/json"
          },
          body: JSON.stringify({
            email: formData.email,
            mobileNumber: formData.mobileNumber,
            firstName: formData.firstName,
            lastName: formData.lastName,
            emailOtp: formData.emailOtp,
            salt: otpData.salt,
            emailOtpSession: otpData.emailOtpSession,
            password: formData.password,
            primaryType: formData.primaryType
          })
        }
      );

      const data = await response.json();

      if (response.ok) {
        setSuccess("Registration successful! Redirecting to login...");
        setTimeout(() => {
          window.location.href = "/login";
        }, 2000);
      } else {
        setError(data.message || "OTP verification failed. Please check and try again.");
      }
    } catch (err) {
      setError("Network error. Please check your connection and try again.");
      console.error("Registration error:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleResendOTP = () => {
    setFormData(prev => ({ ...prev, emailOtp: "" }));
    handleSendOTP();
  };

  const handleBackToStep1 = () => {
    setStep(1);
    setError("");
    setSuccess("");
  };

  return (
    <div className="transition-opacity duration-150 ease-linear">
      {/* heading */}
      <div className="text-center">
        <h3 className="text-size-32 font-bold text-blackColor dark:text-blackColor-dark mb-2 leading-normal">
          {step === 1 ? "Sign Up" : "Verify OTP"}
        </h3>
        <p className="text-contentColor dark:text-contentColor-dark mb-15px">
          {step === 1 ? (
            <>
              Already have an account?
              <a
                href="/login"
                className="hover:text-primaryColor relative after:absolute after:left-0 after:bottom-0.5 after:w-0 after:h-0.5 after:bg-primaryColor after:transition-all after:duration-300 hover:after:w-full ml-1"
              >
                Log In
              </a>
            </>
          ) : (
            "Enter the OTP sent to your email"
          )}
        </p>
      </div>

      <div className="pt-25px">
        {error && (
          <div className="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded">
            {error}
          </div>
        )}
        {success && (
          <div className="mb-4 p-3 bg-green-100 border border-green-400 text-green-700 rounded">
            {success}
          </div>
        )}

        {step === 1 ? (
          <>
            {/* Step 1: Registration Form */}
            <div className="grid grid-cols-1 lg:grid-cols-2 lg:gap-x-30px gap-y-25px mb-25px">
              <div>
                <label className="text-contentColor dark:text-contentColor-dark mb-10px block">
                  First Name *
                </label>
                <input
                  type="text"
                  name="firstName"
                  value={formData.firstName}
                  onChange={handleInputChange}
                  placeholder="First Name"
                  className="w-full h-52px leading-52px pl-5 bg-transparent text-sm focus:outline-none text-contentColor dark:text-contentColor-dark border border-borderColor dark:border-borderColor-dark placeholder:text-placeholder placeholder:opacity-80 font-medium rounded"
                />
              </div>
              <div>
                <label className="text-contentColor dark:text-contentColor-dark mb-10px block">
                  Last Name *
                </label>
                <input
                  type="text"
                  name="lastName"
                  value={formData.lastName}
                  onChange={handleInputChange}
                  placeholder="Last Name"
                  className="w-full h-52px leading-52px pl-5 bg-transparent text-sm focus:outline-none text-contentColor dark:text-contentColor-dark border border-borderColor dark:border-borderColor-dark placeholder:text-placeholder placeholder:opacity-80 font-medium rounded"
                />
              </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-2 lg:gap-x-30px gap-y-25px mb-25px">
              <div>
                <label className="text-contentColor dark:text-contentColor-dark mb-10px block">
                  Email *
                </label>
                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleInputChange}
                  placeholder="Your Email"
                  className="w-full h-52px leading-52px pl-5 bg-transparent text-sm focus:outline-none text-contentColor dark:text-contentColor-dark border border-borderColor dark:border-borderColor-dark placeholder:text-placeholder placeholder:opacity-80 font-medium rounded"
                />
              </div>
              <div>
                <label className="text-contentColor dark:text-contentColor-dark mb-10px block">
                  Mobile Number *
                </label>
                <input
                  type="text"
                  name="mobileNumber"
                  value={formData.mobileNumber}
                  onChange={handleInputChange}
                  placeholder="10-digit Mobile Number"
                  maxLength="10"
                  className="w-full h-52px leading-52px pl-5 bg-transparent text-sm focus:outline-none text-contentColor dark:text-contentColor-dark border border-borderColor dark:border-borderColor-dark placeholder:text-placeholder placeholder:opacity-80 font-medium rounded"
                />
              </div>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-2 lg:gap-x-30px gap-y-25px mb-25px">
              <div>
                <label className="text-contentColor dark:text-contentColor-dark mb-10px block">
                  Password *
                </label>
                <input
                  type="password"
                  name="password"
                  value={formData.password}
                  onChange={handleInputChange}
                  placeholder="Password"
                  className="w-full h-52px leading-52px pl-5 bg-transparent text-sm focus:outline-none text-contentColor dark:text-contentColor-dark border border-borderColor dark:border-borderColor-dark placeholder:text-placeholder placeholder:opacity-80 font-medium rounded"
                />
              </div>
              <div>
                <label className="text-contentColor dark:text-contentColor-dark mb-10px block">
                  Re-Enter Password *
                </label>
                <input
                  type="password"
                  name="confirmPassword"
                  value={formData.confirmPassword}
                  onChange={handleInputChange}
                  placeholder="Re-Enter Password"
                  className="w-full h-52px leading-52px pl-5 bg-transparent text-sm focus:outline-none text-contentColor dark:text-contentColor-dark border border-borderColor dark:border-borderColor-dark placeholder:text-placeholder placeholder:opacity-80 font-medium rounded"
                />
              </div>
            </div>

            <div className="mb-25px">
              <label className="text-contentColor dark:text-contentColor-dark mb-10px block">
                User Type *
              </label>
              <select
                name="primaryType"
                value={formData.primaryType}
                onChange={handleInputChange}
                className="w-full h-52px leading-52px pl-5 pr-5 bg-transparent text-sm focus:outline-none text-contentColor dark:text-contentColor-dark border border-borderColor dark:border-borderColor-dark font-medium rounded"
              >
                <option value="STUDENT">Student</option>
                <option value="TEACHER">Teacher</option>
                <option value="ADMIN">Admin</option>
              </select>
            </div>

            <div className="text-contentColor dark:text-contentColor-dark flex items-center mb-25px">
              <input
                type="checkbox"
                id="accept-pp"
                name="acceptTerms"
                checked={formData.acceptTerms}
                onChange={handleInputChange}
                className="w-18px h-18px mr-2 block box-content"
              />
              <label htmlFor="accept-pp">Accept the Terms and Privacy Policy *</label>
            </div>

            <div className="text-center">
              <button
                onClick={handleSendOTP}
                disabled={loading}
                className="text-size-15 text-whiteColor bg-primaryColor px-25px py-10px w-full border border-primaryColor hover:text-primaryColor hover:bg-whiteColor inline-block rounded group dark:hover:text-whiteColor dark:hover:bg-whiteColor-dark disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {loading ? "Sending OTP..." : "Send OTP"}
              </button>
            </div>
          </>
        ) : (
          <>
            {/* Step 2: OTP Verification */}
            <div className="mb-25px">
              <label className="text-contentColor dark:text-contentColor-dark mb-10px block">
                Enter OTP *
              </label>
              <input
                type="text"
                name="emailOtp"
                value={formData.emailOtp}
                onChange={handleInputChange}
                placeholder="Enter 6-digit OTP"
                maxLength="6"
                className="w-full h-52px leading-52px pl-5 bg-transparent text-sm focus:outline-none text-contentColor dark:text-contentColor-dark border border-borderColor dark:border-borderColor-dark placeholder:text-placeholder placeholder:opacity-80 font-medium rounded"
              />
              <p className="text-sm text-contentColor dark:text-contentColor-dark mt-2">
                OTP sent to: <strong>{formData.email}</strong>
              </p>
            </div>

            <div className="flex gap-4 mb-25px">
              <button
                onClick={handleBackToStep1}
                disabled={loading}
                className="text-size-15 text-contentColor dark:text-contentColor-dark bg-transparent px-25px py-10px flex-1 border border-borderColor dark:border-borderColor-dark hover:bg-gray-100 dark:hover:bg-gray-800 inline-block rounded disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Back
              </button>
              <button
                onClick={handleResendOTP}
                disabled={loading}
                className="text-size-15 text-contentColor dark:text-contentColor-dark bg-transparent px-25px py-10px flex-1 border border-borderColor dark:border-borderColor-dark hover:bg-gray-100 dark:hover:bg-gray-800 inline-block rounded disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {loading ? "Resending..." : "Resend OTP"}
              </button>
            </div>

            <div className="text-center">
              <button
                onClick={handleCompleteRegistration}
                disabled={loading}
                className="text-size-15 text-whiteColor bg-primaryColor px-25px py-10px w-full border border-primaryColor hover:text-primaryColor hover:bg-whiteColor inline-block rounded group dark:hover:text-whiteColor dark:hover:bg-whiteColor-dark disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {loading ? "Verifying..." : "Complete Registration"}
              </button>
            </div>
          </>
        )}
      </div>
    </div>
  );
};

export default SignUpForm;