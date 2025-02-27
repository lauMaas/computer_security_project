package com.computer_security_project.encryption;

public class Encryptor {
    private static final int SHIFT = 3; // Caesar cipher shift value

    public static String encrypt(String plaintext) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : plaintext.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                encrypted.append((char) ((c - base + SHIFT) % 26 + base));
            } else {
                encrypted.append(c);
            }
        }
        return encrypted.toString();
    }
}