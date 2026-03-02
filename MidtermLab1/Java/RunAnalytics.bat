@echo off
REM Batch file to compile and run CSVAnalytics.java automatically
REM User can simply double-click this file to run the program

cd /d "%~dp0Java"

echo Compiling CSVAnalytics.java...
javac CSVAnalytics.java

if %errorlevel% neq 0 (
    echo.
    echo Compilation failed. Please check your Java installation.
    pause
    exit /b 1
)

echo Compilation successful. Running program...
echo.

java CSVAnalytics

pause
