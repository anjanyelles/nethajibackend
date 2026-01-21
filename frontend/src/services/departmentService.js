// Legacy departmentService - kept for backward compatibility
// New code should use academicService.js and authService.js instead

import api from "./api";

export const getAllPrograms = async () => {
  try {
    const response = await api.get("acadamic/getallprograms");
    return response.data;
  } catch (error) {
    if (error.response) {
      return error.response;
    }
    throw error;
  }
};

export const getAllDepartments = async () => {
  const response = await api.get("acadamic/getalldepartments");
  return response.data;
};

export const updateSemester = async (data) => {
  try {
    const response = await api.post("acadamic/addSemesters", data);
    return response;
  } catch (error) {
    if (error.response) {
      return error.response;
    }
    throw error;
  }
};

export const getAllStudents = async (id, semester) => {
  const response = await api.get(`auth/getAllStudentsInfoNew/${id}/${semester}`);
  return response.data;
};

export const toggleStudentLoginStatus = async (studentId, status) => {
  const response = await api.patch(
    `auth/active-inactive-student`,
    null,
    {
      params: { studentId, status }
    }
  );
  return response;
};

export const createStudentByAdmin = async (payload) => {
  try {
    const response = await api.post(
      "auth/admin-or-principal/create-user",
      payload,
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    return response;
  } catch (error) {
    throw error;
  }
};

export const updatePasswordByAdmin = async (studentId, password) => {
  try {
    const response = await api.patch("/auth/update-password-by-admin", null, {
      params: {
        studentId,
        password,
      },
    });
    return response;
  } catch (error) {
    throw error;
  }
};


























