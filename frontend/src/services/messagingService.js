import api from "./api";

export const createMessageTemplate = async (createdBy, payload) => {
  const response = await api.post("messaging/templates", payload, {
    params: { createdBy },
  });
  return response.data;
};

export const listMessageTemplates = async () => {
  const response = await api.get("messaging/templates");
  return response.data;
};

export const updateMessageTemplate = async (id, payload) => {
  const response = await api.put(`messaging/templates/${id}`, payload);
  return response.data;
};

export const deleteMessageTemplate = async (id) => {
  const response = await api.delete(`messaging/templates/${id}`);
  return response.data;
};

export const createCampaignDraft = async (createdBy, payload) => {
  const response = await api.post("messaging/campaigns", payload, {
    params: { createdBy },
  });
  return response.data;
};

export const sendCampaignNow = async (createdBy, payload) => {
  const response = await api.post("messaging/campaigns/send-now", payload, {
    params: { createdBy },
  });
  return response.data;
};

export const scheduleCampaign = async (campaignId, scheduledAtMs) => {
  const response = await api.post(`messaging/campaigns/${campaignId}/schedule`, null, {
    params: { scheduledAtMs },
  });
  return response.data;
};

export const enqueueCampaign = async (campaignId) => {
  const response = await api.post(`messaging/campaigns/${campaignId}/enqueue`);
  return response.data;
};

export const cancelCampaign = async (campaignId) => {
  const response = await api.post(`messaging/campaigns/${campaignId}/cancel`);
  return response.data;
};

export const listCampaigns = async () => {
  const response = await api.get("messaging/campaigns");
  return response.data;
};

export const getCampaign = async (campaignId) => {
  const response = await api.get(`messaging/campaigns/${campaignId}`);
  return response.data;
};

export const getCampaignRecipients = async (campaignId) => {
  const response = await api.get(`messaging/campaigns/${campaignId}/recipients`);
  return response.data;
};

export const getCampaignStats = async (campaignId) => {
  const response = await api.get(`messaging/campaigns/${campaignId}/stats`);
  return response.data;
};

export const listInbox = async (userId) => {
  const response = await api.get("inbox", { params: { userId } });
  return response.data;
};

export const inboxUnreadCount = async (userId) => {
  const response = await api.get("inbox/unread-count", { params: { userId } });
  return response.data;
};

export const markInboxRead = async (id) => {
  const response = await api.post(`inbox/${id}/read`);
  return response.data;
};
