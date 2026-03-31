# Render Deployment Guide - Nethaji Backend

Complete guide to deploy the Nethaji College Management System backend to Render.

---

## 📋 Prerequisites

1. **Render Account** - Sign up at [render.com](https://render.com)
2. **GitHub Account** - Your code should be in a GitHub repository
3. **Git** - Installed on your local machine

---

## 🚀 Step-by-Step Deployment

### **Step 1: Push Code to GitHub**

```bash
cd "/Users/anjanyelle/Desktop/nethaji full code/nethajibackend/nethajibackend"

# Initialize git if not already done
git init

# Add all files
git add .

# Commit
git commit -m "Initial commit - Nethaji backend"

# Add your GitHub repository as remote
git remote add origin https://github.com/YOUR_USERNAME/nethaji-backend.git

# Push to GitHub
git push -u origin main
```

---

### **Step 2: Create PostgreSQL Database on Render**

1. Go to [Render Dashboard](https://dashboard.render.com/)
2. Click **"New +"** → **"PostgreSQL"**
3. Configure:
   - **Name:** `nethaji-db`
   - **Database:** `nethaji`
   - **User:** `nethaji_user`
   - **Region:** Choose closest to your users (e.g., Singapore for India)
   - **Plan:** Free or Starter
4. Click **"Create Database"**
5. **Save the connection details** (you'll need them later)

---

### **Step 3: Create Web Service on Render**

1. Click **"New +"** → **"Web Service"**
2. Connect your GitHub repository
3. Configure:
   - **Name:** `nethaji-backend`
   - **Region:** Same as database
   - **Branch:** `main`
   - **Root Directory:** Leave empty (or specify if in subdirectory)
   - **Runtime:** `Java`
   - **Build Command:**
     ```bash
     mvn clean install -DskipTests
     ```
   - **Start Command:**
     ```bash
     java -Dserver.port=$PORT -jar target/nethaji.jar
     ```

---

### **Step 4: Configure Environment Variables**

In the Render web service settings, add these environment variables:

#### **Database Configuration**

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://<RENDER_DB_HOST>/<DATABASE_NAME>
SPRING_DATASOURCE_USERNAME=<DB_USERNAME>
SPRING_DATASOURCE_PASSWORD=<DB_PASSWORD>
```

**Get these values from your Render PostgreSQL database:**
- Go to your database → **"Info"** tab
- Copy the **Internal Database URL** and extract:
  - Host: `dpg-xxxxx.oregon-postgres.render.com`
  - Database: `nethaji`
  - Username: `nethaji_user`
  - Password: (shown in database info)

**Example:**
```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-abc123.oregon-postgres.render.com/nethaji
SPRING_DATASOURCE_USERNAME=nethaji_user
SPRING_DATASOURCE_PASSWORD=your_db_password_here
```

#### **Application Configuration**

```bash
PORT=9029
APP_AUTH_TOKEN_SECRET=your-super-secret-jwt-key-change-this-in-production
```

#### **AWS Configuration (Optional - for email features)**

```bash
AWS_ACCESS_KEY_ID=your_aws_access_key
AWS_SECRET_ACCESS_KEY=your_aws_secret_key
```

#### **Two Factor API (Optional)**

```bash
TWO_FACTOR_API_KEY=your_2factor_api_key
```

---

### **Step 5: Deploy**

1. Click **"Create Web Service"**
2. Render will automatically:
   - Clone your repository
   - Run the build command
   - Start your application
3. Wait for deployment (5-10 minutes for first deploy)
4. Your app will be available at: `https://nethaji-backend.onrender.com`

---

### **Step 6: Create Admin User on Render Database**

After deployment, you need to create the admin user in the Render database.

#### **Option A: Using Render Shell**

1. Go to your web service → **"Shell"** tab
2. Run:
   ```bash
   cd /opt/render/project/src
   java -cp "src/main/java:target/classes:/opt/render/project/.m2/repository/org/postgresql/postgresql/42.7.5/postgresql-42.7.5.jar" com.nethaji.util.CreateAdminUser
   ```

#### **Option B: Using psql from your local machine**

1. Get database connection string from Render
2. Install psql locally if not already installed
3. Connect and create admin:

```bash
# Connect to Render database
psql "postgresql://nethaji_user:PASSWORD@dpg-xxxxx.oregon-postgres.render.com/nethaji"

# Then run the SQL to create admin (you'll need to hash the password first)
```

#### **Option C: Create a migration endpoint (Recommended)**

Add a one-time setup endpoint in your application that creates the admin user when called.

---

### **Step 7: Verify Deployment**

Test your deployed API:

```bash
# Health check
curl https://nethaji-backend.onrender.com/api/nethaji-service/health

# Admin login
curl -X POST https://nethaji-backend.onrender.com/api/nethaji-service/auth/userLoginWithEmailPassword \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@nethaji.com",
    "password": "admin123"
  }'
```

---

## 🔧 Important Configuration Changes

### **Update application.yml for Production**

The current configuration uses environment variables, which is perfect for Render:

```yaml
datasource:
  url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/nethaji}
  username: ${SPRING_DATASOURCE_USERNAME:nethaji_user}
  password: ${SPRING_DATASOURCE_PASSWORD:password}
```

This means:
- ✅ **On Render:** Uses environment variables you set
- ✅ **Locally:** Uses default localhost values

---

## 📊 Monitoring & Logs

### **View Logs**
1. Go to your web service
2. Click **"Logs"** tab
3. Monitor application startup and errors

### **View Metrics**
1. Click **"Metrics"** tab
2. Monitor CPU, Memory, Request count

---

## 🔄 Continuous Deployment

Render automatically redeploys when you push to GitHub:

```bash
# Make changes to your code
git add .
git commit -m "Update feature"
git push origin main

# Render will automatically detect and redeploy
```

---

## 💰 Pricing

### **Free Tier Limitations**
- Web service spins down after 15 minutes of inactivity
- First request after spin-down takes 30-60 seconds (cold start)
- 750 hours/month free

### **Starter Plan ($7/month)**
- Always on (no cold starts)
- Better performance
- Recommended for production

### **Database**
- Free tier: 90 days, then $7/month
- Includes 1GB storage

---

## 🐛 Troubleshooting

### **Build Fails**

**Issue:** Maven build fails

**Solution:**
- Check build logs
- Ensure `pom.xml` is in root directory
- Verify Java version in Render settings (should be Java 17)

### **Database Connection Fails**

**Issue:** `Connection refused` or `Authentication failed`

**Solution:**
- Verify environment variables are set correctly
- Check database is in same region as web service
- Ensure database URL includes port (usually 5432)

### **Application Won't Start**

**Issue:** App crashes on startup

**Solution:**
- Check logs for specific error
- Verify `PORT` environment variable is set
- Ensure start command uses `$PORT`: `java -Dserver.port=$PORT -jar target/nethaji.jar`

### **Cold Start Issues**

**Issue:** First request takes too long

**Solution:**
- Upgrade to Starter plan ($7/month) for always-on service
- Or implement a cron job to ping your service every 10 minutes

---

## 🔐 Security Best Practices

1. **Change Default Passwords**
   - Update admin password after first login
   - Use strong JWT secret

2. **Environment Variables**
   - Never commit secrets to GitHub
   - Use Render's environment variable feature

3. **Database Security**
   - Use Render's internal database URL when possible
   - Restrict database access to your web service only

4. **HTTPS**
   - Render provides free SSL certificates
   - All traffic is automatically encrypted

---

## 📝 Post-Deployment Checklist

- [ ] Database created and connected
- [ ] Environment variables configured
- [ ] Application deployed successfully
- [ ] Health check endpoint responding
- [ ] Admin user created
- [ ] Admin login working
- [ ] Test creating a student user
- [ ] Test creating a lecturer user
- [ ] Configure custom domain (optional)
- [ ] Set up monitoring/alerts

---

## 🌐 Custom Domain (Optional)

1. Go to your web service → **"Settings"**
2. Scroll to **"Custom Domain"**
3. Add your domain (e.g., `api.nethaji.edu`)
4. Update DNS records as instructed
5. Render will automatically provision SSL certificate

---

## 📞 Support

- **Render Docs:** [docs.render.com](https://docs.render.com)
- **Render Community:** [community.render.com](https://community.render.com)
- **Status Page:** [status.render.com](https://status.render.com)

---

## 🎯 Quick Commands Reference

```bash
# Local development
mvn spring-boot:run

# Build for production
mvn clean install -DskipTests

# Run JAR locally
java -jar target/nethaji.jar

# Connect to Render database
psql "postgresql://user:pass@host/database"

# View Render logs
render logs -s nethaji-backend

# Restart service
render restart -s nethaji-backend
```

---

**Your Render deployment URL will be:**
`https://nethaji-backend.onrender.com`

**API Base URL:**
`https://nethaji-backend.onrender.com/api/nethaji-service`
