@echo off
title FitZone Gym - Launcher Menu
color 0A

:MENU
cls
echo.
echo ╔════════════════════════════════════════════════════════════════╗
echo ║                                                                ║
echo ║          FITZONE GYM MANAGEMENT SYSTEM                         ║
echo ║                  Launcher Menu                                 ║
echo ║                                                                ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.
echo   [1] Start Application (Smart Launcher)
echo   [2] Start with Auto-Restart
echo   [3] Start in Background
echo   [4] Stop Application
echo   [5] Restart Application
echo.
echo   [6] Setup Java Environment
echo   [7] Check System Diagnostics
echo   [8] First Time Setup Wizard
echo.
echo   [9] View Quick Reference
echo   [0] Exit
echo.
echo ════════════════════════════════════════════════════════════════
echo.

choice /C 1234567890 /N /M "Select an option (1-9, 0 to exit): "

if errorlevel 10 goto EXIT
if errorlevel 9 goto QUICK_REF
if errorlevel 8 goto FIRST_TIME
if errorlevel 7 goto CHECK_SYS
if errorlevel 6 goto SETUP_JAVA
if errorlevel 5 goto RESTART
if errorlevel 4 goto STOP
if errorlevel 3 goto START_BG
if errorlevel 2 goto START_AUTO
if errorlevel 1 goto START

:START
cls
echo Starting application with Smart Launcher...
echo.
call start.bat
goto MENU

:START_AUTO
cls
echo Starting application with Auto-Restart...
echo.
call run-continuous.bat
goto MENU

:START_BG
cls
echo Starting application in Background...
echo.
call start-app.bat
timeout /t 3
goto MENU

:STOP
cls
echo Stopping application...
echo.
call stop-app.bat
echo.
pause
goto MENU

:RESTART
cls
echo Restarting application...
echo.
call restart-app.bat
goto MENU

:SETUP_JAVA
cls
call setup-java.bat
goto MENU

:CHECK_SYS
cls
call check-system.bat
goto MENU

:FIRST_TIME
cls
call first-time-setup.bat
goto MENU

:QUICK_REF
cls
type QUICK-REFERENCE.txt
echo.
echo.
pause
goto MENU

:EXIT
cls
echo.
echo Thank you for using FitZone Gym Management System!
echo.
timeout /t 2 /nobreak >nul
exit /b 0
