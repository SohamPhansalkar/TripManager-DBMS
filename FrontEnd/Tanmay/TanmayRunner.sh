#!/bin/bash

# Compile the project
echo "Compiling..."
javac /Users/tanmay/repos/TripManager-DBMS/FrontEnd/Tanmay/LandingPage.java /Users/tanmay/repos/TripManager-DBMS/FrontEnd/Tanmay/AuthPage.java /Users/tanmay/repos/TripManager-DBMS/FrontEnd/Tanmay/Dashboard.java

# If compilation was successful, run it
if [ $? -eq 0 ]; then
    echo "Launching TripManager..."
    java -cp ../../ FrontEnd.Tanmay.LandingPage
else
    echo "Compilation failed!"
fi
