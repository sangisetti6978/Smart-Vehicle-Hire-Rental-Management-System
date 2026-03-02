# Vehicle Rental Platform Startup Script for PowerShell

Write-Host "====================================" -ForegroundColor Cyan
Write-Host "  Vehicle Rental Platform Startup" -ForegroundColor Cyan
Write-Host "====================================" -ForegroundColor Cyan
Write-Host ""

# Check if Java is installed
try {
    $javaVersion = java -version 2>&1 | Select-String "version"
    Write-Host "[✓] Java is installed: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "[✗] ERROR: Java is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Java 17 or higher" -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

# Check if Maven is installed
try {
    $mavenVersion = mvn -version 2>&1 | Select-String "Apache Maven"
    Write-Host "[✓] Maven is installed: $mavenVersion" -ForegroundColor Green
} catch {
    Write-Host "[✗] ERROR: Maven is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Apache Maven" -ForegroundColor Yellow
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "[1/3] Checking Oracle Database connection..." -ForegroundColor Yellow
Write-Host "Make sure Oracle Database is running on localhost:1521" -ForegroundColor Gray
Write-Host "Database: XE, User: vehiclerental, Password: password123" -ForegroundColor Gray
Write-Host ""
Start-Sleep -Seconds 2

Write-Host "[2/3] Starting Backend Server..." -ForegroundColor Yellow
Write-Host ""

# Start backend in a new PowerShell window
$backendPath = Join-Path $PSScriptRoot "backend"
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$backendPath'; Write-Host 'Starting Vehicle Rental Backend...' -ForegroundColor Cyan; mvn spring-boot:run"

Write-Host "Backend is starting in a new window..." -ForegroundColor Green
Write-Host "Please wait for the message: 'Started VehicleRentalApplication'" -ForegroundColor Gray
Write-Host "Backend will be available at: http://localhost:8080" -ForegroundColor Cyan
Write-Host "Swagger UI will be at: http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
Write-Host ""
Start-Sleep -Seconds 10

Write-Host "[3/3] Opening Frontend..." -ForegroundColor Yellow
Write-Host ""
Write-Host "Opening frontend in your default browser..." -ForegroundColor Green
Write-Host ""

# Open frontend files
$frontendTest = Join-Path $PSScriptRoot "frontend\connection-test.html"
$frontendIndex = Join-Path $PSScriptRoot "frontend\index.html"

Start-Process $frontendTest
Start-Sleep -Seconds 2
Start-Process $frontendIndex

Write-Host ""
Write-Host "====================================" -ForegroundColor Cyan
Write-Host "  STARTUP COMPLETE" -ForegroundColor Cyan
Write-Host "====================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Backend: http://localhost:8080" -ForegroundColor Green
Write-Host "Frontend: Opened in browser" -ForegroundColor Green
Write-Host "Connection Test: connection-test.html" -ForegroundColor Green
Write-Host ""
Write-Host "To stop the backend, close the backend PowerShell window" -ForegroundColor Yellow
Write-Host ""
Read-Host "Press Enter to exit this window"
