#!/bin/bash

# Script to create admin user on Render PostgreSQL database
# Usage: ./create_admin_render.sh

echo "=========================================="
echo "Create Admin User for Render Deployment"
echo "=========================================="
echo ""

# Prompt for Render database connection details
read -p "Enter Render PostgreSQL Host (e.g., dpg-xxxxx.oregon-postgres.render.com): " DB_HOST
read -p "Enter Database Name (default: nethaji): " DB_NAME
DB_NAME=${DB_NAME:-nethaji}
read -p "Enter Database Username (default: nethaji_user): " DB_USER
DB_USER=${DB_USER:-nethaji_user}
read -sp "Enter Database Password: " DB_PASSWORD
echo ""

# Admin credentials
ADMIN_EMAIL="admin@nethaji.com"
ADMIN_PASSWORD="admin123"

echo ""
echo "Creating admin user with credentials:"
echo "Email: $ADMIN_EMAIL"
echo "Password: $ADMIN_PASSWORD"
echo ""

# Generate salt and hash password using Java
SALT=$(uuidgen | tr -d '-')$(uuidgen | tr -d '-' | cut -c1-10)

# Create temporary Java file to hash password
cat > /tmp/HashPassword.java << 'EOF'
import java.security.MessageDigest;

public class HashPassword {
    private static final int NO_OF_UPDATES = 100;
    protected final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String hashPassword(String password, String salt) {
        String toBeHashed = password + salt;
        try {
            byte[] stringToHash = toBeHashed.getBytes();
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes());

            for (int i = 0; i < NO_OF_UPDATES; i++) {
                md.reset();
                stringToHash = md.digest(stringToHash);
            }

            return bytesToHex(stringToHash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java HashPassword <password> <salt>");
            System.exit(1);
        }
        System.out.println(hashPassword(args[0], args[1]));
    }
}
EOF

# Compile and run
javac /tmp/HashPassword.java
PASSWORD_HASH=$(java -cp /tmp HashPassword "$ADMIN_PASSWORD" "$SALT")

echo "Salt: $SALT"
echo "Password Hash: $PASSWORD_HASH"
echo ""

# Create SQL to insert admin user
SQL="DELETE FROM users WHERE email = '$ADMIN_EMAIL';
INSERT INTO users (id, email, first_name, last_name, mobile_number, password_hash, salt, user_type, is_active, created_at, updated_at)
VALUES (gen_random_uuid(), '$ADMIN_EMAIL', 'Admin', 'User', '9999999999', '$PASSWORD_HASH', '$SALT', 'ADMIN', true, NOW(), NOW());"

# Execute SQL
echo "Connecting to database and creating admin user..."
PGPASSWORD="$DB_PASSWORD" psql -h "$DB_HOST" -U "$DB_USER" -d "$DB_NAME" -c "$SQL"

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Admin user created successfully!"
    echo ""
    echo "Login credentials:"
    echo "Email: $ADMIN_EMAIL"
    echo "Password: $ADMIN_PASSWORD"
    echo ""
else
    echo ""
    echo "❌ Failed to create admin user"
    echo "Please check your database connection details and try again"
    exit 1
fi

# Cleanup
rm -f /tmp/HashPassword.java /tmp/HashPassword.class

echo "Done!"
