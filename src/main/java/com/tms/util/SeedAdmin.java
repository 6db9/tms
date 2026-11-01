package com.tms.util;

import com.tms.config.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SeedAdmin {
    public static void main(String[] args) throws Exception {
        String username = "admin";
        String password = "admin123"; // change after first login

        Connection c = Database.getConnection();
        try (PreparedStatement check = c.prepareStatement(
                "SELECT id FROM users WHERE username = ?")) {
            check.setString(1, username);
            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) {
                    System.out.println("User '" + username + "' already exists — skipping.");
                    return;
                }
            }
        }

        String salt = PasswordUtil.generateSalt();
        String hash = PasswordUtil.hash(password, salt);

        c.setAutoCommit(false);
        try (PreparedStatement ps = c.prepareStatement(
                "INSERT INTO users (username, password_hash, salt, active) VALUES (?, ?, ?, TRUE)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.setString(2, hash);
            ps.setString(3, salt);
            ps.executeUpdate();

            int userId;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                userId = keys.getInt(1);
            }

            try (PreparedStatement rs = c.prepareStatement(
                    "INSERT INTO user_roles (user_id, role_id) " +
                            "SELECT ?, id FROM roles WHERE name = 'ADMIN'")) {
                rs.setInt(1, userId);
                rs.executeUpdate();
            }
            c.commit();
            System.out.println("Created user '" + username + "' with password '" + password + "' (role ADMIN).");
        } catch (Exception e) {
            c.rollback();
            throw e;
        } finally {
            c.setAutoCommit(true);
        }
    }
}