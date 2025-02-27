package com.computer_security_project.client.ui;

import com.computer_security_project.server.DatabaseManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainUI extends JFrame {
    private String username;
    private JTextField searchField;
    private DefaultListModel<String> searchListModel;
    private JList<String> searchList;
    private DefaultListModel<String> chatListModel;
    private JList<String> chatList;

    public MainUI(String username) {
        this.username = username;

        setTitle("Secure Chat - Contacts");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Search bar
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField("Search users...");
        searchListModel = new DefaultListModel<>();
        searchList = new JList<>(searchListModel);
        JScrollPane searchScrollPane = new JScrollPane(searchList);
        
        searchPanel.add(searchField, BorderLayout.NORTH);
        searchPanel.add(searchScrollPane, BorderLayout.CENTER);

        // Chat list (people the user has already messaged)
        chatListModel = new DefaultListModel<>();
        chatList = new JList<>(chatListModel);
        JScrollPane chatScrollPane = new JScrollPane(chatList);
        
        JButton openChatButton = new JButton("Open Chat");

        add(searchPanel, BorderLayout.NORTH);
        add(chatScrollPane, BorderLayout.CENTER);
        add(openChatButton, BorderLayout.SOUTH);

        // Load chat history & search users
        loadContacts();
        searchUsers(""); // Load all users initially

        // Add search listener
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String query = searchField.getText().trim();
                searchUsers(query);
            }
        });

        // Open chat when clicking a user in search list
        searchList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    String selectedUser = searchList.getSelectedValue();
                    if (selectedUser != null && !selectedUser.equals(username)) {
                        new ChatUI(username, selectedUser);
                    }
                }
            }
        });

        // Open chat when clicking a contact from chat history
        openChatButton.addActionListener(e -> openChat());

        setVisible(true);
    }

    private void loadContacts() {
        chatListModel.clear();
        List<String> contacts = getContactsFromDatabase();

        for (String contact : contacts) {
            chatListModel.addElement(contact);
        }
    }

    private List<String> getContactsFromDatabase() {
        List<String> contacts = new ArrayList<>();
        String query = "SELECT DISTINCT sender FROM messages WHERE receiver = ? " +
                       "UNION " +
                       "SELECT DISTINCT receiver FROM messages WHERE sender = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String contact = rs.getString(1);
                if (!contact.equals(username)) { 
                    contacts.add(contact);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    private void searchUsers(String query) {
        searchListModel.clear();
        List<String> users = getAllUsers(query);

        for (String user : users) {
            if (!user.equals(username)) { 
                searchListModel.addElement(user);
            }
        }
    }

    private List<String> getAllUsers(String searchQuery) {
        List<String> users = new ArrayList<>();
        String query = "SELECT username FROM users WHERE username LIKE ? ORDER BY username ASC";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + searchQuery + "%"); // Partial match search
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                users.add(rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private void openChat() {
        String selectedUser = chatList.getSelectedValue();
        if (selectedUser != null) {
            new ChatUI(username, selectedUser); 
        } else {
            JOptionPane.showMessageDialog(this, "Please select a contact first!");
        }
    }
}