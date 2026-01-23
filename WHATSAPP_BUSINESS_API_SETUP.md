# WhatsApp Business API Setup (Official) for Nethaji ERP

This document is a **step-by-step operational guide** to enable official WhatsApp messaging from your college business number **9441840511** through the **WhatsApp Business Platform (Meta Cloud API)** and integrate it with your existing **Bulk Messaging** module.

> Goal: **Admin sends message in ERP → Students receive on WhatsApp from the official college number → ERP tracks delivery + history**.

---

## 0) Understand the WhatsApp “rules” (important)

WhatsApp has two distinct messaging modes:

### A) **User-initiated (24-hour customer care window)**
If a student messages your number first, you can reply with free-form messages for 24 hours.

### B) **Business-initiated (Admin broadcasts)**
Your Admin-to-Student messaging (announcements, reminders, exam alerts) is **business-initiated**.

That requires:
- **Approved WhatsApp Message Templates**
- Compliance with Meta policies and quality rules

**You cannot legally send arbitrary WhatsApp messages to students without templates** unless they message first.

---

## 1) Decide number strategy for `9441840511`

You have 2 valid options:

### Option 1 — Use the same number `9441840511` (recommended if it is the official public number)
- You will register this number under WhatsApp Business Platform.
- The number will be used by the WhatsApp API sender.
- You should not use this number simultaneously as a normal WhatsApp consumer account.

### Option 2 — Use a new dedicated number
- Operationally easiest.
- Keeps your original number unaffected.

**Recommendation for colleges:**
- If `9441840511` is already printed everywhere (website, admissions, fee receipts): keep it.
- Otherwise use a dedicated new number.

---

## 2) Choose provider: Meta Cloud API vs BSP (Interakt / Twilio / Gupshup)

### Option A — **Meta Cloud API** (recommended for ERP)
- Lowest cost
- Full control
- Best technical fit with your Bulk Messaging module (webhook + delivery attempts)

You will manage:
- API tokens
- Template creation
- Webhook setup
- Retries

### Option B — **BSP** (Interakt/Twilio/Gupshup)
Good if you want:
- Provider dashboard
- Easier onboarding assistance
- SLA/support

Tradeoffs:
- Additional costs / vendor lock-in

**Recommendation for this ERP:** Start with **Meta Cloud API**.

---

## 3) Meta onboarding: convert number to WhatsApp Business API sender

### Step 3.1 — Create / access Meta Business Manager
- Go to Meta Business Suite (Business Manager)
- Create a business if not created
- Fill:
  - Legal business name
  - Address
  - Website
  - Admin email + phone

### Step 3.2 — Business verification (strongly recommended)
- Submit documents (depends on country)
- This impacts:
  - Message limits
  - Trust/quality
  - Faster approvals

### Step 3.3 — Create a Meta Developer App
- Create an app in Meta Developers
- Add product: **WhatsApp**

### Step 3.4 — Create WhatsApp Business Account (WABA)
- In the WhatsApp product section:
  - Create/attach **WABA**
  - Add phone number `9441840511`

### Step 3.5 — Verify phone number via OTP
- Meta will send OTP to that number
- Complete verification

### Step 3.6 — Collect required IDs and secrets
You will get:
- **WABA ID**
- **Phone Number ID** (critical: used in API URL)
- **Permanent Access Token** (do not use temporary token in production)
- Optional: **App Secret** (for webhook signature verification)

---

## 4) Template creation & approval (required for Admin broadcast)

### Step 4.1 — Create message templates
In WhatsApp Manager (within Business Manager/WABA):
- Create templates in categories:
  - Utility (fee reminders, exam alerts)
  - Marketing (festival wishes)
  - Authentication (OTP – not needed here)

### Step 4.2 — Template naming guidelines
Use stable names that your ERP can reference:
- `festival_wish_1`
- `holiday_notice_general`
- `exam_alert_schedule`
- `fee_reminder_due`

### Step 4.3 — Parameter strategy
Templates use **ordered placeholders** `{{1}}`, `{{2}}`, etc.

Example (Fee):
- Name: `fee_reminder_due`
- Body: `Fee Reminder: ₹{{1}} due by {{2}}. Pay: {{3}}`

Your ERP must supply parameters in the exact order.

> Current MVP in this codebase sends only a single body parameter (maps message body to {{1}}).

---

## 5) Webhook setup for delivery tracking (SENT/DELIVERED/READ)

### Step 5.1 — Deploy backend publicly (Render)
You need a public URL:
- `https://<render-domain>/api/nethaji-service/whatsapp/webhook`

### Step 5.2 — Webhook verification endpoint
Meta calls your webhook with a GET to verify:
- `hub.mode=subscribe`
- `hub.verify_token=<your verify token>`
- `hub.challenge=<random>`

Your backend returns the `hub.challenge` if token matches.

### Step 5.3 — Subscribe to webhook fields
In Meta Developers:
- Subscribe to WhatsApp webhook events (messages + statuses)

### Step 5.4 — (Recommended) Enable signature verification
Meta can sign webhook payloads:
- Header: `X-Hub-Signature-256: sha256=<hmac>`

If you set the app secret in backend env vars, the backend will validate.

---

## 6) How ERP integrates (Admin → Backend → WhatsApp → Student)

### Targeting options supported
Your bulk messaging module already supports:
- ALL students
- By PROGRAM
- By YEAR
- SELECTED students

### Data model (already implemented)
- `MessageCampaign`
- `MessageRecipient` (now includes `whatsappStatus`)
- `MessageOutbox`
- `MessageDeliveryAttempt` (stores provider message id)

### Runtime flow
1. Admin selects **WHATSAPP** in ERP
2. Backend creates recipients + outbox jobs
3. Worker calls Meta Cloud API:
   - `POST /{phoneNumberId}/messages`
4. Cloud API returns `messages[0].id`
5. Webhook updates statuses:
   - `sent` → `SENT`
   - `delivered` → `DELIVERED`
   - `read` → `READ`
   - `failed` → `FAILED`

---

## 7) Render deployment configuration (required)

Set these environment variables in Render (Backend Service → Environment):

### WhatsApp Cloud API
- `whatsapp.phoneNumberId` = `<META_PHONE_NUMBER_ID>`
- `whatsapp.accessToken` = `<META_PERMANENT_ACCESS_TOKEN>`
- `whatsapp.graph.baseUrl` = `https://graph.facebook.com/v19.0`

### Webhook
- `whatsapp.webhook.verifyToken` = `<YOUR_RANDOM_VERIFY_TOKEN>`
- `whatsapp.webhook.appSecret` = `<META_APP_SECRET>` (optional but recommended)

Redeploy/restart after setting env vars.

---

## 8) Vercel deployment configuration (frontend)

Set in Vercel → Project → Environment Variables:

- `NEXT_PUBLIC_API_BASE_URL` = `https://<render-domain>/api/nethaji-service/`

This ensures the admin panel calls your Render backend.

---

## 9) Testing checklist (end-to-end)

### 9.1 Pre-flight
- Phone number is verified in WABA
- Template exists and is approved
- Backend env vars set on Render
- Webhook verified and subscribed

### 9.2 ERP test
- Login as admin
- Go to Bulk Messaging page
- Select channel: **WHATSAPP**
- Create a small test campaign to **SELECTED** students

### 9.3 Verify delivery
Use ERP endpoints:
- Campaign stats: `/messaging/campaigns/{id}/stats`
- Attempts: `/messaging/campaigns/{id}/attempts`

Confirm:
- Worker stored `providerMessageId`
- Webhook updates `SENT/DELIVERED/READ`

---

## 10) Common production issues & fixes

### Issue: Template rejected
- Fix wording (avoid spammy content)
- Make it clear it’s college-related
- Use Utility category for notices/reminders

### Issue: No delivery status updates
- Webhook URL not reachable publicly
- Verify token mismatch
- Webhook not subscribed to statuses

### Issue: Students don’t receive messages
- Recipient must be WhatsApp-enabled
- Number format must be correct (E.164)
- Messaging limit reached
- Template not approved

---

## 11) Current ERP code mapping (what to configure)

### WhatsApp send config keys
- `whatsapp.phoneNumberId`
- `whatsapp.accessToken`

### Webhook path
- `GET/POST /api/nethaji-service/whatsapp/webhook`

### WhatsApp status stored
- `message_recipient.whatsapp_status`

---

## 12) Next recommended hardening (not mandatory for MVP)

- Store WhatsApp template name separately from subject
- Support multiple template parameters and buttons
- Idempotent webhook processing (dedupe by status id)
- Better mapping of failed reason codes

