@echo off
echo ========================================
echo   Java Environment Setup
echo ========================================
echo.

REM Check current JAVA_HOME
echo Current JAVA_HOME: %JAVA_HOME%
echo.

REM Search for Java installations
echo Searching for Java installations...
echo.

set FOUND_JAVA=0

REM Check common Java installation paths
if exist "C:\Program Files\Java\jdk-17" (
    echo [FOUND] JDK 17 at: C:\Program Files\Java\jdk-17
    set "JAVA_HOME=C:\Program Files\Java\jdk-17"
    set FOUND_JAVA=1
)

if exist "C:\Program Files\Java\jdk-21" (
    echo [FOUND] JDK 21 at: C:\Program Files\Java\jdk-21
    set "JAVA_HOME=C:\Program Files\Java\jdk-21"
    set FOUND_JAVA=1
)

if exist "C:\Program Files\Java\jdk-11" (
    echo [FOUND] JDK 11 at: C:\Program Files\Java\jdk-11
    set "JAVA_HOME=C:\Program Files\Java\jdk-11"
    set FOUND_JAVA=1
)

if exist "C:\Program Files\Eclipse Adoptium\jdk-17" (
    echo [FOUND] Eclipse Adoptium JDK 17 at: C:\Program Files\Eclipse Adoptium\jdk-17
    set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17"
    set FOUND_JAVA=1
)

if exist "C:\Program Files\Eclipse Adoptium\jdk-21" (
    echo [FOUND] Eclipse Adoptium JDK 21 at: C:\Program Files\Eclipse Adoptium\jdk-21
    set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21"
    set FOUND_JAVA=1
)

echo.
if %FOUND_JAVA%==1 (
    echo ========================================
    echo SUCCESS: Java found!
    echo JAVA_HOME set to: %JAVA_HOME%
    echo ========================================
    echo.
    echo Testing Java version...
    "%JAVA_HOME%\bin\java" -version
    echo.
    echo To make this permanent, add this to your system environment variables:
    echo JAVA_HOME=%JAVA_HOME%
) else (
    echo ========================================
    echo ERROR: No Java installation found!
    echo ========================================
    echo.
    echo Please install Java JDK 17 or later from:
    echo https://adoptium.net/
    echo.
    echo Or set JAVA_HOME manually to your Java installation directory
)

echo.
pause
