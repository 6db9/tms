package com.tms.dao;

import com.tms.config.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuditDAO {

    public List<String[]> findAllLogs() throws SQLException {
        List<String[]> logs = new ArrayList<>();
        String sql = "SELECT a.created_at, u.username, a.action, a.entity_type, a.entity_id, a.old_value, a.new_value " +
                "FROM audit_log a LEFT JOIN users u ON a.user_id = u.id ORDER BY a.created_at DESC";

        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                String[] row = new String[7];
                row[0] = rs.getTimestamp("created_at").toString();
                row[1] = rs.getString("username") != null ? rs.getString("username") : "Système";
                row[2] = rs.getString("action");
                row[3] = rs.getString("entity_type");
                row[4] = String.valueOf(rs.getInt("entity_id"));
                row[5] = rs.getString("old_value") != null ? rs.getString("old_value") : "—";
                row[6] = rs.getString("new_value") != null ? rs.getString("new_value") : "—";
                logs.add(row);
            }
        }
        return logs;
    }
}