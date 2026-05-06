# FitZone Gym Management System

## 🚀 Quick Start

1. **Double-click `start.bat`**
2. Wait for the application to start
3. Open your browser: **http://localhost:8080**

That's it! The application will run continuously.

---

## 📋 Prerequisites

- **Java JDK 17 or later** (Download from: https://adoptium.net/)
- **Windows OS**
- **Port 8080** must be available

---

## 🛠️ Setup & Run

### First Time Setup

1. **Check your system:**
   ```
   Double-click: check-system.bat
   ```
   This will verify Java installation and system requirements.

2. **If Java is not found:**
   ```
   Double-click: setup-java.bat
   ```
   Follow the instructions to install/configure Java.

3. **Start the application:**
   ```
   Double-click: start.bat
   ```

### Running the Application

Choose one of these methods:

#### Method 1: Smart Launcher (Recommended)
```
start.bat
```
- Auto-detects Java
- Handles errors
- Offers restart options

#### Method 2: Continuous Runner
```
run-continuous.bat
```
- Auto-restarts on crash
- Runs until Ctrl+C

#### Method 3: Background Mode
```
start-app.bat
```
- Runs in separate window
- Close window to stop

---

## 🔧 Utility Scripts

| Script | Purpose |
|--------|---------|
| `start.bat` | Smart launcher with auto Java detection |
| `run-continuous.bat` | Run with auto-restart |
| `start-app.bat` | Run in background window |
| `stop-app.bat` | Stop running application |
| `restart-app.bat` | Restart application |
| `setup-java.bat` | Detect and configure Java |
| `check-system.bat` | System diagnostics |

---

## ❌ Troubleshooting

### Problem: "Java Not Found"

**Solution:**
1. Run `setup-java.bat`
2. If Java is not installed:
   - Download from: https://adoptium.net/
   - Install Java JDK 17 or later
   - Run `setup-java.bat` again

### Problem: "Port 8080 Already in Use"

**Solution:**
1. Run `stop-app.bat`
2. Or change port in `src/main/resources/application.properties`:
   ```properties
   server.port=8081
   ```

### Problem: "JAVA_HOME is set to an invalid directory"

**Solution:**
1. Run `setup-java.bat`
2. Note the correct Java path
3. Set JAVA_HOME permanently:
   - Right-click "This PC" → Properties
   - Advanced System Settings → Environment Variables
   - Add/Edit JAVA_HOME with the correct path

### Problem: Application Crashes or Won't Start

**Solution:**
1. Run `check-system.bat` to diagnose
2. Check Java version: `java -version` (must be 17+)
3. Clean and rebuild:
   ```cmd
   mvnw.cmd clean install
   ```
4. Check console for error messages

---

## 📱 Using the Application

Once started, access the application at: **http://localhost:8080**

### Features:
- Member Registration
- Payment Processing
- Member Management (View, Edit, Delete)
- Member Search
- Payment History

---

## 🛑 Stopping the Application

### If running in console:
- Press `Ctrl+C`

### If running in background:
- Run `stop-app.bat`
- Or close the application window

---

## 📂 Project Structure

```
FitZone GYM/
├── src/main/
│   ├── java/com/fitzone/
│   │   ├── controller/     # Web controllers
│   │   ├── model/          # Data models
│   │   ├── service/        # Business logic
│   │   ├── repository/     # Data access
│   │   └── util/           # Utilities
│   └── resources/
│       ├── data/           # CSV data files
│       ├── static/         # CSS, JS
│       ├── templates/      # HTML pages
│       └── application.properties
├── start.bat               # Smart launcher
├── run-continuous.bat      # Auto-restart runner
├── stop-app.bat           # Stop script
└── check-system.bat       # Diagnostics

```

---

## 💡 Tips

1. **Keep the console window open** while the application is running
2. **Check the console** for error messages if something goes wrong
3. **Use `check-system.bat`** before reporting issues
4. **The application saves data** to CSV files in `src/main/resources/data/`

---

## 🆘 Need Help?

1. Run `check-system.bat` for diagnostics
2. Check the console output for error messages
3. Review the troubleshooting section above
4. Ensure Java JDK 17+ is properly installed

---

## 📝 Manual Commands

If you prefer command line:

```cmd
# Start application
mvnw.cmd spring-boot:run

# Clean and build
mvnw.cmd clean install

# Run tests
mvnw.cmd test
```

---

**Enjoy using FitZone Gym Management System! 💪**
