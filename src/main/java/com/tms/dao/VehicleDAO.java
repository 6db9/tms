package com.tms.dao;

import com.tms.config.Database;
import com.tms.model.Vehicle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    public void insert(Vehicle v) throws SQLException {
        String sql = "INSERT INTO vehicles (plate, type, capacity_kg, capacity_m3) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, v.getPlate());
            ps.setString(2, v.getType());
            ps.setBigDecimal(3, v.getCapacityKg());
            ps.setBigDecimal(4, v.getCapacityM3());
            ps.executeUpdate();
        }
    }

    public List<Vehicle> findAllActive() throws SQLException {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE archived = FALSE ORDER BY plate";
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Vehicle v = new Vehicle();
                v.setId(rs.getInt("id"));
                v.setPlate(rs.getString("plate"));
                v.setType(rs.getString("type"));
                v.setCapacityKg(rs.getBigDecimal("capacity_kg"));
                v.setCapacityM3(rs.getBigDecimal("capacity_m3"));
                list.add(v);
            }
        }
        return list;
    }

    public boolean existsPlate(String plate) throws SQLException {
        String sql = "SELECT 1 FROM vehicles WHERE plate = ? AND archived = FALSE";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, plate);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}