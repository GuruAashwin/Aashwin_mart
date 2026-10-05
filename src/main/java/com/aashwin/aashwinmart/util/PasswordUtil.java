package com.aashwin.aashwinmart.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Security utility for bcrypt password hashing and verification.
 * Strictly avoids plaintext passwords, MD5, or SHA-1.
 */
public final class PasswordUtil {

    private static final int DEFAULT_LOG_ROUNDS = 12;

    private PasswordUtil() {
        // Prevent instantiation
    }

    /**
     * Hashes a plaintext password using salted bcrypt with a work factor of 12.
     *
     * @param plainPassword Plaintext password
     * @return 60-character bcrypt hash string
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(DEFAULT_LOG_ROUNDS));
    }

    /**
     * Verifies a plaintext password against a stored bcrypt hash.
     *
     * @param plainPassword Plaintext password
     * @param hashedPassword Stored bcrypt hash string
     * @return true if password matches, false otherwise
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null || hashedPassword.isEmpty()) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            // Invalid hash format
            return false;
        }
    }
}
