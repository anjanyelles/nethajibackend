"use client";

import ProfileDetails from "@/components/shared/dashboards/ProfileDetails";
import ProfileUploads from "@/components/shared/dashboards/ProfileUploads";
import React, { useEffect, useState } from "react";
import { getStaffProfileById, saveStaffProfile } from "@/services/authService";

const InstructorProfileMain = () => {
  const [profile, setProfile] = useState(null);
  const [raw, setRaw] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const normalize = (data) =>
    data
      ? {
          firstName: data.firstName,
          lastName: data.lastName,
          email: data.email,
          mobileNumber: data.phoneNumber,
          userCreatedAt: data.createdAt,
          state: data.employeeSubject || data.designation,
          address: data.address,
          enrollmentNumber: data.email,
          profilePictureUrl: data.profilePicture,
        }
      : null;

  useEffect(() => {
    const load = async () => {
      try {
        const userId = typeof window !== "undefined" ? sessionStorage.getItem("userId") : null;
        if (!userId) {
          setProfile(null);
          return;
        }

        const data = await getStaffProfileById(userId);

        setRaw(data || null);
        setProfile(normalize(data));
      } catch (e) {
        setProfile(null);
        setRaw(null);
      } finally {
        setLoading(false);
      }
    };

    load();
  }, []);

  useEffect(() => {
    setProfile(normalize(raw));
  }, [raw]);

  const handleSave = async () => {
    const userId = typeof window !== "undefined" ? sessionStorage.getItem("userId") : null;
    if (!userId) return;

    setSaving(true);
    setError("");
    setSuccess("");
    try {
      const payload = {
        ...(raw || {}),
        userId,
        address: raw?.address || "",
      };
      const saved = await saveStaffProfile(payload);
      setRaw(saved || payload);
      setSuccess("Profile updated");
      setTimeout(() => setSuccess(""), 2500);
    } catch (e) {
      setError(e?.message || "Failed to save profile");
    } finally {
      setSaving(false);
    }
  };

  return (
    <>
      <ProfileDetails profile={profile} loading={loading} />

      <div className="p-10px md:px-10 md:py-50px mb-30px bg-whiteColor dark:bg-whiteColor-dark shadow-accordion dark:shadow-accordion-dark rounded-5">
        <div className="mb-6 pb-5 border-b-2 border-borderColor dark:border-borderColor-dark">
          <h2 className="text-2xl font-bold text-blackColor dark:text-blackColor-dark">Edit Address</h2>
        </div>

        {error ? <div className="text-red-600 mb-3">{error}</div> : null}
        {success ? <div className="text-green-600 mb-3">{success}</div> : null}

        <div>
          <label className="block text-sm font-medium mb-1">Address</label>
          <textarea
            value={raw?.address || ""}
            onChange={(e) => setRaw((p) => ({ ...(p || {}), address: e.target.value }))}
            rows={2}
            className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
          />
        </div>

        <div className="mt-4 flex justify-end">
          <button
            type="button"
            onClick={handleSave}
            disabled={saving}
            className="px-6 py-2 bg-[#1f5a6c] hover:bg-[#174652] text-white rounded-lg disabled:opacity-50"
          >
            {saving ? "Saving..." : "Save"}
          </button>
        </div>

        <ProfileUploads
          userId={typeof window !== "undefined" ? sessionStorage.getItem("userId") : null}
          profilePhotoUrl={raw?.profilePicture}
          onPhotoUploaded={(url) => {
            setRaw((p) => ({ ...(p || {}), profilePicture: url }));
          }}
        />
      </div>
    </>
  );
};

export default InstructorProfileMain;
