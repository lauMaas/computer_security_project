package com.computer_security_project.encryption;

public class Decryptor {
    private static final int SHIFT = 3; // Same shift value as Encryptor

    public static String decrypt(String ciphertext) {
        StringBuilder decrypted = new StringBuilder();
        for (char c : ciphertext.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                decrypted.append((char) ((c - base - SHIFT + 26) % 26 + base));
            } else {
                decrypted.append(c);
            }
        }
        return decrypted.toString();
    }
}