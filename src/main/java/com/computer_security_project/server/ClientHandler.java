package com.computer_security_project.server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private boolean isAuthenticated = false;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            username = in.readLine(); // Read username from client
            System.out.println("📥 Received username from client: " + username);

            if (username == null || username.trim().isEmpty()) {
                System.out.println("🔌 A client connected but never logged in. Closing connection.");
                socket.close();
                return;
            }

            ChatServer.addClient(username, this);
            isAuthenticated = true;
            System.out.println("📢 " + username + " connected.");

            String message;
            while ((message = in.readLine()) != null) {
                ChatServer.broadcastMessage(username, message);
            }
        } catch (IOException e) {
            System.err.println("⚠️ Connection error: " + e.getMessage());
        } finally {
            if (isAuthenticated) {
                ChatServer.removeClient(username);
                System.out.println("❌ " + username + " disconnected.");
            }

            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }
}