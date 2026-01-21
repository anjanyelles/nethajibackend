import api from "./api";

// ==================== Academic Structure APIs ====================

/**
 * Create/Update Department
 */
export const createOrUpdateDepartment = async (departmentData) => {
  try {
    const response = await api.post("acadamic/adddepartments", departmentData);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get All Departments
 */
export const getAllDepartments = async (id = null) => {
  try {
    const params = id ? { id } : {};
    const response = await api.get("acadamic/getalldepartments", { params });
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Create/Update Program
 */
export const createOrUpdateProgram = async (programData) => {
  try {
    const response = await api.post("acadamic/addprograms", programData);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get All Programs
 */
export const getAllPrograms = async (id = null) => {
  try {
    const params = id ? { id } : {};
    const response = await api.get("acadamic/getallprograms", { params });
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Add/Update Semesters
 */
export const addOrUpdateSemesters = async (semesterData) => {
  try {
    const response = await api.post("acadamic/addSemesters", semesterData);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Create/Update Course
 */
export const createOrUpdateCourse = async (courseData) => {
  try {
    const response = await api.post("acadamic/addcourses", courseData);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get All Courses
 */
export const getAllCourses = async (id = null) => {
  try {
    const params = id ? { id } : {};
    const response = await api.get("acadamic/all-courses", { params });
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Program Hierarchy
 */
export const getProgramHierarchy = async () => {
  try {
    const response = await api.get("acadamic/all-courses-info");
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Program Details by Level
 */
export const getProgramDetailsByLevel = async (programLevel) => {
  try {
    const response = await api.get("acadamic/program/details", {
      params: { programLevel },
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

/**
 * Get Department and Semester Subjects Info
 */
export const getDepartmentSemesterSubjectsInfo = async (
  departmentType,
  semester
) => {
  try {
    const response = await api.get(
      "acadamic/getDepartMentAndSemesterSubjectsInfo",
      {
        params: { departMentType: departmentType, semester },
      }
    );
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

