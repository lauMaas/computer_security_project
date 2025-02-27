package com.computer_security_project.server;

import com.computer_security_project.encryption.Encryptor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageStorage {

    public static void storeEncryptedMessage(String sender, String receiver, String encryptedMessage) {
        String query = "INSERT INTO messages (sender, receiver, encrypted_message, is_read) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, sender);
            stmt.setString(2, receiver);
            stmt.setString(3, encryptedMessage);
            stmt.setBoolean(4, false);  // Message starts as unread
            stmt.executeUpdate();
            System.out.println("Message stored securely.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<String> loadMessages(String sender, String receiver) {
        List<String> messages = new ArrayList<>();
        String query = "SELECT sender, encrypted_message FROM messages WHERE " +
                       "(sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?) ORDER BY timestamp";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, sender);
            stmt.setString(2, receiver);
            stmt.setString(3, receiver);
            stmt.setString(4, sender);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String senderName = rs.getString("sender");
                String encryptedMessage = rs.getString("encrypted_message");
                messages.add(senderName + ": " + encryptedMessage);
            }

            // Mark messages as read
            markMessagesAsRead(sender, receiver);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    private static void markMessagesAsRead(String recipient, String sender) {
        String query = "UPDATE messages SET is_read = true WHERE receiver = ? AND sender = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, recipient);
            stmt.setString(2, sender);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}