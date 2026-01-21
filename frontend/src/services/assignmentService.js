import api from "./api";

// ==================== Assignment APIs ====================

/**
 * Create Assignment
 */
export const createAssignment = async (assignmentData) => {
  try {
    const response = await api.post("assignments/create", assignmentData);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Update Assignment
 */
export const updateAssignment = async (assignmentId, assignmentData) => {
  try {
    const response = await api.put(`assignments/${assignmentId}`, assignmentData);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Assignments by Course
 */
export const getAssignmentsByCourse = async (courseId) => {
  try {
    const response = await api.get(`assignments/course/${courseId}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Assignment by ID
 */
export const getAssignmentById = async (assignmentId) => {
  try {
    const response = await api.get(`assignments/${assignmentId}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Delete Assignment (Soft Delete)
 */
export const deleteAssignment = async (assignmentId) => {
  try {
    const response = await api.delete(`assignments/${assignmentId}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Submit Assignment
 */
export const submitAssignment = async (assignmentId, studentId, fileUrl) => {
  try {
    const response = await api.post(
      `assignments/${assignmentId}/submit/student/${studentId}`,
      { fileUrl }
    );
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Grade Assignment
 */
export const gradeAssignment = async (submissionId, gradeData) => {
  try {
    const response = await api.put(
      `assignments/grade/${submissionId}`,
      gradeData
    );
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Student Assignments
 */
export const getStudentAssignments = async (studentId) => {
  try {
    const response = await api.get(`assignments/student/${studentId}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Submissions for Assignment
 */
export const getAssignmentSubmissions = async (assignmentId) => {
  try {
    const response = await api.get(`assignments/${assignmentId}/submissions`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Specific Student Assignment
 */
export const getStudentAssignment = async (assignmentId, studentId) => {
  try {
    const response = await api.get(
      `assignments/${assignmentId}/student/${studentId}`
    );
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

