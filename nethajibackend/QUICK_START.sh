#!/bin/bash

# Quick Start Script for Nethaji College Management System
# This script helps you set up and run the application

echo "=========================================="
echo "Nethaji College Management System"
echo "Quick Start Script"
echo "=========================================="
echo ""

# Check Java version
echo "Checking Java version..."
java -version
if [ $? -ne 0 ]; then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi
echo "✅ Java is installed"
echo ""

# Check Maven version
echo "Checking Maven version..."
mvn -version
if [ $? -ne 0 ]; then
    echo "❌ Maven is not installed. Please install Maven."
    exit 1
fi
echo "✅ Maven is installed"
echo ""

# Navigate to project directory
cd "$(dirname "$0")"
echo "📁 Current directory: $(pwd)"
echo ""

# Clean previous build
echo "🧹 Cleaning previous build..."
mvn clean
echo ""

# Build project
echo "🔨 Building project..."
mvn install -DskipTests
if [ $? -ne 0 ]; then
    echo "❌ Build failed. Please check the errors above."
    exit 1
fi
echo "✅ Build successful"
echo ""

# Run application
echo "🚀 Starting application..."
echo "Application will run on: http://localhost:9029"
echo "Press Ctrl+C to stop the application"
echo ""
mvn spring-boot:run

