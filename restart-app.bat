@echo off
echo Restarting FitZone Gym Application...
call stop-app.bat
timeout /t 2 /nobreak >nul
call start-app.bat
