@echo off

REM Fix JAVA_HOME if needed
if exist "C:\Program Files\Java\jdk-17" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-17"
) else if exist "C:\Program Files\Java\jdk-21" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-21"
) else if exist "C:\Program Files\Java\jdk-11" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-11"
)

echo Starting FitZone Gym Application...
start "FitZone Backend" cmd /k mvnw.cmd spring-boot:run
echo Application started in a new window!
echo You can close this window. The app will keep running.
pause
