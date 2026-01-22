import api from "./api";

// ==================== Authentication APIs ====================

/**
 * User Registration
 */
export const registerUser = async (userData) => {
  try {
    const response = await api.post("auth/registration", userData);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Login with Email and Password
 */
export const loginWithEmailPassword = async (email, password) => {
  try {
    const response = await api.post("auth/userLoginWithEmailPassword", {
      email,
      password,
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Student Login (Email or Enrollment)
 */
export const studentLogin = async (emailOrEnrollment, password) => {
  try {
    const response = await api.post("auth/student-login", {
      emailOrEnrollment,
      password,
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Change Password
 */
export const changePassword = async (email, oldPassword, newPassword) => {
  try {
    const response = await api.post("auth/change-password", {
      email,
      oldPassword,
      newPassword,
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Create User (Admin/Principal)
 */
export const createUser = async (userData) => {
  try {
    const response = await api.post(
      "auth/admin-or-principal/create-user",
      userData
    );
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get All Students (Paginated)
 */
export const getAllStudents = async (page = 0, size = 10, sortBy = "firstName", sortDirection = "ASC") => {
  try {
    const response = await api.get("auth/students", {
      params: { page, size, sortBy, sortDirection },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get All Students (Simple)
 */
export const getAllStudentsSimple = async () => {
  try {
    const response = await api.get("auth/all-students");
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get All Lecturers
 */
export const getAllLecturers = async () => {
  try {
    const response = await api.get("auth/lecturers");
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Active Students by Program and Semester
 */
export const getActiveStudentsByProgramSemester = async (programId, semester) => {
  try {
    const response = await api.get(
      `auth/active-students-semester-programmed?programId=${programId}&semester=${semester}`
    );
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Students by Branch and Semester
 */
export const getStudentsByBranchSemester = async (programId, semester) => {
  try {
    const response = await api.get(
      `auth/getAllStudentsInfoNew/${programId}/${semester}`
    );
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Update Student Program
 */
export const updateStudentProgram = async (studentId, programId) => {
  try {
    const response = await api.patch(
      `auth/${studentId}/program/${programId}`
    );
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Update Password by Admin
 */
export const updatePasswordByAdmin = async (studentId, password) => {
  try {
    const response = await api.patch("auth/update-password-by-admin", null, {
      params: { studentId, password },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Activate/Deactivate Student
 */
export const toggleStudentStatus = async (studentId, status) => {
  try {
    const response = await api.patch("auth/active-inactive-student", null, {
      params: { studentId, status },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get All Lectures
 */
export const getAllLectures = async () => {
  try {
    const response = await api.get("auth/all-lectures");
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Lecturers and Assistants by Course
 */
export const getLecturersAndAssistantsByCourse = async () => {
  try {
    const response = await api.get("auth/lecturers-and-assistants-courses");
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Save/Update Student Profile
 */
export const saveStudentProfile = async (profileData) => {
  try {
    const response = await api.post("auth/saveuserprofile", profileData);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Student Profile by ID
 */
export const getStudentProfileById = async (studentId) => {
  try {
    const response = await api.get(`auth/getuserprofilebyid/${studentId}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Students by Section and Semester
 */
export const getStudentsBySectionSemester = async (section, semester) => {
  try {
    const response = await api.get(
      `auth/getSectionAndSemesterStudents/${section}/${semester}`
    );
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get All Staff Profiles
 */
export const getAllStaffProfiles = async () => {
  try {
    const response = await api.get("auth/getListofStaffDetails");
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Activate/Deactivate Staff Login
 */
export const toggleStaffStatus = async (staffId, status) => {
  try {
    const response = await api.patch("auth/active-inactive-staff", null, {
      params: { staffId, status },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Save/Update Staff Profile
 */
export const saveStaffProfile = async (staffProfileData) => {
  try {
    const response = await api.post("auth/saveStaffProfile", staffProfileData);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Staff Profile by ID
 */
export const getStaffProfileById = async (staffId) => {
  try {
    const response = await api.get(`auth/getStaffProfileById/${staffId}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Deactivate Staff
 */
export const deactivateStaff = async (userId) => {
  try {
    const response = await api.patch(`auth/staff/${userId}/deactivate`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};
