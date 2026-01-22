"use client";

import { useEffect, useState } from "react";
import { FaPlus, FaEdit, FaTrash, FaToggleOn, FaToggleOff } from "react-icons/fa";
import { 
  createUser, 
  getAllLecturers, 
  getAllStaffProfiles,
  saveStaffProfile,
  toggleStudentStatus 
} from "@/services";
import { getAllDepartments } from "@/services/academicService";

 const SUBJECT_TYPE_OPTIONS = [
  { value: "GENERAL", label: "General" },
  { value: "COMPUTER_SCIENCE", label: "Computer Science" },
  { value: "DATA_SCIENCE", label: "Data Science" },
  { value: "COMMERCE", label: "Commerce" },
  { value: "MANAGEMENT", label: "Management" },
  { value: "ENGLISH", label: "English" },
  { value: "TELUGU", label: "Telugu" },
  { value: "ECONOMICS", label: "Economics" },
  { value: "STATISTICS", label: "Statistics" },
  { value: "MATHS", label: "Mathematics" },
  { value: "PHYSICS", label: "Physics" },
  { value: "CHEMISTRY", label: "Chemistry" },
  { value: "BOTANY", label: "Botany" },
  { value: "ZOOLOGY", label: "Zoology" },
  { value: "FOOD_SCIENCE", label: "Food Science" },
 ];

export default function AdminStaff() {
  const [staff, setStaff] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  // Modal states
  const [showAddModal, setShowAddModal] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);
  const [selectedStaff, setSelectedStaff] = useState(null);
  const [formLoading, setFormLoading] = useState(false);

  // Form data for creating staff
  const [userForm, setUserForm] = useState({
    firstName: "",
    lastName: "",
    email: "",
    mobileNumber: "",
    password: "",
    countryCode: "+91",
    userType: "LECTURER",
    subjectType: "GENERAL",
    graduationType: "",
  });

  // Staff profile form
  const [staffForm, setStaffForm] = useState({
    firstName: "",
    lastName: "",
    middleName: "",
    department: "",
    designation: "",
    qualification: "",
    joiningDate: "",
    experienceYears: "",
    phoneNumber: "",
    email: "",
    employmentType: "FULLTIME",
    salary: "",
    emergencyContactName: "",
    emergencyContactPhone: "",
    address: "",
    dateOfBirth: "",
    gender: "MALE",
    employeeSubject: "",
    status: "ACTIVE",
  });

  const [departments, setDepartments] = useState([]);

  useEffect(() => {
    fetchStaff();
    fetchDepartments();
  }, []);

  const fetchDepartments = async () => {
    try {
      const data = await getAllDepartments();
      setDepartments(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error("Error fetching departments:", err);
    }
  };

  const fetchStaff = async () => {
    setLoading(true);
    setError("");
    try {
      const data = await getAllStaffProfiles();
      setStaff(Array.isArray(data) ? data : []);
    } catch (err) {
      setError("Failed to load staff");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleUserFormChange = (e) => {
    const { name, value } = e.target;
    setUserForm({ ...userForm, [name]: value });
  };

  const handleStaffFormChange = (e) => {
    const { name, value } = e.target;
    setStaffForm({ ...staffForm, [name]: value });
  };

  const validateUserForm = () => {
    if (!userForm.firstName.trim()) return "First name is required";
    if (!userForm.lastName.trim()) return "Last name is required";
    if (!userForm.email.trim()) return "Email is required";
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(userForm.email)) return "Invalid email format";
    if (!/^\d{10}$/.test(userForm.mobileNumber)) return "Mobile number must be 10 digits";
    if (!userForm.password || userForm.password.length < 6) return "Password must be at least 6 characters";
    return null;
  };

  const validateStaffForm = () => {
    if (!staffForm.firstName.trim()) return "First name is required";
    if (!staffForm.lastName.trim()) return "Last name is required";
    if (!staffForm.department) return "Department is required";
    if (!staffForm.designation) return "Designation is required";
    if (!staffForm.joiningDate) return "Joining date is required";
    if (!staffForm.employeeSubject) return "Subject is required";
    return null;
  };

  const handleCreateStaff = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    const userError = validateUserForm();
    if (userError) {
      setError(userError);
      return;
    }

    const staffError = validateStaffForm();
    if (staffError) {
      setError(staffError);
      return;
    }

    setFormLoading(true);

    try {
      // Step 1: Create User account
      const userResponse = await createUser({
        firstName: userForm.firstName,
        lastName: userForm.lastName,
        email: userForm.email,
        mobileNumber: userForm.mobileNumber,
        password: userForm.password,
        countryCode: userForm.countryCode,
        userType: userForm.userType,
        subjectType: userForm.subjectType,
        graduationType: staffForm.department || userForm.graduationType,
      });

      if (userResponse.status !== "Login Successful" && !userResponse.userId) {
        setError(userResponse.status || "Failed to create user account");
        return;
      }

      const userId = userResponse.userId || userResponse.id;

      // Step 2: Create Staff Profile
      const staffProfileData = {
        userId: userId,
        firstName: staffForm.firstName,
        lastName: staffForm.lastName,
        middleName: staffForm.middleName || "",
        department: staffForm.department,
        designation: staffForm.designation,
        qualification: staffForm.qualification || "",
        joiningDate: staffForm.joiningDate,
        experienceYears: staffForm.experienceYears ? parseInt(staffForm.experienceYears) : 0,
        phoneNumber: staffForm.phoneNumber || userForm.mobileNumber,
        email: staffForm.email || userForm.email,
        employmentType: staffForm.employmentType,
        salary: staffForm.salary ? parseFloat(staffForm.salary) : null,
        emergencyContactName: staffForm.emergencyContactName || "",
        emergencyContactPhone: staffForm.emergencyContactPhone || "",
        address: staffForm.address || "",
        dateOfBirth: staffForm.dateOfBirth || null,
        gender: staffForm.gender,
        employeeSubject: staffForm.employeeSubject,
        status: staffForm.status,
      };

      await saveStaffProfile(staffProfileData);

      setSuccess("Staff member created successfully!");
      setShowAddModal(false);
      resetForms();
      fetchStaff();

      setTimeout(() => setSuccess(""), 3000);
    } catch (err) {
      setError(err.response?.data?.status || err.message || "Failed to create staff");
      console.error("Create staff error:", err);
    } finally {
      setFormLoading(false);
    }
  };

  const resetForms = () => {
    setUserForm({
      firstName: "",
      lastName: "",
      email: "",
      mobileNumber: "",
      password: "",
      countryCode: "+91",
      userType: "LECTURER",
      subjectType: "GENERAL",
      graduationType: "",
    });
    setStaffForm({
      firstName: "",
      lastName: "",
      middleName: "",
      department: "",
      designation: "",
      qualification: "",
      joiningDate: "",
      experienceYears: "",
      phoneNumber: "",
      email: "",
      employmentType: "FULLTIME",
      salary: "",
      emergencyContactName: "",
      emergencyContactPhone: "",
      address: "",
      dateOfBirth: "",
      gender: "MALE",
      employeeSubject: "",
      status: "ACTIVE",
    });
  };

  const openAddModal = () => {
    resetForms();
    setError("");
    setSuccess("");
    setShowAddModal(true);
  };

  const formatDate = (dateString) => {
    if (!dateString) return "N/A";
    try {
      return new Date(dateString).toLocaleDateString();
    } catch {
      return dateString;
    }
  };

  return (
    <div className="p-6 bg-white dark:bg-gray-900 min-h-screen">
      <div className="max-w-7xl mx-auto">
        {/* Header */}
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-3xl font-bold text-gray-900 dark:text-white">
            Staff Management
          </h1>
          <button
            onClick={openAddModal}
            className="px-4 py-2 bg-[#1f5a6c] hover:bg-[#174652] text-white rounded-lg flex items-center gap-2"
          >
            <FaPlus /> Add Staff
          </button>
        </div>

        {/* Messages */}
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

        {/* Staff List */}
        {loading ? (
          <div className="text-center text-gray-500 py-10">Loading staff...</div>
        ) : staff.length === 0 ? (
          <div className="text-center text-gray-500 py-10">No staff members found</div>
        ) : (
          <div className="overflow-x-auto shadow-lg rounded-lg">
            <table className="w-full text-sm text-left text-gray-700 dark:text-gray-300">
              <thead className="bg-gray-200 dark:bg-gray-800">
                <tr>
                  <th className="px-6 py-3">Name</th>
                  <th className="px-6 py-3">Email</th>
                  <th className="px-6 py-3">Department</th>
                  <th className="px-6 py-3">Designation</th>
                  <th className="px-6 py-3">Subject</th>
                  <th className="px-6 py-3">Joining Date</th>
                  <th className="px-6 py-3">Status</th>
                  <th className="px-6 py-3">Actions</th>
                </tr>
              </thead>
              <tbody>
                {staff.map((member) => (
                  <tr
                    key={member.id}
                    className="border-b dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-800"
                  >
                    <td className="px-6 py-4 font-medium">
                      {member.firstName} {member.lastName}
                    </td>
                    <td className="px-6 py-4">{member.email || "N/A"}</td>
                    <td className="px-6 py-4">{member.department || "N/A"}</td>
                    <td className="px-6 py-4">{member.designation || "N/A"}</td>
                    <td className="px-6 py-4">{member.employeeSubject || "N/A"}</td>
                    <td className="px-6 py-4">{formatDate(member.joiningDate)}</td>
                    <td className="px-6 py-4">
                      <span
                        className={`px-2 py-1 rounded text-xs ${
                          member.status === "ACTIVE"
                            ? "bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-300"
                            : "bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-300"
                        }`}
                      >
                        {member.status || "ACTIVE"}
                      </span>
                    </td>
                    <td className="px-6 py-4">
                      <button
                        onClick={() => {
                          setSelectedStaff(member);
                          setShowEditModal(true);
                        }}
                        className="px-3 py-1 bg-blue-600 hover:bg-blue-700 text-white rounded mr-2"
                      >
                        <FaEdit />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {/* Add Staff Modal */}
        {showAddModal && (
          <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center z-50">
            <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-xl w-full max-w-4xl max-h-[90vh] overflow-y-auto">
              <h2 className="text-2xl font-bold mb-4 text-gray-900 dark:text-white">
                Add New Staff Member
              </h2>

              <form onSubmit={handleCreateStaff}>
                {/* User Account Section */}
                <div className="mb-6">
                  <h3 className="text-lg font-semibold mb-3 text-gray-700 dark:text-gray-300">
                    User Account Details
                  </h3>
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-sm font-medium mb-1">First Name *</label>
                      <input
                        type="text"
                        name="firstName"
                        value={userForm.firstName}
                        onChange={handleUserFormChange}
                        className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                        required
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium mb-1">Last Name *</label>
                      <input
                        type="text"
                        name="lastName"
                        value={userForm.lastName}
                        onChange={handleUserFormChange}
                        className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                        required
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium mb-1">Email *</label>
                      <input
                        type="email"
                        name="email"
                        value={userForm.email}
                        onChange={handleUserFormChange}
                        className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                        required
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium mb-1">Mobile Number *</label>
                      <input
                        type="text"
                        name="mobileNumber"
                        value={userForm.mobileNumber}
                        onChange={handleUserFormChange}
                        maxLength={10}
                        className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                        required
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium mb-1">Password *</label>
                      <input
                        type="password"
                        name="password"
                        value={userForm.password}
                        onChange={handleUserFormChange}
                        className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                        required
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium mb-1">Subject Type *</label>
                      <select
                        name="subjectType"
                        value={userForm.subjectType}
                        onChange={handleUserFormChange}
                        className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                        required
                      >
                        {SUBJECT_TYPE_OPTIONS.map((opt) => (
                          <option key={opt.value} value={opt.value}>
                            {opt.label}
                          </option>
                        ))}
                      </select>
                    </div>
                  </div>
                </div>

                {/* Staff Profile Section */}
                <div className="mb-6">
                  <h3 className="text-lg font-semibold mb-3 text-gray-700 dark:text-gray-300">
                    Staff Profile Details
                  </h3>
                  <div className="grid grid-cols-2 gap-4">
                    <div>
                      <label className="block text-sm font-medium mb-1">First Name *</label>
                      <input
                        type="text"
                        name="firstName"
                        value={staffForm.firstName}
                        onChange={handleStaffFormChange}
                        className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                        required
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium mb-1">Last Name *</label>
                      <input
                        type="text"
                        name="lastName"
                        value={staffForm.lastName}
                        onChange={handleStaffFormChange}
                        className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                        required
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium mb-1">Department *</label>
                      <select
                        name="department"
                        value={staffForm.department}
                        onChange={handleStaffFormChange}
                        className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                        required
                      >
                        <option value="">Select Department</option>
                        {departments.map((dept) => (
                          <option key={dept.id} value={dept.departmentCode}>
                            {dept.departmentName}
                          </option>
                        ))}
                      </select>
                    </div>
                    <div>
                      <label className="block text-sm font-medium mb-1">Designation *</label>
                      <input
                        type="text"
                        name="designation"
                        value={staffForm.designation}
                        onChange={handleStaffFormChange}
                        placeholder="e.g., Professor, Assistant Professor"
                        className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                        required
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium mb-1">Qualification</label>
                      <input
                        type="text"
                        name="qualification"
                        value={staffForm.qualification}
                        onChange={handleStaffFormChange}
                        placeholder="e.g., Ph.D., M.Sc"
                        className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium mb-1">Subject *</label>
                      <input
                        type="text"
                        name="employeeSubject"
                        value={staffForm.employeeSubject}
                        onChange={handleStaffFormChange}
                        placeholder="e.g., Mathematics, Physics"
                        className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                        required
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium mb-1">Joining Date *</label>
                      <input
                        type="date"
                        name="joiningDate"
                        value={staffForm.joiningDate}
                        onChange={handleStaffFormChange}
                        className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                        required
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium mb-1">Experience (Years)</label>
                      <input
                        type="number"
                        name="experienceYears"
                        value={staffForm.experienceYears}
                        onChange={handleStaffFormChange}
                        className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium mb-1">Employment Type</label>
                      <select
                        name="employmentType"
                        value={staffForm.employmentType}
                        onChange={handleStaffFormChange}
                        className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                      >
                        <option value="FULLTIME">Full Time</option>
                        <option value="PARTTIME">Part Time</option>
                        <option value="CONTRACT">Contract</option>
                      </select>
                    </div>
                    <div>
                      <label className="block text-sm font-medium mb-1">Salary</label>
                      <input
                        type="number"
                        name="salary"
                        value={staffForm.salary}
                        onChange={handleStaffFormChange}
                        className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium mb-1">Gender</label>
                      <select
                        name="gender"
                        value={staffForm.gender}
                        onChange={handleStaffFormChange}
                        className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                      >
                        <option value="MALE">Male</option>
                        <option value="FEMALE">Female</option>
                        <option value="OTHER">Other</option>
                      </select>
                    </div>
                    <div>
                      <label className="block text-sm font-medium mb-1">Date of Birth</label>
                      <input
                        type="date"
                        name="dateOfBirth"
                        value={staffForm.dateOfBirth}
                        onChange={handleStaffFormChange}
                        className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                      />
                    </div>
                    <div className="col-span-2">
                      <label className="block text-sm font-medium mb-1">Address</label>
                      <textarea
                        name="address"
                        value={staffForm.address}
                        onChange={handleStaffFormChange}
                        rows={2}
                        className="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"
                      />
                    </div>
                  </div>
                </div>

                {/* Buttons */}
                <div className="flex justify-end gap-3 mt-6">
                  <button
                    type="button"
                    onClick={() => {
                      setShowAddModal(false);
                      resetForms();
                    }}
                    className="px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    disabled={formLoading}
                    className="px-4 py-2 bg-[#1f5a6c] text-white rounded hover:bg-[#174652] disabled:opacity-50"
                  >
                    {formLoading ? "Creating..." : "Create Staff"}
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

