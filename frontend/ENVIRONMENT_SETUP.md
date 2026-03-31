# Frontend Environment Configuration

## 🌐 **API Configuration**

Your frontend is now configured to connect to the live Render backend.

---

## 📝 **Current Setup**

The API configuration is in `src/services/api.js`:

```javascript
const environment = process.env.NEXT_PUBLIC_API_ENV || "dev";

const BASE_URL =
  API_BASE_URL_OVERRIDE ||
  (environment === "prod"
    ? "https://nethaji-backend.onrender.com/api/nethaji-service/"
    : "http://localhost:9029/api/nethaji-service/");
```

---

## 🔧 **Environment Modes**

### **Development Mode (Default)**
```bash
npm run dev
```
- Uses: `http://localhost:9029/api/nethaji-service/`
- For local backend testing

### **Production Mode**
```bash
NEXT_PUBLIC_API_ENV=prod npm run dev
```
- Uses: `https://nethaji-backend.onrender.com/api/nethaji-service/`
- Connects to live Render backend

---

## 🚀 **Quick Start**

### **Option 1: Use Live Backend (Recommended)**

Create a `.env.local` file in the frontend root:

```bash
NEXT_PUBLIC_API_ENV=prod
```

Then run:
```bash
npm run dev
```

Your frontend will now use the live Render backend!

---

### **Option 2: Override API URL**

Create `.env.local`:

```bash
NEXT_PUBLIC_API_BASE_URL=https://nethaji-backend.onrender.com/api/nethaji-service/
```

This overrides all other settings.

---

### **Option 3: Use Local Backend**

Don't create `.env.local` or set:

```bash
NEXT_PUBLIC_API_ENV=dev
```

Your frontend will use `http://localhost:9029`

---

## 🧪 **Test Login**

1. Start your frontend:
   ```bash
   npm run dev
   ```

2. Open: `http://localhost:3000/login`

3. Login with:
   - **Email:** `admin@nethaji.com`
   - **Password:** `admin123`

4. Check browser console - you should see API calls to:
   ```
   https://nethaji-backend.onrender.com/api/nethaji-service/auth/userLoginWithEmailPassword
   ```

---

## 📊 **Environment Variables Reference**

| Variable | Purpose | Example |
|----------|---------|---------|
| `NEXT_PUBLIC_API_ENV` | Set environment (dev/prod) | `prod` |
| `NEXT_PUBLIC_API_BASE_URL` | Override API URL | `https://nethaji-backend.onrender.com/api/nethaji-service/` |
| `NEXT_PUBLIC_API_TIMEOUT_MS` | API timeout in milliseconds | `90000` |

---

## 🔐 **CORS Already Configured**

Your backend already allows requests from:
- ✅ `http://localhost:3000` (development)
- ✅ `http://localhost:5173` (development)
- ✅ `https://www.nethajidcs.com` (production)
- ✅ `https://www.nethajidcs.edu.in` (production)

---

## 🌍 **Production Deployment**

When deploying your frontend to production (e.g., Vercel, Netlify):

Set environment variable:
```bash
NEXT_PUBLIC_API_ENV=prod
```

Or:
```bash
NEXT_PUBLIC_API_BASE_URL=https://nethaji-backend.onrender.com/api/nethaji-service/
```

---

## 🐛 **Troubleshooting**

### **CORS Error**
- ✅ Already fixed! Backend allows your frontend domains

### **Connection Timeout**
- First request to Render may take 30-60 seconds (cold start)
- Subsequent requests are fast

### **401 Unauthorized**
- Token expired - login again
- Check token is saved in sessionStorage/localStorage

### **Network Error**
- Check backend is running: `https://nethaji-backend.onrender.com/api/nethaji-service/health`
- Check your internet connection

---

## 📱 **API Endpoints Available**

**Base URL:** `https://nethaji-backend.onrender.com/api/nethaji-service`

**Authentication:**
- `POST /auth/userLoginWithEmailPassword` - Login
- `POST /auth/admin-or-principal/create-user` - Create user (admin only)
- `POST /auth/change-password` - Change password

**All other endpoints** - See `API_INTEGRATION_GUIDE.md`

---

## ✅ **Current Status**

- ✅ Backend deployed on Render
- ✅ Admin user created (admin@nethaji.com / admin123)
- ✅ CORS configured for frontend
- ✅ Frontend API URL updated to Render
- ✅ Ready to test!

---

**Next Step:** Restart your frontend and test login!

```bash
# Stop the current dev server (Ctrl+C)
# Then restart with production API
NEXT_PUBLIC_API_ENV=prod npm run dev
```
