"use client";

import React, { useState, useEffect } from "react";

const AdminProfileDetails = () => {
  const [programs, setPrograms] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  // modal/form state
  const [showModal, setShowModal] = useState(false);
  const [selectedProgram, setSelectedProgram] = useState(null);
  const [deptCode, setDeptCode] = useState("");
  const [deptName, setDeptName] = useState("");
  const [submitLoading, setSubmitLoading] = useState(false);
  const [submitMessage, setSubmitMessage] = useState("");

  useEffect(() => {
    const controller = new AbortController();
    const signal = controller.signal;

    const fetchPrograms = async () => {
      setLoading(true);
      setError("");
      try {
        const base = process.env.NEXT_PUBLIC_API_BASE || "http://13.200.240.128:9029";
        const res = await fetch(
          `${base}/api/nethaji-service/acadamic/getallprograms`,
          { method: "GET", signal }
        );

        if (!res.ok) {
          const text = await res.text().catch(() => "");
          throw new Error(`Fetch failed: ${res.status} ${res.statusText} ${text}`);
        }

        const data = await res.json();
        setPrograms(Array.isArray(data) ? data : []);
      } catch (err) {
        if (err.name === "AbortError") return;
        console.error("Fetch error:", err);
        setError("Failed to load programs. Please try again.");
      } finally {
        setLoading(false);
      }
    };

    fetchPrograms();
    return () => controller.abort();
  }, []);

  const openAddDept = (program) => {
    setSelectedProgram(program);
    setDeptCode("");
    setDeptName("");
    setSubmitMessage("");
    setShowModal(true);
  };

  const closeModal = () => {
    setShowModal(false);
    setSelectedProgram(null);
    setSubmitMessage("");
  };

  const handleAddDepartment = async (e) => {
    e.preventDefault();
    if (!deptCode.trim() || !deptName.trim()) {
      setSubmitMessage("Please enter department code and name.");
      return;
    }

    setSubmitLoading(true);
    setSubmitMessage("");

    try {
      const base = process.env.NEXT_PUBLIC_API_BASE || "http://13.200.240.128:9029";
      const res = await fetch(`${base}/api/nethaji-service/acadamic/adddepartments`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          departmentCode: deptCode.trim(),
          departmentName: deptName.trim(),
          programId: selectedProgram?.id || null,
        }),
      });

      const data = await res.json().catch(() => ({}));

      if (!res.ok) {
        const errMsg = data?.message || `Request failed: ${res.status}`;
        setSubmitMessage(errMsg);
      } else {
        // success
        const msg = data?.message || "Department created successfully.";
        setSubmitMessage(msg);

        // optionally close modal after a short delay
        setTimeout(() => {
          closeModal();
        }, 900);
      }
    } catch (err) {
      console.error("Add department error:", err);
      setSubmitMessage("Network error. Please try again.");
    } finally {
      setSubmitLoading(false);
    }
  };

  return (
    <div className="p-6 bg-white dark:bg-gray-900 min-h-screen">
      <div className="max-w-7xl mx-auto">
        <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-6">
          Academic Programs
        </h1>

        {error && (
          <div className="mb-4 p-4 bg-red-100 border border-red-400 text-red-700 rounded">
            {error}
          </div>
        )}

        {loading ? (
          <div className="flex justify-center items-center py-12">
            <div className="text-gray-600 dark:text-gray-400">Loading programs...</div>
          </div>
        ) : (
          <div className="overflow-x-auto shadow-md rounded-lg">
            <table className="w-full text-sm text-left text-gray-700 dark:text-gray-300">
              <thead className="text-xs uppercase bg-gray-100 dark:bg-gray-800 text-gray-700 dark:text-gray-300">
                <tr>
                  <th className="px-6 py-4 font-semibold">Program Name</th>
                  <th className="px-6 py-4 font-semibold">Program Code</th>
                  <th className="px-6 py-4 font-semibold">Level</th>
                  <th className="px-6 py-4 font-semibold">Duration (Years)</th>
                  <th className="px-6 py-4 font-semibold">Status</th>
                  <th className="px-6 py-4 font-semibold">Actions</th>
                </tr>
              </thead>
              <tbody>
                {programs.length === 0 ? (
                  <tr>
                    <td colSpan="6" className="px-6 py-8 text-center text-gray-500 dark:text-gray-400">
                      No programs found
                    </td>
                  </tr>
                ) : (
                  programs.map((program, index) => (
                    <tr
                      key={program.id ?? index}
                      className={`border-b dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-800 ${
                        index % 2 === 0 ? "bg-white dark:bg-gray-900" : "bg-gray-50 dark:bg-gray-850"
                      }`}
                    >
                      <td className="px-6 py-4 font-medium text-gray-900 dark:text-white">
                        {program.name}
                      </td>

                      <td className="px-6 py-4">
                        <span className="px-3 py-1 bg-blue-100 dark:bg-blue-900 text-blue-800 dark:text-blue-200 rounded-full text-xs font-medium">
                          {program.programCode}
                        </span>
                      </td>

                      <td className="px-6 py-4">
                        <span className="px-3 py-1 bg-purple-100 dark:bg-purple-900 text-purple-800 dark:text-purple-200 rounded-full text-xs font-medium">
                          {program.level}
                        </span>
                      </td>

                      <td className="px-6 py-4 text-center">{program.durationYears}</td>

                      <td className="px-6 py-4">
                        <span
                          className={`px-3 py-1 rounded-full text-xs font-medium ${
                            program.status
                              ? "bg-green-100 dark:bg-green-900 text-green-800 dark:text-green-200"
                              : "bg-red-100 dark:bg-red-900 text-red-800 dark:text-red-200"
                          }`}
                        >
                          {program.status ? "Active" : "Inactive"}
                        </span>
                      </td>

                      <td className="px-6 py-4">
                        <button
                          onClick={() => openAddDept(program)}
                          className="px-3 py-1 bg-red-600 hover:bg-indigo-700 text-white rounded text-sm"
                          title={`Add Department to ${program.name}`}
                        >
                          Add Department
                        </button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        )}

        {!loading && programs.length > 0 && (
          <div className="mt-4 text-sm text-gray-600 dark:text-gray-400">
            Showing {programs.length} program{programs.length !== 1 ? "s" : ""}
          </div>
        )}
      </div>

      {/* Simple Modal */}
      {showModal && selectedProgram && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          {/* overlay */}
          <div className="absolute inset-0 bg-black/40" onClick={closeModal} />

          <div className="relative bg-white dark:bg-gray-800 rounded-lg shadow-lg w-full max-w-md p-6 z-10">
            <h2 className="text-lg font-semibold mb-3 text-gray-900 dark:text-white">
              Add Department for:
            </h2>
            <div className="mb-4 text-sm text-gray-700 dark:text-gray-300">
              <strong>{selectedProgram.name}</strong> ({selectedProgram.programCode})
            </div>

            <form onSubmit={handleAddDepartment}>
              <label className="block mb-2 text-sm">
                <span className="text-gray-700 dark:text-gray-200">Department Code</span>
                <input
                  value={deptCode}
                  onChange={(e) => setDeptCode(e.target.value)}
                  className="mt-1 block w-full rounded border px-3 py-2 bg-white dark:bg-gray-900 text-gray-900 dark:text-gray-200"
                  placeholder="e.g. CSE"
                />
              </label>

              <label className="block mb-4 text-sm">
                <span className="text-gray-700 dark:text-gray-200">Department Name</span>
                <input
                  value={deptName}
                  onChange={(e) => setDeptName(e.target.value)}
                  className="mt-1 block w-full rounded border px-3 py-2 bg-white dark:bg-gray-900 text-gray-900 dark:text-gray-200"
                  placeholder="e.g. Computer Science"
                />
              </label>

              {submitMessage && (
                <div className="mb-3 text-sm text-center text-gray-700 dark:text-gray-200">
                  {submitMessage}
                </div>
              )}

              <div className="flex justify-end gap-2">
                <button
                  type="button"
                  onClick={closeModal}
                  className="px-3 py-1 rounded border bg-transparent text-gray-700 dark:text-gray-200"
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  disabled={submitLoading}
                  className="px-4 py-2 rounded bg-green-600 hover:bg-green-700 text-white disabled:opacity-60"
                >
                  {submitLoading ? "Saving..." : "Save Department"}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminProfileDetails;
