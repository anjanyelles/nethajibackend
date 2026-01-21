"use client";

import ProfileDetails from "@/components/shared/dashboards/ProfileDetails";
import ProfileUploads from "@/components/shared/dashboards/ProfileUploads";
import React, { useEffect, useState } from "react";
import { getStudentProfileById, saveStudentProfile } from "@/services/authService";

const StudentProfileMain = () => {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  useEffect(() => {
    const load = async () => {
      try {
        const userId = typeof window !== "undefined" ? sessionStorage.getItem("userId") : null;
        if (!userId) {
          setProfile(null);
          return;
        }

        const data = await getStudentProfileById(userId);
        setProfile(data || null);
      } catch (e) {
        setProfile(null);
      } finally {
        setLoading(false);
      }
    };

    load();
  }, []);

  const handleSave = async () => {
    const userId = typeof window !== "undefined" ? sessionStorage.getItem("userId") : null;
    if (!userId) return;

    setSaving(true);
    setError("");
    setSuccess("");
    try {
      const payload = {
        ...(profile || {}),
        userId,
      };
      const saved = await saveStudentProfile(payload);
      setProfile(saved || payload);
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

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-sm font-medium mb-1">Address</label>
            <textarea
              value={profile?.address || ""}
              onChange={(e) => setProfile((p) => ({ ...(p || {}), address: e.target.value }))}
              rows={2}
              className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
            />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">City</label>
            <input
              value={profile?.city || ""}
              onChange={(e) => setProfile((p) => ({ ...(p || {}), city: e.target.value }))}
              className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
            />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">State</label>
            <input
              value={profile?.state || ""}
              onChange={(e) => setProfile((p) => ({ ...(p || {}), state: e.target.value }))}
              className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
            />
          </div>
          <div>
            <label className="block text-sm font-medium mb-1">Pincode</label>
            <input
              value={profile?.pincode || ""}
              onChange={(e) => setProfile((p) => ({ ...(p || {}), pincode: e.target.value }))}
              className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
            />
          </div>
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
          profilePhotoUrl={profile?.profilePictureUrl}
          onPhotoUploaded={(url) => setProfile((p) => ({ ...(p || {}), profilePictureUrl: url }))}
        />
      </div>
    </>
  );
};

export default StudentProfileMain;
