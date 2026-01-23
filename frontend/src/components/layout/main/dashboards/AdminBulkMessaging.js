"use client";

import { useEffect, useMemo, useState } from "react";
import {
  listMessageTemplates,
  listCampaigns,
  sendCampaignNow,
  getCampaignStats,
} from "@/services/messagingService";
import { getAllPrograms } from "@/services/academicService";

const CHANNELS = ["IN_APP", "EMAIL", "SMS", "WHATSAPP"];
const TARGETING_TYPES = ["ALL", "PROGRAM", "YEAR", "SELECTED"];

export default function AdminBulkMessaging() {
  const [templates, setTemplates] = useState([]);
  const [campaigns, setCampaigns] = useState([]);
  const [programs, setPrograms] = useState([]);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const [form, setForm] = useState({
    title: "",
    templateId: "",
    subject: "",
    body: "",
    targetingType: "ALL",
    joiningYear: "",
    programIds: [],
    userIds: "",
    channels: ["IN_APP"],
  });

  const createdBy = useMemo(() => {
    if (typeof window === "undefined") return "";
    return sessionStorage.getItem("userId") || "";
  }, []);

  const load = async () => {
    setLoading(true);
    setError("");
    try {
      const [t, c, p] = await Promise.all([
        listMessageTemplates(),
        listCampaigns(),
        getAllPrograms(),
      ]);
      setTemplates(Array.isArray(t) ? t : []);
      setCampaigns(Array.isArray(c) ? c : []);
      setPrograms(Array.isArray(p) ? p : []);
    } catch (e) {
      setError(e?.message || e?.status || "Failed to load messaging data");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    if (name === "channels") {
      const next = new Set(form.channels);
      if (checked) next.add(value);
      else next.delete(value);
      setForm((prev) => ({ ...prev, channels: Array.from(next) }));
      return;
    }

    if (name === "programIds") {
      const next = new Set(form.programIds);
      if (checked) next.add(value);
      else next.delete(value);
      setForm((prev) => ({ ...prev, programIds: Array.from(next) }));
      return;
    }

    setForm((prev) => ({ ...prev, [name]: type === "checkbox" ? checked : value }));
  };

  const buildPayload = () => {
    const targeting = {};
    if (form.targetingType === "YEAR") {
      targeting.joiningYear = Number.parseInt(form.joiningYear, 10);
    }
    if (form.targetingType === "PROGRAM") {
      targeting.programIds = form.programIds;
    }
    if (form.targetingType === "SELECTED") {
      const ids = (form.userIds || "")
        .split(",")
        .map((s) => s.trim())
        .filter(Boolean);
      targeting.userIds = ids;
    }

    return {
      title: form.title,
      templateId: form.templateId || null,
      subject: form.subject,
      body: form.body,
      targetingType: form.targetingType,
      targeting,
      channels: form.channels,
      scheduledAt: null,
    };
  };

  const handleSend = async () => {
    setError("");
    setSuccess("");

    if (!createdBy) {
      setError("Missing admin userId in session. Please login again.");
      return;
    }
    if (!form.title.trim()) {
      setError("Title is required");
      return;
    }
    if (!form.channels.length) {
      setError("Select at least one channel");
      return;
    }

    setLoading(true);
    try {
      const res = await sendCampaignNow(createdBy, buildPayload());
      setSuccess(`Campaign queued. Recipients: ${res?.recipientCount ?? 0}`);
      await load();
    } catch (e) {
      setError(e?.message || e?.status || "Failed to send campaign");
    } finally {
      setLoading(false);
    }
  };

  const viewStats = async (c) => {
    setError("");
    try {
      const s = await getCampaignStats(c.id);
      alert(
        `Status: ${s.status}\nRecipients: ${s.recipients}\nOutbox Pending: ${s.outboxPending}\nDone: ${s.outboxDone}\nFailed: ${s.outboxFailed}`
      );
    } catch (e) {
      setError(e?.message || e?.status || "Failed to fetch stats");
    }
  };

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold">Bulk Messaging</h1>
        <button
          onClick={load}
          className="px-4 py-2 bg-[#1f5a6c] hover:bg-[#174652] text-white rounded-lg"
          disabled={loading}
        >
          Refresh
        </button>
      </div>

      {error && (
        <div className="mb-4 p-4 bg-red-100 dark:bg-red-900 text-red-700 dark:text-red-300 rounded-lg">
          {error}
        </div>
      )}
      {success && (
        <div className="mb-4 p-4 bg-green-100 dark:bg-green-900 text-green-700 dark:text-green-300 rounded-lg">
          {success}
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-5">
          <h2 className="text-lg font-semibold mb-4">Compose</h2>

          <div className="grid grid-cols-1 gap-3">
            <input
              name="title"
              value={form.title}
              onChange={handleChange}
              placeholder="Title"
              className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
            />

            <select
              name="templateId"
              value={form.templateId}
              onChange={handleChange}
              className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
            >
              <option value="">Select Template (optional)</option>
              {templates.map((t) => (
                <option key={t.id} value={t.id}>
                  {t.name}
                </option>
              ))}
            </select>

            <input
              name="subject"
              value={form.subject}
              onChange={handleChange}
              placeholder="Subject (for In-App/Email)"
              className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
            />

            <textarea
              name="body"
              value={form.body}
              onChange={handleChange}
              placeholder="Message body (leave empty to use template body)"
              rows={4}
              className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
            />

            <div>
              <label className="block text-sm font-medium mb-1">Channels</label>
              <div className="flex gap-4 flex-wrap">
                {CHANNELS.map((ch) => (
                  <label key={ch} className="flex items-center gap-2">
                    <input
                      type="checkbox"
                      name="channels"
                      value={ch}
                      checked={form.channels.includes(ch)}
                      onChange={handleChange}
                    />
                    {ch}
                  </label>
                ))}
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium mb-1">Targeting</label>
              <select
                name="targetingType"
                value={form.targetingType}
                onChange={handleChange}
                className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
              >
                {TARGETING_TYPES.map((t) => (
                  <option key={t} value={t}>
                    {t}
                  </option>
                ))}
              </select>
            </div>

            {form.targetingType === "YEAR" && (
              <input
                name="joiningYear"
                value={form.joiningYear}
                onChange={handleChange}
                placeholder="Joining Year (e.g. 2024)"
                className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
              />
            )}

            {form.targetingType === "PROGRAM" && (
              <div className="border rounded p-3 dark:border-gray-600">
                <div className="text-sm font-medium mb-2">Programs</div>
                <div className="max-h-40 overflow-auto">
                  {programs.map((p) => (
                    <label key={p.id} className="flex items-center gap-2 mb-1">
                      <input
                        type="checkbox"
                        name="programIds"
                        value={p.id}
                        checked={form.programIds.includes(p.id)}
                        onChange={handleChange}
                      />
                      {p.name}
                    </label>
                  ))}
                </div>
              </div>
            )}

            {form.targetingType === "SELECTED" && (
              <input
                name="userIds"
                value={form.userIds}
                onChange={handleChange}
                placeholder="Student userIds comma separated"
                className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
              />
            )}

            <button
              onClick={handleSend}
              disabled={loading}
              className="px-4 py-2 bg-[#1f5a6c] hover:bg-[#174652] text-white rounded-lg disabled:opacity-50"
            >
              {loading ? "Sending..." : "Send Now"}
            </button>
          </div>
        </div>

        <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-5">
          <h2 className="text-lg font-semibold mb-4">History</h2>

          {loading ? (
            <div className="text-gray-500">Loading...</div>
          ) : campaigns.length === 0 ? (
            <div className="text-gray-500">No campaigns</div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="text-left border-b dark:border-gray-700">
                    <th className="py-2">Title</th>
                    <th>Status</th>
                    <th>Created</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  {campaigns
                    .slice()
                    .sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
                    .slice(0, 20)
                    .map((c) => (
                      <tr key={c.id} className="border-b dark:border-gray-700">
                        <td className="py-2">{c.title}</td>
                        <td>{c.status}</td>
                        <td>{c.createdAt ? new Date(c.createdAt).toLocaleString() : ""}</td>
                        <td className="text-right">
                          <button
                            onClick={() => viewStats(c)}
                            className="px-3 py-1 bg-gray-200 dark:bg-gray-700 rounded"
                          >
                            Stats
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
    </div>
  );
}
