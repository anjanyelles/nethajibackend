#!/bin/bash

echo "=========================================="
echo "Nethaji College Management System"
echo "Starting Application..."
echo "=========================================="
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven is not installed!"
    echo ""
    echo "Please install Maven first:"
    echo "  brew install maven"
    echo ""
    exit 1
fi

echo "✅ Maven found"
echo ""

# Navigate to project directory
cd "$(dirname "$0")"
echo "📁 Current directory: $(pwd)"
echo ""

# Build and run
echo "🔨 Building project..."
mvn clean install -DskipTests

if [ $? -ne 0 ]; then
    echo "❌ Build failed!"
    exit 1
fi

echo ""
echo "🚀 Starting application..."
echo "=========================================="
echo "Application URL: http://localhost:9029"
echo "Health Check: http://localhost:9029/api/nethaji-service/health"
echo "=========================================="
echo ""
echo "Press Ctrl+C to stop the application"
echo ""
mvn spring-boot:run
