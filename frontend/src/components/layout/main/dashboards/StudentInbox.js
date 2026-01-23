"use client";

import { useEffect, useState } from "react";
import { listInbox, markInboxRead } from "@/services/messagingService";

export default function StudentInbox() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const userId = typeof window === "undefined" ? "" : sessionStorage.getItem("userId");

  const load = async () => {
    if (!userId) return;
    setLoading(true);
    setError("");
    try {
      const data = await listInbox(userId);
      setItems(Array.isArray(data) ? data : []);
    } catch (e) {
      setError(e?.message || e?.status || "Failed to load inbox");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, [userId]);

  const open = async (m) => {
    if (!m?.id) return;
    try {
      await markInboxRead(m.id);
      await load();
      alert(m.body || "");
    } catch (e) {
      setError(e?.message || e?.status || "Failed to open message");
    }
  };

  return (
    <div className="p-6">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold">Inbox</h1>
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

      {loading ? (
        <div className="text-gray-500">Loading...</div>
      ) : items.length === 0 ? (
        <div className="text-gray-500">No messages</div>
      ) : (
        <div className="overflow-x-auto shadow-lg rounded-lg">
          <table className="w-full text-sm text-left text-gray-700 dark:text-gray-300">
            <thead className="bg-gray-200 dark:bg-gray-800">
              <tr>
                <th className="px-6 py-3">Title</th>
                <th className="px-6 py-3">Date</th>
                <th className="px-6 py-3">Status</th>
                <th className="px-6 py-3"></th>
              </tr>
            </thead>
            <tbody>
              {items.map((m) => (
                <tr key={m.id} className="border-b dark:border-gray-700">
                  <td className="px-6 py-4 font-medium">{m.title || "Message"}</td>
                  <td className="px-6 py-4">
                    {m.createdAt ? new Date(m.createdAt).toLocaleString() : ""}
                  </td>
                  <td className="px-6 py-4">{m.readAt ? "READ" : "UNREAD"}</td>
                  <td className="px-6 py-4 text-right">
                    <button
                      onClick={() => open(m)}
                      className="px-3 py-1 bg-gray-200 dark:bg-gray-700 rounded"
                    >
                      Open
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
