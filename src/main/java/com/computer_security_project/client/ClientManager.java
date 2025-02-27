package com.computer_security_project.client;

import java.io.*;
import java.net.*;

public class ClientManager {
    private static ClientManager instance;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 5050;

    private ClientManager(String username) throws IOException {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("⚠️ Username cannot be null or empty!");
        }
        this.username = username;
        socket = new Socket(SERVER_IP, SERVER_PORT);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Immediately send the username to the server
        out.println(username);
        out.flush();
    }

    public static synchronized ClientManager getInstance(String username) throws IOException {
        if (instance == null) {
            instance = new ClientManager(username);
        }
        return instance;
    }

    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }

    public String receiveMessage() throws IOException {
        if (in != null) {
            return in.readLine();
        }
        return null;
    }

    public void closeConnection() {
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
            instance = null; // ✅ Reset instance
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}