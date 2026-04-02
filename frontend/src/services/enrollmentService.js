// services/enrollmentService.js
import api from "./api";

const enrollmentService = {
  /**
   * Submit student enrollment data
   * @param {Object} enrollmentData - Enrollment form data
   * @returns {Promise} - API response
   */
  submitEnrollment: async (enrollmentData) => {
    try {
      const response = await api.post("../enrollment", enrollmentData);
      return {
        success: true,
        data: response.data,
        message: "Enrollment submitted successfully!"
      };
    } catch (error) {
      // Handle different types of errors
      let errorMessage = "Failed to submit enrollment";
      
      if (error.response) {
        // Server responded with error status
        const status = error.response.status;
        const errorData = error.response.data;
        
        if (status === 400) {
          errorMessage = errorData?.message || "Invalid enrollment data";
        } else if (status === 409) {
          errorMessage = errorData?.message || "Enrollment already exists";
        } else if (status === 500) {
          errorMessage = "Server error. Please try again later.";
        } else {
          errorMessage = errorData?.message || `Error ${status}: Failed to submit enrollment`;
        }
      } else if (error.request) {
        // Network error
        errorMessage = "Network error. Please check your connection and try again.";
      } else {
        // Other error
        errorMessage = error.message || "An unexpected error occurred";
      }
      
      return {
        success: false,
        error: error,
        message: errorMessage
      };
    }
  },

  /**
   * Get all enrollments with pagination
   * @param {Object} params - Query parameters (page, size, sort)
   * @returns {Promise} - API response
   */
  getAllEnrollments: async (params = {}) => {
    try {
      const response = await api.get("../enrollment", { params });
      return {
        success: true,
        data: response.data,
        message: "Enrollments retrieved successfully"
      };
    } catch (error) {
      return {
        success: false,
        error: error,
        message: "Failed to retrieve enrollments"
      };
    }
  },

  /**
   * Get enrollment by ID
   * @param {string} id - Enrollment ID
   * @returns {Promise} - API response
   */
  getEnrollmentById: async (id) => {
    try {
      const response = await api.get(`../enrollment/${id}`);
      return {
        success: true,
        data: response.data,
        message: "Enrollment retrieved successfully"
      };
    } catch (error) {
      if (error.response?.status === 404) {
        return {
          success: false,
          error: error,
          message: "Enrollment not found"
        };
      }
      return {
        success: false,
        error: error,
        message: "Failed to retrieve enrollment"
      };
    }
  },

  /**
   * Search enrollments with filters
   * @param {Object} params - Search parameters
   * @returns {Promise} - API response
   */
  searchEnrollments: async (params = {}) => {
    try {
      const response = await api.get("../enrollment/search", { params });
      return {
        success: true,
        data: response.data,
        message: "Enrollments searched successfully"
      };
    } catch (error) {
      return {
        success: false,
        error: error,
        message: "Failed to search enrollments"
      };
    }
  },

  /**
   * Delete enrollment
   * @param {string} id - Enrollment ID
   * @returns {Promise} - API response
   */
  deleteEnrollment: async (id) => {
    try {
      await api.delete(`../enrollment/${id}`);
      return {
        success: true,
        message: "Enrollment deleted successfully"
      };
    } catch (error) {
      if (error.response?.status === 404) {
        return {
          success: false,
          error: error,
          message: "Enrollment not found"
        };
      }
      return {
        success: false,
        error: error,
        message: "Failed to delete enrollment"
      };
    }
  },

  /**
   * Update enrollment
   * @param {string} id - Enrollment ID
   * @param {Object} enrollmentData - Updated enrollment data
   * @returns {Promise} - API response
   */
  updateEnrollment: async (id, enrollmentData) => {
    try {
      const response = await api.put(`../enrollment/${id}`, enrollmentData);
      return {
        success: true,
        data: response.data,
        message: "Enrollment updated successfully"
      };
    } catch (error) {
      if (error.response?.status === 404) {
        return {
          success: false,
          error: error,
          message: "Enrollment not found"
        };
      }
      return {
        success: false,
        error: error,
        message: "Failed to update enrollment"
      };
    }
  }
};

export default enrollmentService;
