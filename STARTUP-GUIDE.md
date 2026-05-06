# FitZone Gym - Startup Scripts

## Quick Start (RECOMMENDED)

1. **Double-click `start.bat`** - Smart launcher with auto Java detection
2. Wait for "Started FitZoneApplication" message
3. Open browser: http://localhost:8080

## Available Scripts

### 1. start.bat ⭐ (RECOMMENDED)
Smart launcher with automatic Java detection and error handling.
- Auto-detects Java installation
- Handles errors gracefully
- Offers restart on failure
- Shows helpful error messages

### 2. setup-java.bat
Detects and configures Java environment.
- Finds Java installations
- Shows Java version
- Provides setup instructions

### 3. run-continuous.bat
Runs with auto-restart on crash.
- Auto-restarts if app crashes
- Keeps running until Ctrl+C

### 4. start-app.bat
Starts in separate background window.
- Runs in new window
- Close window to stop

### 5. stop-app.bat
Stops any running instance on port 8080.

### 6. restart-app.bat
Stops and restarts the application.

## Troubleshooting

### Java Not Found Error
1. Run `setup-java.bat` to detect Java
2. If not found, install Java JDK 17+ from: https://adoptium.net/
3. After installation, run `setup-java.bat` again

### Port 8080 Already in Use
1. Run `stop-app.bat`
2. Or change port in `src/main/resources/application.properties`

### Application Won't Start
1. Check Java version: `java -version` (need 17+)
2. Clean and rebuild: `mvnw.cmd clean install`
3. Check error messages in console

### JAVA_HOME Error
If you see "JAVA_HOME is set to an invalid directory":
1. Run `setup-java.bat`
2. Follow the instructions to set JAVA_HOME permanently

## Manual Start

```cmd
mvnw.cmd spring-boot:run
```

## System Requirements

- Java JDK 17 or later
- Windows OS
- Port 8080 available
