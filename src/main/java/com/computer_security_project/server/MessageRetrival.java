package com.computer_security_project.server;

import com.computer_security_project.encryption.Decryptor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MessageRetrival {

    public static void getMessagesForUser(String username, Decryptor decryptor) {
        String query = "SELECT sender, encrypted_message FROM messages WHERE receiver = ?";
        
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String sender = rs.getString("sender");
                String message = rs.getString("encrypted_message");


                System.out.println("From " + sender + ": " + message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}