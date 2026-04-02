"use client";
import React, { useState, useEffect } from "react";
import ButtonPrimary from "../buttons/ButtonPrimary";
import enrollmentService from "@/services/enrollmentService";

const EnrollmentDetailsModal = ({ enrollmentId, isOpen, onClose }) => {
  const [enrollment, setEnrollment] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // Fetch enrollment details
  useEffect(() => {
    if (!isOpen || !enrollmentId) return;

    const fetchEnrollmentDetails = async () => {
      setLoading(true);
      setError("");

      try {
        const result = await enrollmentService.getEnrollmentById(enrollmentId);
        
        if (result.success) {
          setEnrollment(result.data);
        } else {
          setError(result.message);
          setEnrollment(null);
        }
      } catch (error) {
        setError("Failed to fetch enrollment details");
        setEnrollment(null);
      } finally {
        setLoading(false);
      }
    };

    fetchEnrollmentDetails();
  }, [isOpen, enrollmentId]);

  // Format date
  const formatDate = (dateString) => {
    if (!dateString) return "N/A";
    return new Date(dateString).toLocaleDateString();
  };

  // Format courses for display
  const formatCourses = (coursesString) => {
    if (!coursesString) return "N/A";
    const courses = coursesString.split(",");
    return (
      <div className="flex flex-wrap gap-2">
        {courses.map((course, index) => (
          <span
            key={index}
            className="px-2 py-1 bg-blue-100 dark:bg-blue-900 text-blue-800 dark:text-blue-200 text-xs rounded-full"
          >
            {course.trim()}
          </span>
        ))}
      </div>
    );
  };

  // Handle delete enrollment
  const handleDelete = async () => {
    if (!window.confirm("Are you sure you want to delete this enrollment?")) {
      return;
    }

    try {
      const result = await enrollmentService.deleteEnrollment(enrollmentId);
      
      if (result.success) {
        alert("Enrollment deleted successfully");
        onClose();
      } else {
        alert(result.message || "Failed to delete enrollment");
      }
    } catch (error) {
      alert("Failed to delete enrollment");
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white dark:bg-gray-800 rounded-lg shadow-xl max-w-4xl w-full max-h-[90vh] overflow-y-auto">
        {/* Modal Header */}
        <div className="sticky top-0 bg-white dark:bg-gray-800 border-b border-gray-200 dark:border-gray-700 p-6">
          <div className="flex justify-between items-center">
            <h3 className="text-2xl font-bold text-gray-900 dark:text-white">
              Enrollment Details
            </h3>
            <button
              onClick={onClose}
              className="text-gray-400 hover:text-gray-600 dark:hover:text-gray-300 text-2xl font-bold"
            >
              ×
            </button>
          </div>
        </div>

        {/* Modal Body */}
        <div className="p-6">
          {loading ? (
            <div className="flex justify-center items-center py-12">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
            </div>
          ) : error ? (
            <div className="text-center py-12">
              <div className="text-red-600 dark:text-red-400 mb-4">{error}</div>
              <ButtonPrimary onClick={onClose}>Close</ButtonPrimary>
            </div>
          ) : enrollment ? (
            <div className="space-y-6">
              {/* Personal Information */}
              <div>
                <h4 className="text-lg font-semibold text-gray-900 dark:text-white mb-4 pb-2 border-b border-gray-200 dark:border-gray-700">
                  Personal Information
                </h4>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-600 dark:text-gray-400 mb-1">
                      Full Name
                    </label>
                    <p className="text-gray-900 dark:text-white">{enrollment.fullName || "N/A"}</p>
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-600 dark:text-gray-400 mb-1">
                      Email
                    </label>
                    <p className="text-gray-900 dark:text-white">{enrollment.email || "N/A"}</p>
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-600 dark:text-gray-400 mb-1">
                      Hall Ticket Number
                    </label>
                    <p className="text-gray-900 dark:text-white">{enrollment.hallTicketNo || "N/A"}</p>
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-600 dark:text-gray-400 mb-1">
                      Date of Birth
                    </label>
                    <p className="text-gray-900 dark:text-white">{formatDate(enrollment.dateOfBirth)}</p>
                  </div>
                </div>
              </div>

              {/* Academic Information */}
              <div>
                <h4 className="text-lg font-semibold text-gray-900 dark:text-white mb-4 pb-2 border-b border-gray-200 dark:border-gray-700">
                  Academic Information
                </h4>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-600 dark:text-gray-400 mb-1">
                      Inter Group & College
                    </label>
                    <p className="text-gray-900 dark:text-white">{enrollment.interGroupCollege || "N/A"}</p>
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-600 dark:text-gray-400 mb-1">
                      Courses
                    </label>
                    {formatCourses(enrollment.courses)}
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-600 dark:text-gray-400 mb-1">
                      Referred By
                    </label>
                    <p className="text-gray-900 dark:text-white">{enrollment.referredBy || "N/A"}</p>
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-600 dark:text-gray-400 mb-1">
                      Enrollment Date
                    </label>
                    <p className="text-gray-900 dark:text-white">{formatDate(enrollment.createdAt)}</p>
                  </div>
                </div>
              </div>

              {/* Contact Information */}
              <div>
                <h4 className="text-lg font-semibold text-gray-900 dark:text-white mb-4 pb-2 border-b border-gray-200 dark:border-gray-700">
                  Contact Information
                </h4>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-600 dark:text-gray-400 mb-1">
                      Aadhar Number
                    </label>
                    <p className="text-gray-900 dark:text-white">{enrollment.aadharNo || "N/A"}</p>
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-600 dark:text-gray-400 mb-1">
                      Aadhar Mobile
                    </label>
                    <p className="text-gray-900 dark:text-white">{enrollment.aadharMobile || "N/A"}</p>
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-600 dark:text-gray-400 mb-1">
                      WhatsApp Number
                    </label>
                    <p className="text-gray-900 dark:text-white">{enrollment.whatsappNo || "N/A"}</p>
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-600 dark:text-gray-400 mb-1">
                      Village
                    </label>
                    <p className="text-gray-900 dark:text-white">{enrollment.village || "N/A"}</p>
                  </div>
                  <div className="md:col-span-2">
                    <label className="block text-sm font-medium text-gray-600 dark:text-gray-400 mb-1">
                      Address
                    </label>
                    <p className="text-gray-900 dark:text-white whitespace-pre-wrap">
                      {enrollment.address || "N/A"}
                    </p>
                  </div>
                </div>
              </div>

              {/* System Information */}
              <div>
                <h4 className="text-lg font-semibold text-gray-900 dark:text-white mb-4 pb-2 border-b border-gray-200 dark:border-gray-700">
                  System Information
                </h4>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-600 dark:text-gray-400 mb-1">
                      Enrollment ID
                    </label>
                    <p className="text-gray-900 dark:text-white font-mono text-sm">{enrollment.id}</p>
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-600 dark:text-gray-400 mb-1">
                      Last Updated
                    </label>
                    <p className="text-gray-900 dark:text-white">{formatDate(enrollment.updatedAt)}</p>
                  </div>
                </div>
              </div>
            </div>
          ) : null}
        </div>

        {/* Modal Footer */}
        <div className="sticky bottom-0 bg-white dark:bg-gray-800 border-t border-gray-200 dark:border-gray-700 p-6">
          <div className="flex justify-between gap-4">
            <div>
              <ButtonPrimary
                type="button"
                onClick={handleDelete}
                className="bg-red-600 hover:bg-red-700 text-white"
              >
                Delete Enrollment
              </ButtonPrimary>
            </div>
            <div className="flex gap-4">
              <ButtonPrimary
                type="button"
                onClick={onClose}
                className="bg-gray-600 hover:bg-gray-700 text-white"
              >
                Close
              </ButtonPrimary>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EnrollmentDetailsModal;
