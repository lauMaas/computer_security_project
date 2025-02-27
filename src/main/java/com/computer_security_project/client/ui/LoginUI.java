package com.computer_security_project.client.ui;

import com.computer_security_project.client.ClientManager;
import com.computer_security_project.server.UserAuth;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel statusLabel;

    public LoginUI() {
        setTitle("Secure Chat - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(5, 1));

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");
        statusLabel = new JLabel("", SwingConstants.CENTER);

        add(new JLabel("Username:", SwingConstants.CENTER));
        add(usernameField);
        add(new JLabel("Password:", SwingConstants.CENTER));
        add(passwordField);
        add(loginButton);
        add(registerButton);
        add(statusLabel);

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegister());

        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
    
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("⚠️ Please enter both username and password.");
            return;
        }
    
        if (UserAuth.loginUser(username, password)) {
            try {
                if (ClientManager.getInstance(username) == null) { // ✅ Prevent duplicate creation
                    ClientManager.getInstance(username);
                }
                dispose();
                new MainUI(username); // ✅ Pass username to MainUI
            } catch (IOException e) {
                statusLabel.setText("❌ Failed to connect to server.");
            }
        } else {
            statusLabel.setText("❌ Login failed! Try again.");
        }
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("⚠️ Username and password cannot be empty.");
            return;
        }

        if (UserAuth.registerUser(username, password)) {
            statusLabel.setText("✅ Registration successful! Please login.");
        } else {
            statusLabel.setText("⚠️ User already exists!");
        }
    }

    public static void main(String[] args) {
        new LoginUI();
    }
}