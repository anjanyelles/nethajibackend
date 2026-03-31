# Quick Deploy to Render - 5 Steps

## 🚀 Fast Track Deployment

### **Step 1: Push to GitHub** (2 minutes)

```bash
cd "/Users/anjanyelle/Desktop/nethaji full code/nethajibackend/nethajibackend"

git init
git add .
git commit -m "Deploy to Render"
git remote add origin https://github.com/YOUR_USERNAME/nethaji-backend.git
git push -u origin main
```

---

### **Step 2: Create Database on Render** (3 minutes)

1. Go to [render.com](https://render.com) → Sign up/Login
2. Click **New +** → **PostgreSQL**
3. Settings:
   - Name: `nethaji-db`
   - Database: `nethaji`
   - Region: **Singapore** (closest to India)
4. Click **Create Database**
5. **Copy the connection details** (you'll need them)

---

### **Step 3: Create Web Service** (5 minutes)

1. Click **New +** → **Web Service**
2. Connect your GitHub repo
3. Settings:
   - Name: `nethaji-backend`
   - Region: **Singapore** (same as database)
   - Build Command: `mvn clean install -DskipTests`
   - Start Command: `java -Dserver.port=$PORT -jar target/nethaji.jar`

---

### **Step 4: Add Environment Variables** (3 minutes)

In your web service, add these environment variables:

**From your database "Info" tab, copy:**

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-xxxxx-xxx.singapore-postgres.render.com/nethaji
SPRING_DATASOURCE_USERNAME=nethaji_user
SPRING_DATASOURCE_PASSWORD=<your_db_password>
```

**Add these:**

```bash
PORT=9029
APP_AUTH_TOKEN_SECRET=change-this-to-a-random-secret-key-for-production
```

Click **Save Changes**

---

### **Step 5: Create Admin User** (2 minutes)

After deployment completes, run this script:

```bash
./create_admin_render.sh
```

Enter your Render database details when prompted.

**Or manually connect:**

```bash
# Get connection string from Render database → Connect → External Connection
psql "postgresql://nethaji_user:PASSWORD@dpg-xxxxx.singapore-postgres.render.com/nethaji"

# Then run:
java -cp "src/main/java:target/classes:$HOME/.m2/repository/org/postgresql/postgresql/42.7.5/postgresql-42.7.5.jar" com.nethaji.util.CreateAdminUser
```

---

## ✅ Test Your Deployment

Your API will be at: `https://nethaji-backend.onrender.com`

```bash
# Health check
curl https://nethaji-backend.onrender.com/api/nethaji-service/health

# Admin login
curl -X POST https://nethaji-backend.onrender.com/api/nethaji-service/auth/userLoginWithEmailPassword \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@nethaji.com", "password": "admin123"}'
```

---

## 📝 Important Notes

1. **First deploy takes 5-10 minutes** - be patient
2. **Free tier spins down after 15 min** - first request will be slow
3. **Upgrade to $7/month** for always-on service
4. **Auto-deploys** on every git push to main branch

---

## 🔗 Useful Links

- **Render Dashboard:** https://dashboard.render.com
- **Your App:** https://nethaji-backend.onrender.com
- **Logs:** Dashboard → Your Service → Logs tab
- **Full Guide:** See `RENDER_DEPLOYMENT.md`

---

## 🆘 Troubleshooting

**Build fails?**
- Check Java version is 17 in Render settings
- Verify `pom.xml` is in repository root

**Can't connect to database?**
- Ensure database and web service are in same region
- Double-check environment variables
- Use **Internal Database URL** from Render

**App won't start?**
- Check logs in Render dashboard
- Verify start command includes `$PORT`
- Ensure all required env vars are set

---

**Total Time: ~15 minutes** ⏱️
