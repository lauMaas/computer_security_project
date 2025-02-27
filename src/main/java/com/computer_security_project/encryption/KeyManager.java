package com.computer_security_project.encryption;

import java.math.BigInteger;
import java.security.SecureRandom;

public class KeyManager {
    private static BigInteger privateKey;
    private static BigInteger publicKey;
    private static BigInteger modulus;
    private static final int BIT_LENGTH = 1024; // Increase key security

    static {
        generateKeys();
    }

    private static void generateKeys() {
        SecureRandom random = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(BIT_LENGTH / 2, random);
        BigInteger q = BigInteger.probablePrime(BIT_LENGTH / 2, random);
        modulus = p.multiply(q);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        publicKey = new BigInteger("65537"); // Common public exponent
        privateKey = publicKey.modInverse(phi);
    }

    public static BigInteger getPublicKey() {
        return publicKey;
    }

    public static BigInteger getPrivateKey() {
        return privateKey;
    }

    public static BigInteger getModulus() {
        return modulus;
    }
}