"use client";
import React, { useState } from "react";
import ButtonPrimary from "../buttons/ButtonPrimary";
import enrollmentService from "@/services/enrollmentService";

const EnrollmentForm = () => {
  const [formData, setFormData] = useState({
    email: "",
    fullName: "",
    hallTicketNo: "",
    dateOfBirth: "",
    interGroupCollege: "",
    aadharNo: "",
    aadharMobile: "",
    whatsappNo: "",
    address: "",
    village: "",
    courses: [],
    referredBy: ""
  });

  const [errors, setErrors] = useState({});
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitMessage, setSubmitMessage] = useState("");

  const availableCourses = [
    "Mathematics",
    "Physics", 
    "Chemistry",
    "Biology",
    "Computer Science",
    "English",
    "Telugu",
    "Hindi",
    "Economics",
    "Commerce",
    "Accountancy",
    "Business Studies"
  ];

  const validateForm = () => {
    const newErrors = {};

    if (!formData.email) {
      newErrors.email = "Email is required";
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = "Email is invalid";
    }

    if (!formData.fullName.trim()) {
      newErrors.fullName = "Full name is required";
    }

    if (!formData.aadharMobile.trim()) {
      newErrors.aadharMobile = "Aadhar mobile is required";
    } else if (!/^\d{10}$/.test(formData.aadharMobile)) {
      newErrors.aadharMobile = "Mobile number must be 10 digits";
    }

    if (formData.whatsappNo && !/^\d{10}$/.test(formData.whatsappNo)) {
      newErrors.whatsappNo = "WhatsApp number must be 10 digits";
    }

    if (formData.aadharNo && !/^\d{12}$/.test(formData.aadharNo)) {
      newErrors.aadharNo = "Aadhar number must be 12 digits";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    
    // Clear error for this field
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ""
      }));
    }
  };

  const handleCourseChange = (course) => {
    setFormData(prev => ({
      ...prev,
      courses: prev.courses.includes(course)
        ? prev.courses.filter(c => c !== course)
        : [...prev.courses, course]
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }

    setIsSubmitting(true);
    setSubmitMessage("");

    try {
      const enrollmentData = {
        ...formData,
        courses: formData.courses.join(",") // Convert array to comma-separated string
      };

      // Use the service layer instead of direct fetch
      const result = await enrollmentService.submitEnrollment(enrollmentData);

      if (result.success) {
        setSubmitMessage(result.message || "Enrollment submitted successfully!");
        // Reset form after successful submission
        setFormData({
          email: "",
          fullName: "",
          hallTicketNo: "",
          dateOfBirth: "",
          interGroupCollege: "",
          aadharNo: "",
          aadharMobile: "",
          whatsappNo: "",
          address: "",
          village: "",
          courses: [],
          referredBy: ""
        });
      } else {
        setSubmitMessage(result.message || "Failed to submit enrollment");
      }
    } catch (error) {
      setSubmitMessage("An unexpected error occurred. Please try again.");
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="max-w-4xl mx-auto">
      <div className="bg-white dark:bg-gray-800 rounded-lg  p-4">
        <h2 className="text-3xl font-bold text-center mb-8 text-gray-900 dark:text-white">
          Inter Student Enrollment Form
        </h2>

        {submitMessage && (
          <div className={`mb-6 p-4 rounded-lg text-center ${
            submitMessage.includes("successfully") 
              ? "bg-green-100 text-green-700 dark:bg-green-800 dark:text-green-200" 
              : "bg-red-100 text-red-700 dark:bg-red-800 dark:text-red-200"
          }`}>
            {submitMessage}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Personal Information Section */}
          <div className="border-b border-gray-200 dark:border-gray-700 pb-6">
            <h3 className="text-xl font-semibold mb-4 text-gray-900 dark:text-white">
              Personal Information
            </h3>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Email <span className="text-red-500">*</span>
                </label>
                <input
                  type="email"
                  name="email"
                  value={formData.email}
                  onChange={handleInputChange}
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-700 dark:text-white"
                  placeholder="Enter your email"
                />
                {errors.email && (
                  <p className="mt-1 text-sm text-red-500">{errors.email}</p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Full Name <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  name="fullName"
                  value={formData.fullName}
                  onChange={handleInputChange}
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-700 dark:text-white"
                  placeholder="Enter your full name"
                />
                {errors.fullName && (
                  <p className="mt-1 text-sm text-red-500">{errors.fullName}</p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Hall Ticket Number
                </label>
                <input
                  type="text"
                  name="hallTicketNo"
                  value={formData.hallTicketNo}
                  onChange={handleInputChange}
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-700 dark:text-white"
                  placeholder="Enter hall ticket number"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Date of Birth
                </label>
                <input
                  type="date"
                  name="dateOfBirth"
                  value={formData.dateOfBirth}
                  onChange={handleInputChange}
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-700 dark:text-white"
                />
              </div>
            </div>
          </div>

          {/* Academic Information Section */}
          <div className="border-b border-gray-200 dark:border-gray-700 pb-6">
            <h3 className="text-xl font-semibold mb-4 text-gray-900 dark:text-white">
              Academic Information
            </h3>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Inter Group & College
                </label>
                <input
                  type="text"
                  name="interGroupCollege"
                  value={formData.interGroupCollege}
                  onChange={handleInputChange}
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-700 dark:text-white"
                  placeholder="Enter your inter group and college"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Referred By
                </label>
                <input
                  type="text"
                  name="referredBy"
                  value={formData.referredBy}
                  onChange={handleInputChange}
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-700 dark:text-white"
                  placeholder="Who referred you?"
                />
              </div>
            </div>

            {/* Courses Selection */}
            <div className="mt-6">
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                Select Courses (Choose multiple)
              </label>
              <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-3">
                {availableCourses.map((course) => (
                  <label key={course} className="flex items-center space-x-2 cursor-pointer">
                    <input
                      type="checkbox"
                      checked={formData.courses.includes(course)}
                      onChange={() => handleCourseChange(course)}
                      className="w-4 h-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500"
                    />
                    <span className="text-sm text-gray-700 dark:text-gray-300">{course}</span>
                  </label>
                ))}
              </div>
            </div>
          </div>

          {/* Contact Information Section */}
          <div className="border-b border-gray-200 dark:border-gray-700 pb-6">
            <h3 className="text-xl font-semibold mb-4 text-gray-900 dark:text-white">
              Contact Information
            </h3>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Aadhar Number
                </label>
                <input
                  type="text"
                  name="aadharNo"
                  value={formData.aadharNo}
                  onChange={handleInputChange}
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-700 dark:text-white"
                  placeholder="12-digit Aadhar number"
                  maxLength="12"
                />
                {errors.aadharNo && (
                  <p className="mt-1 text-sm text-red-500">{errors.aadharNo}</p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Aadhar Mobile <span className="text-red-500">*</span>
                </label>
                <input
                  type="tel"
                  name="aadharMobile"
                  value={formData.aadharMobile}
                  onChange={handleInputChange}
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-700 dark:text-white"
                  placeholder="10-digit mobile number"
                  maxLength="10"
                />
                {errors.aadharMobile && (
                  <p className="mt-1 text-sm text-red-500">{errors.aadharMobile}</p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  WhatsApp Number
                </label>
                <input
                  type="tel"
                  name="whatsappNo"
                  value={formData.whatsappNo}
                  onChange={handleInputChange}
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-700 dark:text-white"
                  placeholder="10-digit WhatsApp number"
                  maxLength="10"
                />
                {errors.whatsappNo && (
                  <p className="mt-1 text-sm text-red-500">{errors.whatsappNo}</p>
                )}
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  Village
                </label>
                <input
                  type="text"
                  name="village"
                  value={formData.village}
                  onChange={handleInputChange}
                  className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-700 dark:text-white"
                  placeholder="Enter your village"
                />
              </div>
            </div>

            <div className="mt-6">
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                Address
              </label>
              <textarea
                name="address"
                value={formData.address}
                onChange={handleInputChange}
                rows="3"
                className="w-full px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent dark:bg-gray-700 dark:text-white"
                placeholder="Enter your complete address"
              />
            </div>
          </div>

          {/* Submit Button */}
          <div className="flex justify-center">
            <ButtonPrimary
              type="submit"
              disabled={isSubmitting}
              width="full"
            >
              {isSubmitting ? "Submitting..." : "Submit Enrollment"}
            </ButtonPrimary>
          </div>
        </form>
      </div>
    </div>
  );
};

export default EnrollmentForm;
