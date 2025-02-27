package com.computer_security_project.client;

import com.computer_security_project.client.ui.LoginUI;
import com.computer_security_project.encryption.KeyManager;
import java.io.*;
import java.net.*;

public class ChatClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 5050;

    public static void main(String[] args) {
        new LoginUI();

    }
}