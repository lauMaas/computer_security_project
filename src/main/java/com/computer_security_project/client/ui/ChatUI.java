package com.computer_security_project.client.ui;

import com.computer_security_project.client.ClientManager;
import com.computer_security_project.server.MessageStorage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ChatUI extends JFrame {
    private JTextPane chatPane;
    private JTextField messageField;
    private JButton sendButton;
    private String sender;
    private String receiver;
    private ClientManager clientManager;

    public ChatUI(String sender, String receiver) {
        this.sender = sender;
        this.receiver = receiver;

        setTitle("Chat with " + receiver);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        chatPane = new JTextPane();
        chatPane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatPane);

        messageField = new JTextField();
        sendButton = new JButton("Send");

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(messageField, BorderLayout.CENTER);
        panel.add(sendButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());

        loadPreviousMessages();
        setVisible(true);
        connectToServer();
    }

    /**
     * Establishes a connection to the chat server.
     */
    private void connectToServer() {
        try {
            clientManager = ClientManager.getInstance(sender); // ✅ Reuse existing instance
    
            // Start a separate thread to listen for incoming messages
            new Thread(() -> {
                try {
                    String received;
                    while ((received = clientManager.receiveMessage()) != null) {
                        String[] parts = received.split(": ", 2);
                        if (parts.length == 2) {
                            appendMessageToChat(parts[0], parts[1], Color.RED);
                        }
                    }
                } catch (IOException e) {
                    appendMessageToChat("System", "❌ Connection lost with server!", Color.BLACK);
                }
            }).start();
    
        } catch (IOException e) {
            appendMessageToChat("System", "❌ Error connecting to server!", Color.BLACK);
        }
    }

    /**
     * Sends a message through ClientManager and stores it in DB.
     */
    private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            clientManager.sendMessage(message);
            messageField.setText("");

            appendMessageToChat("Me", message, Color.BLUE);
            MessageStorage.storeEncryptedMessage(sender, receiver, message);
        }
    }

    /**
     * Loads previous chat messages from the database.
     */
    private void loadPreviousMessages() {
        chatPane.setText("Loading previous messages...\n");
        List<String> messages = MessageStorage.loadMessages(sender, receiver);

        for (String msg : messages) {
            appendMessageToChat(msg.split(": ")[0], msg.split(": ")[1], Color.GRAY);
        }
    }

    /**
     * Displays messages with color.
     */
    private void appendMessageToChat(String senderName, String message, Color color) {
        chatPane.setForeground(color);
        chatPane.setText(chatPane.getText() + senderName + ": " + message + "\n");
        chatPane.setForeground(Color.BLACK);
    }
}