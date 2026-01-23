# Nethaji College ERP — Application Overview

This document provides a high-level overview of the College ERP application, its roles, modules, and key workflows.

---

## 1) Purpose

The application is a College ERP system for managing academic and administrative workflows for a college.

Primary goals:
- Maintain centralized student/staff academic records.
- Provide role-based dashboards for Admin, Staff, and Students.
- Enable announcements and bulk communications across channels.

---

## 2) User Roles

### Admin
- Manages master data (programs, students, staff).
- Controls system-level configuration and access.
- Can send bulk communications and track delivery.

### Staff
- Performs academic operations assigned to staff (attendance, student management, etc.).
- Access is role-based and can be enabled/disabled by Admin.

### Student
- Views personal profile and academic-related information.
- Receives announcements via in-app inbox (and other channels when enabled).

---

## 3) Major Features / Modules

### 3.1 Authentication & Access Control
- Login flow for users.
- Admin controls to enable/disable staff login.
- Password change for staff.

### 3.2 Student Management
- Student listing and management.
- Student profile and basic student data.

### 3.3 Staff Management
- Staff listing and management.
- Staff login enable/disable.

### 3.4 Academic Configuration
- Programs / academic metadata used for targeting and grouping.

### 3.5 Attendance
- Admin/Student attendance dashboard pages exist.

### 3.6 Bulk Messaging (Admin)
A bulk messaging module to send announcements to targeted students using multiple channels.

Core concepts:
- **Message Templates**: reusable content blocks.
- **Message Campaigns**: a bulk send job (title, content, targeting, channels, schedule).
- **Recipients**: resolved students for the campaign.
- **Outbox**: queue for asynchronous sending.
- **Delivery Attempts**: per-recipient delivery attempts and provider response/error.

Supported channels:
- **IN_APP**: delivered to the student inbox inside the application.
- **EMAIL**: delivered via AWS SES provider.
- **SMS**: provider interface exists; implementation can be swapped to a real SMS gateway.
- **WHATSAPP**: Meta WhatsApp Cloud API provider + webhook based delivery tracking.

Admin can:
- Create templates.
- Create campaigns (choose targeting + channels).
- Send immediately or schedule.
- View campaign history and delivery attempts.

### 3.7 Student Inbox (In-App)
- Students can view received in-app announcements.
- Unread count endpoint.
- Mark messages as read.

---

## 4) Communication Channels — How They Work

### In-App
- Writes an `InboxMessage` for each targeted student.
- Student reads it from Student Inbox dashboard.

### Email (AWS SES)
- Emails are sent by an outbox worker.
- Requires AWS SES sender verification and proper AWS credentials.
- If SES is in sandbox, recipient emails may also need verification for testing.

### SMS
- Interface exists; current behavior may be a stub depending on configuration.

### WhatsApp (Meta Cloud API)
- Sends approved WhatsApp templates using Meta Graph API.
- Delivery tracking uses Meta webhooks to update statuses (SENT/DELIVERED/READ/FAILED).
- Requires WABA onboarding, template approval, and webhook configuration.

---

## 5) Tech Stack

### Backend
- Java + Spring Boot
- Spring Data JPA (PostgreSQL)
- Scheduled worker for outbox processing

### Frontend
- Next.js / React
- Dashboard pages for Admin and Student

### Database
- PostgreSQL

---

## 6) Key Workflows

### 6.1 Admin Bulk Messaging Workflow
1. Admin selects a message template (or types content).
2. Admin selects targeting (ALL / PROGRAM / YEAR / SELECTED).
3. Admin selects channels (IN_APP / EMAIL / SMS / WHATSAPP).
4. System creates campaign, recipients, and outbox jobs.
5. Worker sends async for EMAIL/SMS/WHATSAPP.
6. Student can see IN_APP messages in Student Inbox.

### 6.2 Student Inbox Workflow
1. Student opens Student Inbox page.
2. System shows list and unread count.
3. Student marks a message as read.

---

## 7) Local Development URLs (typical)

### Frontend
- `http://localhost:3000`

### Backend
- `http://localhost:9029`

---

## 8) Notes / Production Deployment

- Backend can be deployed on Render.
- Frontend can be deployed on Vercel.
- Frontend should point to backend base URL using `NEXT_PUBLIC_API_BASE_URL`.

---

## 9) Related Documents

- `WHATSAPP_BUSINESS_API_SETUP.md` — official WhatsApp onboarding + webhook + template setup.

