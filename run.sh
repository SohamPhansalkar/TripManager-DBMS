#!/bin/bash

# This script compiles and runs the database connection test.

# --- CONFIGURATION ---
# Path to your MySQL JDBC Driver JAR file.
# I've used the path I found on your system.
# If you move it, you'll need to update this path.
MYSQL_DRIVER_PATH="/home/soham/.m2/repository/com/mysql/mysql-connector-j/8.0.32/mysql-connector-j-8.0.32.jar"

# --- SCRIPT ---
# Exit immediately if a command exits with a non-zero status.
set -e

echo "Compiling Java source files..."
javac BackEnd/ReferenceCode/EgJDBC.java BackEnd/ReferenceCode/QueryRunner.java

echo ""
echo "Running the query..."
# We build a classpath that includes the current directory (.) and the MySQL driver.
java -cp ".:${MYSQL_DRIVER_PATH}" BackEnd.ReferenceCode.QueryRunner

echo ""
echo "Script finished."
