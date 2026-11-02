package com.tms.dao;

import com.tms.config.Database;
import com.tms.model.Driver;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverDAO {

    public void insert(Driver d) throws SQLException {
        String sql = "INSERT INTO drivers (full_name, phone, license_number, license_valid_until) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, d.getFullName());
            ps.setString(2, d.getPhone());
            ps.setString(3, d.getLicenseNumber());
            ps.setDate(4, d.getLicenseValidUntil() != null ? Date.valueOf(d.getLicenseValidUntil()) : null);
            ps.executeUpdate();
        }
    }

    public List<Driver> findAllActive() throws SQLException {
        List<Driver> list = new ArrayList<>();
        String sql = "SELECT * FROM drivers WHERE archived = FALSE ORDER BY full_name";
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Driver d = new Driver();
                d.setId(rs.getInt("id"));
                d.setFullName(rs.getString("full_name"));
                d.setPhone(rs.getString("phone"));
                d.setLicenseNumber(rs.getString("license_number"));
                Date date = rs.getDate("license_valid_until");
                if (date != null) d.setLicenseValidUntil(date.toLocalDate());
                list.add(d);
            }
        }
        return list;
    }
}