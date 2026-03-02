@echo off
echo ====================================
echo   Vehicle Rental Platform Startup
echo ====================================
echo.

REM Set JAVA_HOME and Maven if not already in PATH
if exist "C:\Program Files\Java\jdk-22" set "JAVA_HOME=C:\Program Files\Java\jdk-22"
if exist "C:\apache-maven-3.9.6\bin" set "PATH=C:\apache-maven-3.9.6\bin;%PATH%"

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17 or higher
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Apache Maven
    pause
    exit /b 1
)

echo [1/2] Starting Backend Server (H2 Database - no external DB needed)...
echo.
cd backend

REM Start backend in a new window
start "Vehicle Rental Backend - Port 8888" cmd /k "set JAVA_HOME=C:\Program Files\Java\jdk-22 && set PATH=C:\apache-maven-3.9.6\bin;%PATH% && mvn spring-boot:run"

echo Backend is starting in a new window...
echo Please wait for the message: "Started VehicleRentalApplication"
echo Backend will be available at: http://localhost:8888
echo Swagger UI will be at: http://localhost:8888/swagger-ui.html
echo.
timeout /t 15 >nul

cd ..

echo [2/2] Opening Frontend...
echo.
start "" "frontend\index.html"

echo.
echo ====================================
echo   STARTUP COMPLETE
echo ====================================
echo.
echo Backend: http://localhost:8888
echo Swagger: http://localhost:8888/swagger-ui.html
echo H2 Console: http://localhost:8888/h2-console
echo Frontend: Opening in browser
echo.
echo Press Ctrl+C in the backend window to stop the server
echo.
pause
//ctrl+shift+B to start the application