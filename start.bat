@echo off
title FitZone Gym - Smart Launcher
color 0A

REM ========================================
REM Auto-detect and set JAVA_HOME
REM ========================================

if exist "C:\Program Files\Java\jdk-17" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-17"
    goto JAVA_FOUND
)

if exist "C:\Program Files\Java\jdk-21" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-21"
    goto JAVA_FOUND
)

if exist "C:\Program Files\Java\jdk-11" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-11"
    goto JAVA_FOUND
)

if exist "C:\Program Files\Eclipse Adoptium\jdk-17" (
    set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17"
    goto JAVA_FOUND
)

if exist "C:\Program Files\Eclipse Adoptium\jdk-21" (
    set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21"
    goto JAVA_FOUND
)

REM Check if java is in PATH
java -version >nul 2>&1
if %errorlevel%==0 (
    echo Java found in PATH
    goto JAVA_FOUND
)

REM Java not found
cls
echo ========================================
echo   ERROR: Java Not Found!
echo ========================================
echo.
echo Java JDK 17 or later is required to run this application.
echo.
echo Please:
echo 1. Install Java JDK from: https://adoptium.net/
echo 2. Run setup-java.bat to configure JAVA_HOME
echo 3. Or set JAVA_HOME manually in system environment variables
echo.
pause
exit /b 1

:JAVA_FOUND
echo Java found: %JAVA_HOME%
echo.

REM ========================================
REM Main Application Loop
REM ========================================

:START
cls
echo ========================================
echo    FitZone Gym Management System
echo ========================================
echo.
echo Java: %JAVA_HOME%
echo URL:  http://localhost:8080
echo.
echo Starting application...
echo Press Ctrl+C to stop
echo ========================================
echo.

REM Run the application
call mvnw.cmd spring-boot:run

REM Check exit code
if %errorlevel%==0 (
    echo.
    echo Application stopped normally.
    choice /C YN /M "Restart application"
    if errorlevel 2 exit /b 0
    goto START
) else (
    echo.
    echo ========================================
    echo   Application Error (Exit Code: %errorlevel%)
    echo ========================================
    echo.
    echo Common issues:
    echo - Port 8080 already in use (run stop-app.bat)
    echo - Missing dependencies (check pom.xml)
    echo - Java version mismatch (need JDK 17+)
    echo.
    choice /C YN /M "Try to restart"
    if errorlevel 2 exit /b 1
    timeout /t 3 /nobreak >nul
    goto START
)
