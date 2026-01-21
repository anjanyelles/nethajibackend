import api from "./api";

export const uploadUserProfilePhoto = async (userId, file) => {
  try {
    const formData = new FormData();
    formData.append("file", file);

    const response = await api.post(`files/users/${userId}/profile-photo`, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });

    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

export const uploadUserDocument = async (userId, documentType, file) => {
  try {
    const formData = new FormData();
    formData.append("documentType", documentType);
    formData.append("file", file);

    const response = await api.post(`files/users/${userId}/documents`, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });

    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

export const getUserDocuments = async (userId) => {
  try {
    const response = await api.get(`files/users/${userId}/documents`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

export const deleteUserDocument = async (documentId) => {
  try {
    const response = await api.delete(`files/documents/${documentId}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};
