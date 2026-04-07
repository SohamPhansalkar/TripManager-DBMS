@echo off
title Start TripManager Backends
echo ===========================================
echo       TripManager Backends (Talib & Soham)
echo ===========================================

cd /d "%~dp0"
echo Compiling Java Backend files for Talib...
powershell -Command "javac -d . -cp '.;BackEnd\Talib\lib\mysql-connector-j-9.6.0.jar' (Get-ChildItem -Path BackEnd\Talib -Recurse -Filter *.java).FullName"

echo Compiling Java Backend files for Soham...
powershell -Command "javac -d . -cp '.;BackEnd\Talib\lib\mysql-connector-j-9.6.0.jar' (Get-ChildItem -Path BackEnd\Soham -Recurse -Filter *.java).FullName"

echo.
echo Both Backends Successfully Compiled!
echo Starting Talib Server on port 8081...
start "Talib Backend (Port 8081)" cmd /k "java -cp \".;BackEnd\Talib\lib\mysql-connector-j-9.6.0.jar\" BackEnd.Talib.Main"

echo Starting Soham Server on port 8080...
start "Soham Backend (Port 8080)" cmd /k "java -cp \".;BackEnd\Talib\lib\mysql-connector-j-9.6.0.jar\" BackEnd.Soham.Main"

echo.
echo Both servers are now running in separate windows!
echo Keep those windows open to keep the servers alive.
pause