# Fix Render Database Connection Error

## 🔴 Error: "The connection attempt failed" / "java.io.EOFException"

This error means your application cannot connect to the PostgreSQL database on Render.

---

## ✅ **Solution: Step-by-Step Fix**

### **Step 1: Get the Correct Database Connection String**

1. Go to your **Render Dashboard**
2. Click on your **PostgreSQL database** (nethaji-db)
3. Go to **"Info"** tab
4. Look for **"Internal Database URL"**

**Example Internal URL:**
```
postgres://nethaji_user:abc123def456@dpg-cr234567890-a/nethaji
```

### **Step 2: Convert to JDBC Format**

The Internal URL needs to be converted for Spring Boot:

**Original (from Render):**
```
postgres://nethaji_user:abc123def456@dpg-cr234567890-a/nethaji
```

**Convert to JDBC format:**
```
jdbc:postgresql://dpg-cr234567890-a/nethaji
```

**Extract the parts:**
- **Host:** `dpg-cr234567890-a` (everything between `@` and `/`)
- **Database:** `nethaji` (everything after last `/`)
- **Username:** `nethaji_user` (between `://` and `:`)
- **Password:** `abc123def456` (between `:` after username and `@`)

### **Step 3: Update Environment Variables in Render**

1. Go to your **Web Service** (nethaji-backend)
2. Click **"Environment"** in left sidebar
3. Update/Add these variables:

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-cr234567890-a/nethaji
SPRING_DATASOURCE_USERNAME=nethaji_user
SPRING_DATASOURCE_PASSWORD=abc123def456
PORT=9029
APP_AUTH_TOKEN_SECRET=change-this-to-random-secret
```

**⚠️ IMPORTANT:**
- Use `jdbc:postgresql://` NOT `postgres://`
- Do NOT include `:5432` port for internal connections
- Do NOT include `?` or query parameters
- Make sure there are NO extra spaces

### **Step 4: Verify Database is Ready**

1. Go to your **PostgreSQL database**
2. Check status shows **"Available"** (green)
3. If status is "Creating" or "Updating", wait 2-3 minutes

### **Step 5: Check Region Matching**

1. **Database region:** Click database → See region (e.g., "Singapore")
2. **Web service region:** Click web service → Settings → See region

**They MUST be the same!**

If different:
- Delete and recreate one to match the other
- Internal connections only work in same region

### **Step 6: Save and Redeploy**

1. After updating environment variables, click **"Save Changes"**
2. Render will automatically redeploy
3. Wait 5-10 minutes for deployment
4. Check **"Logs"** tab for any errors

---

## 🔍 **Verify Your Settings**

### **Correct Format Example:**

```bash
# ✅ CORRECT
SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-abc123-a/nethaji
SPRING_DATASOURCE_USERNAME=nethaji_user
SPRING_DATASOURCE_PASSWORD=mypassword123

# ❌ WRONG - Don't use postgres://
SPRING_DATASOURCE_URL=postgres://nethaji_user:pass@host/db

# ❌ WRONG - Don't include port for internal
SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-abc123-a:5432/nethaji

# ❌ WRONG - Don't use external hostname
SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-abc123.oregon-postgres.render.com/nethaji
```

---

## 🎯 **Quick Checklist**

- [ ] Using **Internal Database URL** (not External)
- [ ] URL starts with `jdbc:postgresql://`
- [ ] No port number (`:5432`) in URL
- [ ] Username and password are correct
- [ ] Database status is "Available"
- [ ] Database and web service in **same region**
- [ ] No extra spaces in environment variables
- [ ] Saved changes and redeployed

---

## 🔧 **Alternative: Use Render's Auto-Connect**

Instead of manually setting variables, let Render auto-connect:

1. Go to your **Web Service**
2. Click **"Environment"** tab
3. Scroll to **"Environment Variables"**
4. Click **"Add from Database"**
5. Select your database: `nethaji-db`
6. Render will automatically add:
   - `DATABASE_URL` (you'll need to convert this)

Then add one more environment variable:

```bash
SPRING_DATASOURCE_URL=${DATABASE_URL}
```

But you'll need to convert the format. Better to manually set as shown above.

---

## 📋 **Still Not Working?**

### **Check Logs:**

1. Go to web service → **"Logs"** tab
2. Look for the actual connection string being used
3. Check for any authentication errors

### **Test Database Connection:**

1. Go to database → **"Connect"** tab
2. Use the **psql** command shown
3. If you can't connect with psql, database might not be ready

### **Common Issues:**

**Issue:** "password authentication failed"
- **Fix:** Double-check password in environment variables

**Issue:** "Connection timeout"
- **Fix:** Ensure same region for database and web service

**Issue:** "Database does not exist"
- **Fix:** Check database name is exactly `nethaji`

---

## 🚀 **After Fixing**

Once connected successfully, you'll see in logs:

```
Hibernate: create table if not exists users...
Started NethajiApplication in X.XXX seconds
```

Then create admin user and test:

```bash
curl https://nethaji-backend.onrender.com/api/nethaji-service/health
```

---

## 📞 **Need More Help?**

1. Check Render's status: https://status.render.com
2. Render docs: https://render.com/docs/databases
3. Share your logs (remove sensitive data) for debugging
