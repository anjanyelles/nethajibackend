#!/bin/bash

echo "=========================================="
echo "Setting up Local PostgreSQL Database"
echo "=========================================="
echo ""

# Check if PostgreSQL is installed
if ! command -v psql &> /dev/null; then
    echo "❌ PostgreSQL is not installed!"
    echo ""
    echo "Installing PostgreSQL..."
    brew install postgresql@14
    echo ""
fi

echo "✅ PostgreSQL found"
echo ""

# Start PostgreSQL service
echo "🚀 Starting PostgreSQL service..."
brew services start postgresql@14

# Wait a bit for service to start
sleep 3

echo ""
echo "📦 Creating database 'nethaji'..."

# Create database (will fail if already exists, that's okay)
createdb nethaji 2>/dev/null || echo "Database 'nethaji' already exists or created"

echo ""
echo "✅ Database setup complete!"
echo ""
echo "=========================================="
echo "Next Steps:"
echo "=========================================="
echo "1. Update application.yml to use local database:"
echo "   url: jdbc:postgresql://localhost:5432/nethaji"
echo "   username: postgres"
echo "   password: <your-postgres-password>"
echo ""
echo "2. Or use the local profile:"
echo "   mvn spring-boot:run -Dspring-boot.run.profiles=local"
echo ""
echo "3. Run the application:"
echo "   mvn spring-boot:run"
echo ""

