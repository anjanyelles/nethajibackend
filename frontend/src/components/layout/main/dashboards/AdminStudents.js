"use client";

import { useEffect, useState } from "react";
import {
  FaToggleOn,
  FaToggleOff,
  FaPlus,
  FaTimes,
} from "react-icons/fa";

import { createStudentByAdmin, getAllPrograms,getAllStudents,toggleStudentLoginStatus, updatePasswordByAdmin, } from "@/services/departmentService";
import { getAllStudents as getAllStudentsPaginated } from "@/services/authService";

export default function AdminStudents() {

const [showPasswordModal, setShowPasswordModal] = useState(false);
const [selectedStudent, setSelectedStudent] = useState(null);
const [newPassword, setNewPassword] = useState("");
const [passwordError, setPasswordError] = useState("");
const [passwordLoading, setPasswordLoading] = useState(false);


  const [programs, setPrograms] = useState([]);
  const [programId, setProgramId] = useState("");
  const [semester, setSemester] = useState(1);
  const [showAllStudents, setShowAllStudents] = useState(false);

  const [students, setStudents] = useState([]);
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  const [showModal, setShowModal] = useState(false);
  const [formLoading, setFormLoading] = useState(false);
  const [formError, setFormError] = useState("");

  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    email: "",
    mobileNumber: "",
    password: "",
    branch: "",
    graduationType: "UG",
    countryCode: "+91",
    userType: "STUDENT",
    programId: "",
    semester: 1,
  });

function validatePassword() {
  if (!newPassword || newPassword.trim().length < 6) {
    return "Password must be at least 6 characters";
  }
  return null;
}

  useEffect(() => {
    async function loadPrograms() {
      try {
        const data = await getAllPrograms();
        if (Array.isArray(data) && data.length > 0) {
          setPrograms(data);
          setProgramId(data[0].id);
          setForm((f) => ({ ...f, programId: data[0].id }));
        }
      } catch (err) {
        console.error(err);
      }
    }
    loadPrograms();
  }, []);


  useEffect(() => {
    if (showAllStudents) {
      fetchAllStudents();
    } else if (programId && semester) {
      fetchStudents();
    }
  }, [programId, semester, showAllStudents]);

  async function fetchStudents() {
    setLoading(true);
    setMessage("");
    try {
      const data = await getAllStudents(programId, semester);
      if (Array.isArray(data)) {
        setStudents(data);
        if (data.length === 0) {
          setMessage(`No students found for selected program and semester ${semester}. Try "Show All Students" or create a new student.`);
        }
      } else {
        setStudents([]);
        setMessage("Invalid response");
      }
    } catch (err) {
      console.error(err);
      setMessage("Server error");
    } finally {
      setLoading(false);
    }
  }

  async function fetchAllStudents() {
    setLoading(true);
    setMessage("");
    try {
      const data = await getAllStudentsPaginated(0, 100);
      if (data?.content && Array.isArray(data.content)) {
        // Transform paginated response to match expected format
        const transformed = data.content.map((student) => ({
          id: student.id,
          student: student,
          branch: student.branch || "N/A",
        }));
        setStudents(transformed);
        if (transformed.length === 0) {
          setMessage("No students found in the system.");
        }
      } else if (Array.isArray(data)) {
        setStudents(data);
        if (data.length === 0) {
          setMessage("No students found in the system.");
        }
      } else {
        setStudents([]);
        setMessage("Invalid response");
      }
    } catch (err) {
      console.error(err);
      setMessage("Server error");
    } finally {
      setLoading(false);
    }
  }


  async function handleToggleLogin(studentId, currentStatus) {
    try {
      const res = await toggleStudentLoginStatus(
        studentId,
        !currentStatus
      );

      if (res.status === 200) {
        setStudents((prev) =>
          prev.map((s) =>
            s.student.id === studentId
              ? {
                  ...s,
                  student: {
                    ...s.student,
                    isActive: !currentStatus,
                  },
                }
              : s
          )
        );
      }
    } catch (err) {
      alert("Failed to update login status");
    }
  }


  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  function validateForm() {
    if (!form.firstName.trim()) return "First name is required";
    if (!form.lastName.trim()) return "Last name is required";

    if (!form.email.trim()) return "Email is required";
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email))
      return "Invalid email format";

    if (!/^\d{10}$/.test(form.mobileNumber))
      return "Mobile number must be 10 digits";

    if (!form.password || form.password.length < 6)
      return "Password must be at least 6 characters";

    if (!form.branch) return "Branch is required";
    if (!form.programId) return "Program is required";

    if (!form.semester || form.semester < 1 || form.semester > 6)
      return "Semester must be between 1 and 6";

    return null;
  }

  async function handleCreateStudent(e) {
    e.preventDefault();
    setFormError("");

    const error = validateForm();
    if (error) {
      setFormError(error);
      return;
    }

    setFormLoading(true);

    try {
      const res = await createStudentByAdmin(form);

      if (res.status === 200 || res.status === 201) {
        setShowModal(false);
        fetchStudents();
      }
    } catch (err) {
      if (err.response) {
        const status = err.response.status;
        if (status === 400 || status === 409) {
          setFormError(err.response.data?.status || "Invalid input");
        } else if (status === 500) {
          setFormError("Server error. Try again later");
        } else {
          setFormError("Something went wrong");
        }
      } else {
        setFormError("Network error");
      }
    } finally {
      setFormLoading(false);
    }
  }


  async function handleUpdatePassword(e) {
  e.preventDefault();
  setPasswordError("");

  const error = validatePassword();
  if (error) {
    setPasswordError(error);
    return;
  }

  setPasswordLoading(true);

  try {
    const res = await updatePasswordByAdmin(selectedStudent.id,newPassword);

    if (res.status === 200) {
      setShowPasswordModal(false);
      alert("Password updated successfully");
    }

  } catch (err) {
    if (err.response) {
      const status = err.response.status;

      if (status === 400) {
        setPasswordError(err.response.data?.message || "Invalid request");
      } else if (status === 404) {
        setPasswordError("Student not found");
      } else if (status === 500) {
        setPasswordError("Server error. Try again later");
      } else {
        setPasswordError("Something went wrong");
      }
    } else {
      setPasswordError("Network error");
    }
  } finally {
    setPasswordLoading(false);
  }
}


  return (
    <div className="max-w-7xl mx-auto mt-8 bg-white p-6 rounded shadow">
      {/* HEADER */}
      <div className="flex justify-between mb-6">
        <h1 className="text-2xl font-bold">Students</h1>
     <button
  onClick={() => setShowModal(true)}
  className="w-48 text-size-12 2xl:text-size-15 text-whiteColor bg-primaryColor block border-primaryColor border hover:text-primaryColor hover:bg-white px-15px py-2 rounded-standard dark:hover:bg-whiteColor-dark dark:hover:text-whiteColor"
>
  <FaPlus className="inline mr-2" /> Add Student
</button>

      </div>

      {/* FILTERS */}
      <div className="flex gap-4 mb-6 items-center">
        <div className="flex gap-2 items-center">
          <input
            type="checkbox"
            id="showAll"
            checked={showAllStudents}
            onChange={(e) => {
              setShowAllStudents(e.target.checked);
              if (e.target.checked) {
                setMessage("");
              }
            }}
            className="w-4 h-4"
          />
          <label htmlFor="showAll" className="text-sm font-medium">
            Show All Students
          </label>
        </div>
        
        {!showAllStudents && (
          <>
            <select
              value={programId}
              onChange={(e) => {
                setProgramId(e.target.value);
                setShowAllStudents(false);
              }}
              className="border p-2 rounded w-1/2"
            >
              {programs.map((p) => (
                <option key={p.id} value={p.id}>
                  {p.name}
                </option>
              ))}
            </select>

            <select
              value={semester}
              onChange={(e) => {
                setSemester(Number(e.target.value));
                setShowAllStudents(false);
              }}
              className="border p-2 rounded"
            >
              {[1, 2, 3, 4, 5, 6].map((s) => (
                <option key={s} value={s}>
                  Semester {s}
                </option>
              ))}
            </select>
          </>
        )}
      </div>

      {message && (
        <p className="mb-4 text-center font-semibold">{message}</p>
      )}

      {/* TABLE */}
{/* TABLE */}
<div className="overflow-x-auto">
  <table className="min-w-[1000px] w-full border border-collapse">
        <thead className="bg-gray-100">
          <tr>
            <th className="border p-2">Enroll No</th>
            <th className="border p-2">Name</th>
            <th className="border p-2">Email</th>
            <th className="border p-2">Mobile</th>
            <th className="border p-2">Branch</th>
            <th className="border p-2">Login</th>
            <th className="border p-2">Action</th>
          </tr>
        </thead>

        <tbody>
          {loading ? (
            <tr>
              <td colSpan="7" className="text-center p-4">
                Loading...
              </td>
            </tr>
          ) : students.length === 0 ? (
            <tr>
              <td colSpan="7" className="text-center p-4 text-gray-500">
                {message || "No students found"}
              </td>
            </tr>
          ) : (
            students.map((s) => {
              const student = s.student || s;
              const studentId = student.id || s.id;
              const enrollmentNumber = student.enrollmentNumber || "N/A";
              const firstName = student.firstName || "";
              const lastName = student.lastName || "";
              const email = student.email || "N/A";
              const mobileNumber = student.mobileNumber || "N/A";
              const branch = s.branch || student.branch || "N/A";
              const isActive = student.isActive !== undefined ? student.isActive : true;

              return (
                <tr key={studentId || s.id}>
                  <td className="border p-2">{enrollmentNumber}</td>
                  <td className="border p-2">
                    {firstName} {lastName}
                  </td>
                  <td className="border p-2">{email}</td>
                  <td className="border p-2">{mobileNumber}</td>
                  <td className="border p-2">{branch}</td>
                  <td className="border p-2 text-center">
                    <button
                      onClick={() =>
                        handleToggleLogin(studentId, isActive)
                      }
                    >
                      {isActive ? (
                        <FaToggleOn className="text-green-600 text-2xl" />
                      ) : (
                        <FaToggleOff className="text-gray-400 text-2xl" />
                      )}
                    </button>
                  </td>
                  <td className="border p-2">
                    <button
                      onClick={() => {
                        setSelectedStudent(student);
                        setNewPassword("");
                        setPasswordError("");
                        setShowPasswordModal(true);
                      }}
                      className="text-sm bg-primaryColor text-white px-3 py-1 rounded hover:bg-white hover:text-primaryColor border border-primaryColor transition"
                    >
                      Update Password
                    </button>
                  </td>
                </tr>
              );
            })
          )}
        </tbody>
      </table>
      </div>

    {/* MODAL */}
{showModal && (
  <div className="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
    <div className="bg-white p-6 rounded w-[520px]">

      {/* Header */}
      <div className="flex justify-between mb-4">
        <h2 className="text-xl font-bold">Add Student</h2>
        <button onClick={() => setShowModal(false)}>
          <FaTimes size={20} />
        </button>
      </div>

      {/* Error */}
      {formError && (
        <div className="bg-red-100 text-red-700 p-2 rounded mb-3 text-sm">
          {formError}
        </div>
      )}

      <form onSubmit={handleCreateStudent} className="space-y-3">

        {/* Row 1 */}
        <div className="grid grid-cols-2 gap-3">
          <input
            name="firstName"
            placeholder="First Name"
            onChange={handleChange}
            className="border p-2 rounded"
          />
          <input
            name="lastName"
            placeholder="Last Name"
            onChange={handleChange}
            className="border p-2 rounded"
          />
        </div>

        {/* Row 2 */}
        <div className="grid grid-cols-2 gap-3">
          <input
            name="email"
            placeholder="Email"
            onChange={handleChange}
            className="border p-2 rounded"
          />
          <input
            name="mobileNumber"
            placeholder="Mobile Number"
            onChange={handleChange}
            className="border p-2 rounded"
          />
        </div>

        {/* Row 3 */}
        <div className="grid grid-cols-2 gap-3">
          <input
            name="password"
            placeholder="Password"
            onChange={handleChange}
            className="border p-2 rounded"
          />

          <select
            name="branch"
            onChange={handleChange}
            className="border p-2 rounded"
          >
            <option value="">Select Branch</option>
            <option value="BCOM">BCOM</option>
            <option value="BBA">BBA</option>
            <option value="BSC">BSC</option>
            <option value="MSC">MSC</option>
          </select>
        </div>

        {/* Row 4 */}
        <div className="grid grid-cols-2 gap-3">
          <select
            name="programId"
            value={form.programId}
            onChange={handleChange}
            className="border p-2 rounded"
          >
            {programs.map((p) => (
              <option key={p.id} value={p.id}>
                {p.name}
              </option>
            ))}
          </select>

          <input
            type="number"
            name="semester"
            value={form.semester}
            onChange={handleChange}
            min="1"
            max="6"
            className="border p-2 rounded"
          />
        </div>

        {/* Submit */}
        <button
          type="submit"
          disabled={formLoading}
          className="w-full bg-primaryColor text-white p-2 rounded border border-primaryColor hover:bg-white hover:text-primaryColor transition disabled:opacity-50 mt-3"
        >
          {formLoading ? "Creating..." : "Create Student"}
        </button>

      </form>
    </div>
  </div>
)}

{showPasswordModal && (
  <div className="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center z-50">
    <div className="bg-white p-6 rounded w-[400px]">

      {/* Header */}
      <div className="flex justify-between mb-4">
        <h2 className="text-xl font-bold">Update Password</h2>
        <button onClick={() => setShowPasswordModal(false)}>
          <FaTimes size={20} />
        </button>
      </div>

      {/* Student Info */}
      <p className="text-sm mb-3 text-gray-600">
        {selectedStudent?.firstName} {selectedStudent?.lastName}
        <br />
        <span className="text-xs">{selectedStudent?.email}</span>
      </p>

      {/* Error */}
      {passwordError && (
        <div className="bg-red-100 text-red-700 p-2 rounded mb-3 text-sm">
          {passwordError}
        </div>
      )}

      <form onSubmit={handleUpdatePassword} className="space-y-3">
        <input
          type="password"
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
          placeholder="New Password"
          className="w-full border p-2 rounded"
        />

        <button
          type="submit"
          disabled={passwordLoading}
          className="w-full bg-primaryColor text-white p-2 rounded border border-primaryColor hover:bg-white hover:text-primaryColor transition disabled:opacity-50"
        >
          {passwordLoading ? "Updating..." : "Update Password"}
        </button>
      </form>
    </div>
  </div>
)}





    </div>
  );
}





