import api from "./api";

// ==================== Attendance APIs ====================

/**
 * Mark Individual Attendance
 */
export const markAttendance = async (attendanceData) => {
  try {
    const response = await api.post("attendance/mark", attendanceData);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Mark Bulk Attendance
 */
export const markBulkAttendance = async (bulkAttendanceData) => {
  try {
    const response = await api.post("attendance/mark-bulk", bulkAttendanceData);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Attendance by Student
 */
export const getAttendanceByStudent = async (studentId) => {
  try {
    const response = await api.get(`attendance/student/${studentId}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Attendance by Course
 */
export const getAttendanceByCourse = async (courseId) => {
  try {
    const response = await api.get(`attendance/course/${courseId}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Attendance by Student and Course
 */
export const getAttendanceByStudentAndCourse = async (studentId, courseId) => {
  try {
    const response = await api.get(
      `attendance/student/${studentId}/course/${courseId}`
    );
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Attendance by Date Range
 */
export const getAttendanceByDateRange = async (
  studentId,
  courseId,
  startDate,
  endDate
) => {
  try {
    const response = await api.get(
      `attendance/student/${studentId}/course/${courseId}/date-range`,
      {
        params: { startDate, endDate },
      }
    );
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Attendance Report (Student + Course)
 */
export const getAttendanceReport = async (studentId, courseId) => {
  try {
    const response = await api.get(
      `attendance/report/student/${studentId}/course/${courseId}`
    );
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Attendance Report by Course
 */
export const getAttendanceReportByCourse = async (courseId) => {
  try {
    const response = await api.get(`attendance/report/course/${courseId}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

export const getAttendanceViewByCourseAndDate = async (courseId, date, status, search) => {
  try {
    const response = await api.get(`attendance/view/course/${courseId}`, {
      params: { date, status, search },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

export const getCourseAttendanceReportByDateRange = async (
  courseId,
  startDate,
  endDate,
  lowAttendanceThreshold
) => {
  try {
    const response = await api.get(`attendance/report/course/${courseId}/date-range`, {
      params: { startDate, endDate, lowAttendanceThreshold },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Update Attendance
 */
export const updateAttendance = async (attendanceId, attendanceData) => {
  try {
    const response = await api.put(
      `attendance/${attendanceId}`,
      attendanceData
    );
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Delete Attendance
 */
export const deleteAttendance = async (attendanceId) => {
  try {
    const response = await api.delete(`attendance/${attendanceId}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

