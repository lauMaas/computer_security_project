package com.computer_security_project.server.hash;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordHasher {

    // Hash a password using Argon2
    public static String hashPassword(String password) {
        Argon2 argon2 = Argon2Factory.create();
        try {
            // 3 iterations, 64MB memory, 1 parallel thread (adjust as needed)
            return argon2.hash(3, 65536, 1, password);
        } finally {
            argon2.wipeArray(password.toCharArray()); // Clears sensitive data
        }
    }

    // Verify a password against a stored hash
    public static boolean verifyPassword(String password, String hash) {
        Argon2 argon2 = Argon2Factory.create();
        try {
            return argon2.verify(hash, password);
        } finally {
            argon2.wipeArray(password.toCharArray());
        }
    }
}