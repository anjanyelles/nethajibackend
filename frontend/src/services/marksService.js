import api from "./api";

// ==================== Marks APIs ====================

/**
 * Enter Marks
 */
export const enterMarks = async (marksData) => {
  try {
    const response = await api.post("marks/enter", marksData);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Enter Bulk Marks
 */
export const enterBulkMarks = async (bulkMarksData) => {
  try {
    const response = await api.post("marks/enter-bulk", bulkMarksData);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Marks by Student
 */
export const getMarksByStudent = async (studentId) => {
  try {
    const response = await api.get(`marks/student/${studentId}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Marks by Course
 */
export const getMarksByCourse = async (courseId) => {
  try {
    const response = await api.get(`marks/course/${courseId}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Marks by Student and Course
 */
export const getMarksByStudentAndCourse = async (studentId, courseId) => {
  try {
    const response = await api.get(
      `marks/student/${studentId}/course/${courseId}`
    );
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Update Marks
 */
export const updateMarks = async (marksId, marksData) => {
  try {
    const response = await api.put(`marks/${marksId}`, marksData);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Delete Marks
 */
export const deleteMarks = async (marksId) => {
  try {
    const response = await api.delete(`marks/${marksId}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

