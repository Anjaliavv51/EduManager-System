# Quick Start Guide - EduManager System

## 🚀 Fastest Way to Run (Docker)

```bash
# 1. Clone the repository
git clone https://github.com/Anjaliavv51/EduManager-System.git
cd EduManager-System

# 2. Start everything with one command
./start.sh

# 3. Open your browser
# Frontend: http://localhost:3000
# Backend:  http://localhost:8080/api
```

That's it! The entire system is now running with:
- MySQL database (pre-populated with sample data)
- Spring Boot backend (REST APIs)
- React frontend (full UI with Students, Courses, Enrollments)

## 🛑 Stop the System

```bash
./stop.sh
```

---

## 💻 Local Development (Without Docker)

### One-Time Setup

**1. Install Prerequisites:**
```bash
# Install Java 11
sudo apt install openjdk-11-jdk  # Ubuntu/Debian
brew install openjdk@11          # macOS

# Install Node.js 18
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt install nodejs           # Ubuntu/Debian
brew install node@18              # macOS

# Install MySQL 8
sudo apt install mysql-server     # Ubuntu/Debian
brew install mysql                # macOS
```

**2. Setup Database:**
```bash
# Start MySQL
sudo systemctl start mysql        # Linux
brew services start mysql         # macOS

# Create database
mysql -u root -p
```

In MySQL:
```sql
CREATE DATABASE edumanager;
CREATE USER 'edumanager'@'localhost' IDENTIFIED BY 'edumanager123';
GRANT ALL PRIVILEGES ON edumanager.* TO 'edumanager'@'localhost';
EXIT;
```

Load schema:
```bash
mysql -u edumanager -p edumanager < database/schema.sql
# Password: edumanager123
```

### Run Backend

```bash
# From project root
mvn spring-boot:run
```

Backend runs on: http://localhost:8080

### Run Frontend

```bash
# From project root
cd frontend
npm install
npm start
```

Frontend runs on: http://localhost:3000

---

## 🌐 Deploy to Cloud (Get Public URL)

### Option 1: Railway (Recommended - Free Tier)

1. Go to https://railway.app
2. Sign up with GitHub
3. Click "New Project" → "Deploy from GitHub"
4. Select your EduManager-System repository
5. Railway automatically detects and deploys your app
6. Get public URL: `https://your-app.up.railway.app`

### Option 2: Render (Free Tier)

1. Go to https://render.com
2. Sign up with GitHub
3. Create "Web Service" → Connect repository
4. Render builds and deploys automatically
5. Get public URL: `https://edumanager.onrender.com`

### Option 3: Heroku (Popular Platform)

```bash
# Install Heroku CLI
curl https://cli-assets.heroku.com/install.sh | sh

# Login
heroku login

# Create app
heroku create edumanager-app

# Add MySQL
heroku addons:create jawsdb:kitefin

# Deploy
git push heroku main

# Get URL
heroku open
```

---

## 📱 What You Can Do in the UI

### Students Tab
- ✅ View all students
- ✅ Search students by name (live search)
- ✅ Delete students

### Courses Tab
- ✅ View all courses
- ✅ Search courses by code/name/department
- ✅ Add new courses
- ✅ See enrollment capacity (e.g., 5/30 seats)
- ✅ Delete courses

### Enrollments Tab
- ✅ View all enrollments
- ✅ Enroll students in courses
- ✅ Drop enrollments
- ✅ See enrollment status and grades

---

## 🔧 Verify Everything Works

```bash
# Check backend
curl http://localhost:8080/api/students

# Check database
mysql -u edumanager -p -e "USE edumanager; SELECT COUNT(*) FROM students;"

# Check frontend
# Open http://localhost:3000 in browser
```

---

## 📊 Sample API Requests

```bash
# Get all students
curl http://localhost:8080/api/students

# Search students
curl http://localhost:8080/api/students/search?q=john

# Get all courses
curl http://localhost:8080/api/courses

# Get available courses
curl http://localhost:8080/api/courses/available

# Enroll student
curl -X POST "http://localhost:8080/api/enrollments?studentId=1&courseId=2"
```

---

## ❓ Troubleshooting

**Backend won't start?**
```bash
# Check if port 8080 is in use
lsof -i :8080
# Kill the process if needed
kill -9 <PID>
```

**Frontend won't start?**
```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json
npm install
```

**Database connection error?**
```bash
# Verify MySQL is running
sudo systemctl status mysql  # Linux
brew services list           # macOS

# Test connection
mysql -u edumanager -p
```

**Docker issues?**
```bash
# Clean everything and restart
docker-compose down
docker system prune -a
./start.sh
```

---

## 📚 More Resources

- **Full Documentation**: See README.md
- **API Endpoints**: See docs/API_DOCUMENTATION.md
- **Postman Collection**: Import docs/EduManager-API.postman_collection.json
- **Implementation Details**: See docs/IMPLEMENTATION_SUMMARY.md

---

## 🎯 Summary

**Fastest Method**: `./start.sh` → Open http://localhost:3000

**Public Deployment**: Deploy to Railway/Render for free public URL

**Full Features**: Students + Courses + Enrollments management with search
