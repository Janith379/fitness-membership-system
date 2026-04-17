@echo off
title FitZone Gym - Application Launcher
color 0A

REM Fix JAVA_HOME if needed
if exist "C:\Program Files\Java\jdk-25.0.2" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-25.0.2"
) else if exist "C:\Program Files\Java\jdk-17" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-17"
) else if exist "C:\Program Files\Java\jdk-21" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-21"
) else if exist "C:\Program Files\Java\jdk-11" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-11"
) else (
    echo WARNING: Could not find Java JDK automatically
    echo Please set JAVA_HOME manually
)

echo JAVA_HOME is set to: %JAVA_HOME%
echo.

:START
cls
echo ========================================
echo    FitZone Gym Management System
echo ========================================
echo.
echo Starting application on http://localhost:8080
echo.
echo Press Ctrl+C to stop the application
echo ========================================
echo.

.\.maven\apache-maven-3.9.6\bin\mvn.cmd spring-boot:run

if errorlevel 1 (
    echo.
    echo ========================================
    echo ERROR: Application failed to start!
    echo Check the error messages above
    echo ========================================
    pause
    goto START
)

echo.
echo ========================================
echo Application stopped unexpectedly!
echo Restarting in 5 seconds...
echo Press Ctrl+C to cancel restart
echo ========================================
timeout /t 5
goto START
