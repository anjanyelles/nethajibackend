// services/api.js
import axios from "axios";

// Determine environment - can be overridden by NEXT_PUBLIC_API_ENV
const environment = process.env.NEXT_PUBLIC_API_ENV || "dev";

const API_BASE_URL_OVERRIDE = process.env.NEXT_PUBLIC_API_BASE_URL;

const BASE_URL =
  API_BASE_URL_OVERRIDE ||
  (environment === "prod"
    ? "https://api.nethajidcs.com/api/nethaji-service/"
    : "http://localhost:9029/api/nethaji-service/");

const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// Get token from sessionStorage (preferred) or localStorage
const getToken = () => {
  if (typeof window === "undefined") return null;
  
  // Try sessionStorage first, then localStorage
  return (
    sessionStorage.getItem("accessToken") ||
    localStorage.getItem("accessToken") ||
    null
  );
};

// Request interceptor - add auth token
api.interceptors.request.use(
  (config) => {
    const token = getToken();
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor - handle errors globally
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // Handle 401 Unauthorized - redirect to login
    if (error.response?.status === 401) {
      if (typeof window !== "undefined") {
        sessionStorage.clear();
        localStorage.clear();
        window.location.href = "/login";
      }
    }
    return Promise.reject(error);
  }
);

export default api;