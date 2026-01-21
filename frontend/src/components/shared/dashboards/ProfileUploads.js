"use client";

import { useEffect, useState } from "react";
import api from "@/services/api";
import {
  deleteUserDocument,
  getUserDocuments,
  uploadUserDocument,
  uploadUserProfilePhoto,
} from "@/services/fileService";

const DEFAULT_DOC_TYPES = [
  "AADHAR",
  "PAN",
  "PHOTO_ID",
  "TRANSFER_CERTIFICATE",
  "MARKSHEET",
  "OTHER",
];

const ProfileUploads = ({ userId, profilePhotoUrl, onPhotoUploaded }) => {
  const [docs, setDocs] = useState([]);
  const [docType, setDocType] = useState("AADHAR");
  const [photoUploading, setPhotoUploading] = useState(false);
  const [docUploading, setDocUploading] = useState(false);
  const [loadingDocs, setLoadingDocs] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const resolveUrl = (u) => {
    if (!u) return u;
    if (/^https?:\/\//i.test(u)) return u;
    try {
      const base = new URL(api.defaults.baseURL);
      return base.origin + u;
    } catch {
      return u;
    }
  };

  const loadDocs = async () => {
    if (!userId) return;
    setLoadingDocs(true);
    try {
      setError("");
      const data = await getUserDocuments(userId);
      setDocs(Array.isArray(data) ? data : []);
    } catch (e) {
      setDocs([]);
      setError(e?.message || "Failed to load documents");
    } finally {
      setLoadingDocs(false);
    }
  };

  useEffect(() => {
    loadDocs();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [userId]);

  const handleUploadPhoto = async (file) => {
    if (!userId || !file) return;
    setPhotoUploading(true);
    try {
      setError("");
      setSuccess("");
      const res = await uploadUserProfilePhoto(userId, file);
      onPhotoUploaded?.(resolveUrl(res?.url));
      setSuccess("Profile photo uploaded");
      setTimeout(() => setSuccess(""), 2500);
    } catch (e) {
      setError(e?.message || "Failed to upload profile photo");
    } finally {
      setPhotoUploading(false);
    }
  };

  const handleUploadDoc = async (file) => {
    if (!userId || !file) return;
    setDocUploading(true);
    try {
      setError("");
      setSuccess("");
      await uploadUserDocument(userId, docType, file);
      setSuccess("Document uploaded");
      setTimeout(() => setSuccess(""), 2500);
      await loadDocs();
    } catch (e) {
      setError(e?.message || "Failed to upload document");
    } finally {
      setDocUploading(false);
    }
  };

  const handleDelete = async (id) => {
    if (!id) return;
    try {
      setError("");
      await deleteUserDocument(id);
      await loadDocs();
    } catch (e) {
      setError(e?.message || "Failed to delete document");
    }
  };

  return (
    <div className="mt-6 p-4 border border-borderColor dark:border-borderColor-dark rounded">
      <h3 className="text-lg font-semibold text-blackColor dark:text-blackColor-dark mb-3">Photo & Documents</h3>

      {error ? <div className="text-red-600 mb-3">{error}</div> : null}
      {success ? <div className="text-green-600 mb-3">{success}</div> : null}

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <div className="text-sm font-semibold mb-2 text-blackColor dark:text-blackColor-dark">Profile Photo</div>
          <div className="flex items-center gap-4">
            <div className="w-20 h-20 rounded-full overflow-hidden border border-borderColor dark:border-borderColor-dark bg-gray-50 flex items-center justify-center">
              {profilePhotoUrl ? (
                // eslint-disable-next-line @next/next/no-img-element
                <img src={resolveUrl(profilePhotoUrl)} alt="profile" className="w-full h-full object-cover" />
              ) : (
                <div className="text-xs text-gray-500">No Photo</div>
              )}
            </div>
            <div>
              <input
                type="file"
                accept="image/*"
                disabled={photoUploading}
                onChange={(e) => handleUploadPhoto(e.target.files?.[0])}
                className="text-sm"
              />
              <div className="text-xs text-gray-500 mt-1">
                {photoUploading ? "Uploading..." : "JPG/PNG"}
              </div>
            </div>
          </div>
        </div>

        <div>
          <div className="text-sm font-semibold mb-2 text-blackColor dark:text-blackColor-dark">Upload Document</div>
          <div className="flex flex-col gap-2">
            <select
              value={docType}
              onChange={(e) => setDocType(e.target.value)}
              className="border border-borderColor dark:border-borderColor-dark rounded px-3 py-2 bg-transparent text-sm"
            >
              {DEFAULT_DOC_TYPES.map((t) => (
                <option key={t} value={t}>
                  {t}
                </option>
              ))}
            </select>
            <input
              type="file"
              disabled={docUploading}
              onChange={(e) => handleUploadDoc(e.target.files?.[0])}
              className="text-sm"
            />
            <div className="text-xs text-gray-500">
              {docUploading ? "Uploading..." : "PDF/JPG/PNG"}
            </div>
          </div>
        </div>
      </div>

      <div className="mt-6">
        <div className="text-sm font-semibold mb-2 text-blackColor dark:text-blackColor-dark">My Documents</div>
        {loadingDocs ? (
          <div className="text-sm">Loading...</div>
        ) : docs.length === 0 ? (
          <div className="text-sm text-gray-500">No documents uploaded.</div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-left border border-borderColor dark:border-borderColor-dark rounded">
              <thead className="bg-gray-50 dark:bg-whiteColor-dark">
                <tr className="border-b border-borderColor dark:border-borderColor-dark">
                  <th className="py-2 px-3 text-sm font-semibold">Type</th>
                  <th className="py-2 px-3 text-sm font-semibold">File</th>
                  <th className="py-2 px-3 text-sm font-semibold">Actions</th>
                </tr>
              </thead>
              <tbody>
                {docs.map((d, idx) => (
                  <tr
                    key={d.id || idx}
                    className={`border-b border-borderColor dark:border-borderColor-dark ${
                      idx % 2 === 0 ? "bg-whiteColor dark:bg-whiteColor-dark" : "bg-gray-50/50 dark:bg-whiteColor-dark"
                    }`}
                  >
                    <td className="py-2 px-3 text-sm">{d.documentType || "-"}</td>
                    <td className="py-2 px-3 text-sm">
                      {d.fileUrl ? (
                        <a className="text-blue-600 underline" href={resolveUrl(d.fileUrl)} target="_blank" rel="noreferrer">
                          {d.fileName || "View"}
                        </a>
                      ) : (
                        d.fileName || "-"
                      )}
                    </td>
                    <td className="py-2 px-3 text-sm">
                      <button
                        type="button"
                        onClick={() => handleDelete(d.id)}
                        className="text-red-600"
                      >
                        Delete
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default ProfileUploads;
