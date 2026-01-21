import api from "./api";

// ==================== Grades APIs ====================

/**
 * Calculate Grade for a Course
 */
export const calculateGrade = async (studentId, courseId, semesterId) => {
  try {
    const response = await api.post("grades/calculate", null, {
      params: { studentId, courseId, semesterId },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Calculate Grades for Entire Semester
 */
export const calculateSemesterGrades = async (studentId, semesterId) => {
  try {
    const response = await api.post("grades/calculate-semester", null, {
      params: { studentId, semesterId },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get All Grades by Student
 */
export const getGradesByStudent = async (studentId) => {
  try {
    const response = await api.get(`grades/student/${studentId}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Grades by Student and Semester
 */
export const getGradesByStudentAndSemester = async (studentId, semesterId) => {
  try {
    const response = await api.get(
      `grades/student/${studentId}/semester/${semesterId}`
    );
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Transcript
 */
export const getTranscript = async (studentId, semesterId) => {
  try {
    const response = await api.get(
      `grades/transcript/student/${studentId}/semester/${semesterId}`
    );
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get SGPA (Semester GPA)
 */
export const getSGPA = async (studentId, semesterId) => {
  try {
    const response = await api.get(
      `grades/sgpa/student/${studentId}/semester/${semesterId}`
    );
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get CGPA (Cumulative GPA)
 */
export const getCGPA = async (studentId) => {
  try {
    const response = await api.get(`grades/cgpa/student/${studentId}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Update Grade
 */
export const updateGrade = async (gradeId, gradeData) => {
  try {
    const response = await api.put(`grades/${gradeId}`, gradeData);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

