package com.nethaji.util;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.UUID;

public class CreateAdminUser {

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
        String dbUrl = "jdbc:postgresql://localhost:5432/nethaji";
        String dbUser = "nethaji_user";
        String dbPassword = "password";

        String email = "admin@nethaji.com";
        String password = "admin123";
        String salt = UUID.randomUUID().toString() + UUID.randomUUID().toString().substring(0, 10);
        String passwordHash = hashPassword(password, salt);

        System.out.println("Creating admin user...");
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);
        System.out.println("Salt: " + salt);
        System.out.println("Password Hash: " + passwordHash);

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            // First, delete existing admin user if exists
            String deleteSql = "DELETE FROM users WHERE email = ?";
            PreparedStatement deletePstmt = conn.prepareStatement(deleteSql);
            deletePstmt.setString(1, email);
            deletePstmt.executeUpdate();
            
            // Insert new admin user
            String sql = "INSERT INTO users (id, email, first_name, last_name, mobile_number, password_hash, salt, user_type, is_active, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setObject(1, UUID.randomUUID());
            pstmt.setString(2, email);
            pstmt.setString(3, "Admin");
            pstmt.setString(4, "User");
            pstmt.setString(5, "9999999999");
            pstmt.setString(6, passwordHash);
            pstmt.setString(7, salt);
            pstmt.setString(8, "ADMIN");
            pstmt.setBoolean(9, true);

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("\n✅ Admin user created successfully! Rows affected: " + rowsAffected);
            System.out.println("\nLogin credentials:");
            System.out.println("Email: admin@nethaji.com");
            System.out.println("Password: admin123");

        } catch (Exception e) {
            System.err.println("❌ Error creating admin user: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
