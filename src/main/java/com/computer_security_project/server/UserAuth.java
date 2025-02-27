package com.computer_security_project.server;

import com.computer_security_project.server.hash.PasswordHasher;
import java.sql.*;

public class UserAuth {

    public static boolean registerUser(String username, String password) {
        if (usernameExists(username)) {
            System.err.println("⚠️ User already exists!");
            return false;
        }

        String hashedPassword = PasswordHasher.hashPassword(password);
        String query = "INSERT INTO users (username, password_hash) VALUES (?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("⚠️ Registration failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean loginUser(String username, String password) {
        String query = "SELECT password_hash FROM users WHERE username=?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                return PasswordHasher.verifyPassword(password, storedHash);
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Login failed: " + e.getMessage());
        }
        return false;
    }

    private static boolean usernameExists(String username) {
        String query = "SELECT username FROM users WHERE username = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Returns true if username exists
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}