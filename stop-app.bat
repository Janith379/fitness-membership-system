@echo off
echo Stopping FitZone Gym Application...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8080" ^| find "LISTENING"') do taskkill /F /PID %%a
echo Application stopped!
pause
