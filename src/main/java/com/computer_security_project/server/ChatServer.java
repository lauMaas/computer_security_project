package com.computer_security_project.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private static final int PORT = 5050;
    private static ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        System.out.println("🚀 Chat Server started...");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addClient(String username, ClientHandler clientHandler) {
        clients.put(username, clientHandler);
    }

    public static void removeClient(String username) {
        clients.remove(username);
    }

    public static void broadcastMessage(String sender, String message) {
        for (String client : clients.keySet()) {
            if (!client.equals(sender)) {
                clients.get(client).sendMessage(sender + ": " + message);
            }
        }
    }

    public static void sendPrivateMessage(String sender, String recipient, String message) {
        if (clients.containsKey(recipient)) {
            clients.get(recipient).sendMessage("(Private) " + sender + ": " + message);
        }
    }
}