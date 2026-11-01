package com.tms.util;

import org.mindrot.jbcrypt.BCrypt;
import java.security.SecureRandom;
import java.util.Base64;

public final class PasswordUtil {

    private static final SecureRandom RNG = new SecureRandom();

    private PasswordUtil() {}


    public static String generateSalt() {
        byte[] salt = new byte[16];
        RNG.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }


    public static String hash(String password, String salt) {
        // BCrypt accepts salt as $2a$10$... form; combine manually:
        return BCrypt.hashpw(password + ":" + salt, BCrypt.gensalt(12));
    }


    public static boolean verify(String password, String salt, String hash) {
        try {
            return BCrypt.checkpw(password + ":" + salt, hash);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}