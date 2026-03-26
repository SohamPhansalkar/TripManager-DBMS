#!/bin/bash

# Compile the project
echo "Compiling..."
javac LandingPage.java AuthPage.java Dashboard.java

# If compilation was successful, run it
if [ $? -eq 0 ]; then
    echo "Launching TripManager..."
    java -cp ../../ FrontEnd.Tanmay.LandingPage
else
    echo "Compilation failed!"
fi
