"use client";

import React, { useState, useEffect } from "react";
import {
  getAllDepartments,
  updateSemester,
} from "@/services/departmentService";

const AdminDepartment = () => {
  const [departments, setDepartments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [selectedDept, setSelectedDept] = useState(null);
  const [showModal, setShowModal] = useState(false);

  // For Edit Modal
  const [showEditModal, setShowEditModal] = useState(false);
  const [editError, setEditError] = useState("");
  const [editSuccess, setEditSuccess] = useState("");

  const [editData, setEditData] = useState({
    id: null,
    semesterStartDate: "",
    semesterEndDate: "",
    semester: "",
    semesterYear: "",
    semesterSubjects: "",
    semesterTotalLabs: "",
    departmentId: "",
  });

  useEffect(() => {
    fetchDepartments();
  }, []);

  const fetchDepartments = async () => {
    setLoading(true);
    setError("");

    try {
      const data = await getAllDepartments();
      setDepartments(data);
    } catch (err) {
      setError("Failed to fetch departments");
    } finally {
      setLoading(false);
    }
  };

  const openModal = (department) => {
    setSelectedDept(department);
    setShowModal(true);
  };

  const closeModal = () => {
    setSelectedDept(null);
    setShowModal(false);
  };

  const formatDate = (dateString) => {
    if (!dateString) return "N/A";
    return new Date(dateString).toLocaleDateString();
  };

  // OPEN EDIT MODAL
  const openEditModal = (sem) => {
    setEditError("");
    setEditSuccess("");

    setEditData({
      id: sem.id,
      semesterStartDate: sem.semesterStartDate?.slice(0, 10) || "",
      semesterEndDate: sem.semesterEndDate?.slice(0, 10) || "",
      semester: sem.semester,
      semesterYear: sem.semesterYear || "",
      semesterSubjects: sem.semesterSubjects,
      semesterTotalLabs: sem.semesterTotalLabs,
      departmentId: selectedDept.id,
    });

    setShowEditModal(true);
  };

  // SUBMIT EDIT
  const handleSemesterSubmit = async () => {
    setEditError("");
    setEditSuccess("");

    const response = await updateSemester(editData);

    // ❌ HANDLE BAD REQUEST
    if (response.status === 400) {
      setEditError(response.data.message || "Validation error");
      return;
    }

    // ✅ HANDLE SUCCESS
    if (response.status === 200) {
      setEditSuccess("Semester updated successfully!");

      await fetchDepartments(); // refresh table

      // Auto close
      setTimeout(() => {
        setShowEditModal(false);
        setEditSuccess("");
      }, 800);
    }
  };

  return (
    <div className="p-6 bg-white dark:bg-gray-900 min-h-screen">
      <div className="max-w-7xl mx-auto">
        {/* HEADER */}
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-3xl font-bold text-gray-900 dark:text-white">
            Departments
          </h1>
          <button
            onClick={fetchDepartments}
            className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg"
          >
            Refresh
          </button>
        </div>

        {error && (
          <div className="mb-4 p-4 bg-red-200 text-red-700 rounded">
            {error}
          </div>
        )}

        {loading ? (
          <div className="text-center text-gray-500">Loading...</div>
        ) : (
          <div className="overflow-x-auto shadow-lg rounded-lg">
            <table className="w-full text-sm text-left text-gray-700 dark:text-gray-300">
              <thead className="bg-gray-200 dark:bg-gray-800 text-gray-700 dark:text-gray-300">
                <tr>
                  <th className="px-6 py-3">Department Name</th>
                  <th className="px-6 py-3">Code</th>
                  <th className="px-6 py-3">Created</th>
                  <th className="px-6 py-3">Updated</th>
                  <th className="px-6 py-3">View</th>
                </tr>
              </thead>

              <tbody>
                {departments.map((dept) => (
                  <tr
                    key={dept.id}
                    className="border-b dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-800"
                  >
                    <td className="px-6 py-4 font-medium">
                      {dept.departmentName}
                    </td>
                    <td className="px-6 py-4">{dept.departmentCode}</td>
                    <td className="px-6 py-4">{formatDate(dept.createdAt)}</td>
                    <td className="px-6 py-4">{formatDate(dept.updatedAt)}</td>

                    <td className="px-6 py-4">
                      <button
                        className="px-3 py-1 bg-[#1f5a6c]/90 text-white rounded hover:bg-[#174652] transition"
                        onClick={() => openModal(dept)}
                      >
                        View
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {/* VIEW MODAL */}
        {showModal && selectedDept && (
          <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
            <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-xl w-3/4 max-h-[80vh] overflow-y-auto">
              <h2 className="text-2xl font-bold mb-4">
                {selectedDept.departmentName} - Semesters
              </h2>

              {selectedDept.departMentSemestersDtoList?.length > 0 ? (
                <table className="w-full text-sm text-left border">
                  <thead className="bg-gray-100 dark:bg-gray-700">
                    <tr>
                      <th className="px-4 py-2">Semester</th>
                      <th className="px-4 py-2">Start Date</th>
                      <th className="px-4 py-2">End Date</th>
                      <th className="px-4 py-2">Subjects</th>
                      <th className="px-4 py-2">Labs</th>
                      <th className="px-4 py-2">Actions</th>
                    </tr>
                  </thead>

                  <tbody>
                    {[...selectedDept.departMentSemestersDtoList]
                      .sort((a, b) => a.semester - b.semester) // ← SORT HERE
                      .map((sem) => (
                        <tr key={sem.id} className="border-b">
                          <td className="px-4 py-2">{sem.semester}</td>
                          <td className="px-4 py-2">
                            {formatDate(sem.semesterStartDate)}
                          </td>
                          <td className="px-4 py-2">
                            {formatDate(sem.semesterEndDate)}
                          </td>
                          <td className="px-4 py-2">{sem.semesterSubjects}</td>
                          <td className="px-4 py-2">{sem.semesterTotalLabs}</td>

                          <td className="px-4 py-2">
                            <button
                              className="px-2 py-1 bg-[#1f5a6c] text-white rounded hover:bg-[#174652] transition"
                              onClick={() => openEditModal(sem)}
                            >
                              Edit
                            </button>
                          </td>
                        </tr>
                      ))}
                  </tbody>
                </table>
              ) : (
                <p>No semesters found.</p>
              )}

              <button
                onClick={closeModal}
                className="mt-4 px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700"
              >
                Close
              </button>
            </div>
          </div>
        )}

        {/* EDIT MODAL */}
        {showEditModal && (
          <div className="fixed inset-0 flex justify-center items-center bg-black bg-opacity-50">
            <div className="bg-white dark:bg-gray-800 p-6 rounded-lg w-1/2">
              <h2 className="text-xl font-bold mb-4">Edit Semester</h2>

              {/* ERROR MESSAGE */}
              {editError && (
                <div className="mb-3 p-2 bg-red-100 text-red-700 rounded">
                  {editError}
                </div>
              )}

              {/* SUCCESS MESSAGE */}
              {editSuccess && (
                <div className="mb-3 p-2 bg-green-100 text-green-700 rounded">
                  {editSuccess}
                </div>
              )}

              {/* FORM GRID */}
              <div className="grid grid-cols-2 gap-4">
                {/* START DATE */}
                <div className="flex flex-col">
                  <label className="text-sm font-medium mb-1">
                    Semester Start Date
                  </label>
                  <input
                    type="date"
                    value={editData.semesterStartDate}
                    onChange={(e) =>
                      setEditData({
                        ...editData,
                        semesterStartDate: e.target.value,
                      })
                    }
                    className="p-2 border rounded"
                  />
                </div>

                {/* END DATE */}
                <div className="flex flex-col">
                  <label className="text-sm font-medium mb-1">
                    Semester End Date
                  </label>
                  <input
                    type="date"
                    value={editData.semesterEndDate}
                    onChange={(e) =>
                      setEditData({
                        ...editData,
                        semesterEndDate: e.target.value,
                      })
                    }
                    className="p-2 border rounded"
                  />
                </div>

                {/* SUBJECT COUNT */}
                <div className="flex flex-col">
                  <label className="text-sm font-medium mb-1">
                    Total Subjects
                  </label>
                  <input
                    type="number"
                    value={editData.semesterSubjects}
                    onChange={(e) =>
                      setEditData({
                        ...editData,
                        semesterSubjects: e.target.value,
                      })
                    }
                    className="p-2 border rounded"
                    placeholder="Enter number of subjects"
                  />
                </div>

                {/* LAB COUNT */}
                <div className="flex flex-col">
                  <label className="text-sm font-medium mb-1">Total Labs</label>
                  <input
                    type="number"
                    value={editData.semesterTotalLabs}
                    onChange={(e) =>
                      setEditData({
                        ...editData,
                        semesterTotalLabs: e.target.value,
                      })
                    }
                    className="p-2 border rounded"
                    placeholder="Enter number of labs"
                  />
                </div>

                {/* SEMESTER YEAR */}
                <div className="flex flex-col">
                  <label className="text-sm font-medium mb-1">
                    Semester Year
                  </label>
                  <input
                    type="text"
                    value={editData.semesterYear}
                    onChange={(e) =>
                      setEditData({ ...editData, semesterYear: e.target.value })
                    }
                    className="p-2 border rounded"
                    placeholder="Ex: 2025"
                  />
                </div>
              </div>

              {/* BUTTONS */}
              <div className="flex justify-end mt-4 gap-3">
                <button
                  className="px-4 py-2 bg-gray-500 text-white rounded"
                  onClick={() => setShowEditModal(false)}
                >
                  Cancel
                </button>

                <button
                  className="px-4 py-2 bg-[#1f5a6c] text-white rounded hover:bg-[#174652] transition"
                  onClick={handleSemesterSubmit}
                >
                  Save
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default AdminDepartment;
