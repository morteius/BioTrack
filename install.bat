@echo off
cd /d "%~dp0"
echo Building BioTrack for Installer...
echo.

:: Clean previous builds
if exist "dist" rmdir /s /q "dist"
if exist "build" rmdir /s /q "build"

:: Create directories
mkdir build
mkdir dist
mkdir dist\data
mkdir dist\img

:: Compile all Java files
echo Step 1: Compiling Java files...
javac -encoding UTF-8 -d "build" ^
    "src\auth\*.java" ^
    "src\data\*.java" ^
    "src\models\*.java" ^
    "src\ui\*.java" ^
    "src\ui\dialogs\*.java" ^
    "src\utils\*.java"

if errorlevel 1 (
    echo ERROR: Compilation failed!
    echo Please fix compilation errors first.
    pause
    exit /b 1
)

:: Create JAR file
echo.
echo Step 2: Creating JAR file...
cd build
jar cfm "..\dist\BioTrack.jar" "..\MANIFEST.MF" *
cd ..

:: Copy data files
echo.
echo Step 3: Copying data files...
copy "data\equipments.txt" "dist\data\"
copy "data\transactions.txt" "dist\data\"

:: Create empty users.txt if doesn't exist
if not exist "dist\data\users.txt" (
    echo admin,admin123,Administrator > "dist\data\users.txt"
)

:: Copy image files
echo.
echo Step 4: Copying image files...
copy "src\img\logo.jpg" "dist\img\"
copy "src\img\password.png" "dist\img\"
copy "src\img\username.png" "dist\img\"

:: Create run.bat for installer
echo.
echo Step 5: Creating run.bat for installer...
echo @echo off > "dist\run.bat"
echo echo BioTrack Laboratory Management System >> "dist\run.bat"
echo echo ===================================== >> "dist\run.bat"
echo echo. >> "dist\run.bat"
echo echo Starting application... >> "dist\run.bat"
echo java -jar "BioTrack.jar" >> "dist\run.bat"
echo echo. >> "dist\run.bat"
echo echo Application closed. >> "dist\run.bat"
echo pause >> "dist\run.bat"

echo.
echo =====================================
echo BUILD COMPLETE for Installer!
echo =====================================
echo.
echo The 'dist' folder now contains:
echo   BioTrack.jar      - Main application
echo   run.bat           - Launcher script
echo   data\             - Database files
echo   img\              - Image resources
echo.
echo Now you can run the installer!
echo.
pause