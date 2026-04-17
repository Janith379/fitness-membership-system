@echo off
title FitZone Gym - System Diagnostics
color 0B

echo ========================================
echo   FitZone Gym - System Diagnostics
echo ========================================
echo.

REM Check Java
echo [1/4] Checking Java...
java -version 2>&1 | findstr /i "version"
if %errorlevel%==0 (
    echo [OK] Java is installed
) else (
    echo [ERROR] Java not found in PATH
)
echo.

REM Check JAVA_HOME
echo [2/4] Checking JAVA_HOME...
if defined JAVA_HOME (
    echo JAVA_HOME = %JAVA_HOME%
    if exist "%JAVA_HOME%\bin\java.exe" (
        echo [OK] JAVA_HOME is valid
    ) else (
        echo [ERROR] JAVA_HOME points to invalid directory
    )
) else (
    echo [WARNING] JAVA_HOME not set
)
echo.

REM Check Port 8080
echo [3/4] Checking Port 8080...
netstat -ano | findstr ":8080" | findstr "LISTENING" >nul
if %errorlevel%==0 (
    echo [WARNING] Port 8080 is already in use
    echo Run stop-app.bat to free the port
) else (
    echo [OK] Port 8080 is available
)
echo.

REM Check Project Files
echo [4/4] Checking Project Files...
if exist "pom.xml" (
    echo [OK] pom.xml found
) else (
    echo [ERROR] pom.xml not found
)

if exist "mvnw.cmd" (
    echo [OK] mvnw.cmd found
) else (
    echo [ERROR] mvnw.cmd not found
)

if exist "src\main\java\com\fitzone\FitZoneApplication.java" (
    echo [OK] Main application class found
) else (
    echo [ERROR] Main application class not found
)
echo.

echo ========================================
echo   Diagnostic Complete
echo ========================================
echo.
echo If you see any errors above, please fix them before running the application.
echo.
pause
