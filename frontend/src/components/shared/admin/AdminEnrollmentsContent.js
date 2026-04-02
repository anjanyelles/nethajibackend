"use client";
import EnrollmentTable from "@/components/shared/tables/EnrollmentTable";
import EnrollmentDetailsModal from "@/components/shared/modals/EnrollmentDetailsModal";
import DashboardContainer from "@/components/shared/containers/DashboardContainer";
import DsahboardWrapper from "@/components/shared/wrappers/DsahboardWrapper";
import { useState } from "react";

const AdminEnrollmentsContent = () => {
  const [selectedEnrollment, setSelectedEnrollment] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleViewDetails = (enrollment) => {
    setSelectedEnrollment(enrollment);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedEnrollment(null);
  };

  return (
    <DsahboardWrapper>
      <DashboardContainer>
        <div className="space-y-6">
          {/* Page Header */}
          <div className="flex justify-between items-center">
            <div>
              <h1 className="text-3xl font-bold text-gray-900 dark:text-white">
                Student Enrollments
              </h1>
              <p className="text-gray-600 dark:text-gray-400 mt-2">
                Manage and view all student enrollment applications
              </p>
            </div>
          </div>

          {/* Enrollment Table */}
          <EnrollmentTable onViewDetails={handleViewDetails} />
        </div>
      </DashboardContainer>
      
      {/* Enrollment Details Modal */}
      <EnrollmentDetailsModal
        enrollmentId={selectedEnrollment?.id}
        isOpen={isModalOpen}
        onClose={handleCloseModal}
      />
    </DsahboardWrapper>
  );
};

export default AdminEnrollmentsContent;
