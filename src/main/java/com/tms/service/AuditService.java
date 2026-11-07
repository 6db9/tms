package com.tms.service;

import com.tms.config.Database;
import com.tms.util.Session;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AuditService {

    public static void log(String action, String entityType, int entityId, String oldVal, String newVal) {
        if (!Session.isLoggedIn()) return;

        String sql = "INSERT INTO audit_log (user_id, action, entity_type, entity_id, old_value, new_value) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, Session.getUser().getId());
            ps.setString(2, action);
            ps.setString(3, entityType);
            ps.setInt(4, entityId);
            ps.setString(5, oldVal);
            ps.setString(6, newVal);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Don't crash the app if audit fails, just print
            System.err.println("Failed to write audit log: " + e.getMessage());
        }
    }
}