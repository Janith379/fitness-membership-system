@echo off
title FitZone Gym - First Time Setup Wizard
color 0E

cls
echo.
echo ╔════════════════════════════════════════════════════════════════╗
echo ║                                                                ║
echo ║          FITZONE GYM - FIRST TIME SETUP WIZARD                 ║
echo ║                                                                ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.
echo This wizard will help you set up and run FitZone Gym for the first time.
echo.
pause

REM Step 1: Check Java
cls
echo.
echo ════════════════════════════════════════════════════════════════
echo   STEP 1/3: Checking Java Installation
echo ════════════════════════════════════════════════════════════════
echo.

java -version >nul 2>&1
if %errorlevel%==0 (
    echo [✓] Java is installed!
    echo.
    java -version 2>&1
    echo.
    goto STEP2
) else (
    echo [✗] Java is NOT installed!
    echo.
    echo You need Java JDK 17 or later to run this application.
    echo.
    echo Please:
    echo   1. Visit: https://adoptium.net/
    echo   2. Download Java JDK 17 or later
    echo   3. Install it
    echo   4. Run this wizard again
    echo.
    pause
    exit /b 1
)

:STEP2
pause

REM Step 2: Check Port
cls
echo.
echo ════════════════════════════════════════════════════════════════
echo   STEP 2/3: Checking Port Availability
echo ════════════════════════════════════════════════════════════════
echo.

netstat -ano | findstr ":8080" | findstr "LISTENING" >nul
if %errorlevel%==0 (
    echo [!] Port 8080 is already in use!
    echo.
    choice /C YN /M "Do you want to stop the application using port 8080"
    if errorlevel 2 (
        echo.
        echo Please free port 8080 manually or change the port in:
        echo src\main\resources\application.properties
        echo.
        pause
        exit /b 1
    )
    call stop-app.bat
    timeout /t 2 /nobreak >nul
) else (
    echo [✓] Port 8080 is available!
)
echo.
pause

REM Step 3: Start Application
cls
echo.
echo ════════════════════════════════════════════════════════════════
echo   STEP 3/3: Starting Application
echo ════════════════════════════════════════════════════════════════
echo.
echo The application will now start...
echo.
echo Once you see "Started FitZoneApplication", open your browser to:
echo.
echo     http://localhost:8080
echo.
echo Press any key to start the application...
pause >nul

cls
echo.
echo ════════════════════════════════════════════════════════════════
echo   Starting FitZone Gym Management System...
echo ════════════════════════════════════════════════════════════════
echo.
echo URL: http://localhost:8080
echo.
echo Keep this window open while using the application.
echo Press Ctrl+C to stop the application.
echo.
echo ════════════════════════════════════════════════════════════════
echo.

REM Auto-detect Java
if exist "C:\Program Files\Java\jdk-17" set "JAVA_HOME=C:\Program Files\Java\jdk-17"
if exist "C:\Program Files\Java\jdk-21" set "JAVA_HOME=C:\Program Files\Java\jdk-21"
if exist "C:\Program Files\Eclipse Adoptium\jdk-17" set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17"
if exist "C:\Program Files\Eclipse Adoptium\jdk-21" set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21"

call mvnw.cmd spring-boot:run

echo.
echo ════════════════════════════════════════════════════════════════
echo   Application Stopped
echo ════════════════════════════════════════════════════════════════
echo.
echo To run the application again, use: start.bat
echo.
pause
