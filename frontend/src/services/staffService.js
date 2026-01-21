import api from "./api";

export const getLecturerCourses = async (lecturerId) => {
  try {
    const response = await api.get(`staff/lecturer/${lecturerId}/courses`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

export const getLecturerCourseRoster = async (lecturerId, courseId) => {
  try {
    const response = await api.get(`staff/lecturer/${lecturerId}/courses/${courseId}/students`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

export const markLecturerBulkAttendance = async (lecturerId, courseId, bulkAttendanceData) => {
  try {
    const response = await api.post(
      `staff/lecturer/${lecturerId}/courses/${courseId}/attendance/mark-bulk`,
      bulkAttendanceData
    );
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

export const enterLecturerBulkMarks = async (lecturerId, courseId, bulkMarksData) => {
  try {
    const response = await api.post(
      `staff/lecturer/${lecturerId}/courses/${courseId}/marks/enter-bulk`,
      bulkMarksData
    );
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

export const calculateLecturerGradesForCourse = async (lecturerId, courseId) => {
  try {
    const response = await api.post(`staff/lecturer/${lecturerId}/courses/${courseId}/grades/calculate`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};
