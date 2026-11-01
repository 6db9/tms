package com.tms.dao;

import com.tms.config.Database;
import com.tms.model.Role;
import com.tms.model.User;

import java.sql.*;
import java.util.Optional;

public class UserDAO {

    /** Returns the user (with roles loaded) or empty if not found. */
    public Optional<User> findByUsername(String username) throws SQLException {
        Connection c = Database.getConnection();
        String sql = "SELECT id, username, password_hash, salt, active FROM users WHERE username = ?";

        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setSalt(rs.getString("salt"));
                u.setActive(rs.getBoolean("active"));
                loadRoles(u);
                return Optional.of(u);
            }
        }
    }

    private void loadRoles(User user) throws SQLException {
        Connection c = Database.getConnection();
        String sql = "SELECT r.name FROM roles r " +
                "JOIN user_roles ur ON ur.role_id = r.id " +
                "WHERE ur.user_id = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, user.getId());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    user.getRoles().add(Role.valueOf(rs.getString("name")));
                }
            }
        }
    }
}