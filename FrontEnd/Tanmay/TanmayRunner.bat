@echo off

echo Compiling...
javac LandingPage.java AuthPage.java Dashboard.java

if %errorlevel% equ 0 (
    echo Launching TripManager...
    java -cp ..\..\ FrontEnd.Tanmay.LandingPage
) else (
    echo Compilation failed!
)
