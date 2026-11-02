package com.tms.dao;

import com.tms.config.Database;
import com.tms.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MissionDAO {

    public void insert(Mission m) throws SQLException {
        String sql = "INSERT INTO missions (request_id, vehicle_id, driver_id, state, planned_start, planned_end) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, m.getRequest().getId());
            ps.setInt(2, m.getVehicle().getId());
            ps.setInt(3, m.getDriver().getId());
            ps.setString(4, m.getState().name());
            ps.setTimestamp(5, Timestamp.valueOf(m.getPlannedStart()));
            ps.setTimestamp(6, Timestamp.valueOf(m.getPlannedEnd()));
            ps.executeUpdate();
        }
    }


     // Checks if a vehicle or driver is already booked during the requested time period.

    public boolean hasConflict(int vehicleId, int driverId, Timestamp start, Timestamp end) throws SQLException {
        String sql = "SELECT 1 FROM missions " +
                "WHERE state NOT IN ('CANCELLED', 'DONE') " +
                "AND (vehicle_id = ? OR driver_id = ?) " +
                "AND planned_start < ? AND planned_end > ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            ps.setInt(2, driverId);
            ps.setTimestamp(3, end);   // Existing start < New end
            ps.setTimestamp(4, start); // Existing end > New start
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true if a conflict is found
            }
        }
    }

    public List<Mission> findAll() throws SQLException {
        List<Mission> list = new ArrayList<>();
        String sql = "SELECT m.id, m.state, m.planned_start, m.planned_end, " +
                "r.id AS req_id, r.type AS req_type, " +
                "v.plate AS v_plate, " +
                "d.full_name AS d_name " +
                "FROM missions m " +
                "JOIN requests r ON m.request_id = r.id " +
                "LEFT JOIN vehicles v ON m.vehicle_id = v.id " +
                "LEFT JOIN drivers d ON m.driver_id = d.id " +
                "ORDER BY m.planned_start DESC";

        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Mission m = new Mission();
                m.setId(rs.getInt("id"));
                m.setState(MissionState.valueOf(rs.getString("state")));

                Timestamp start = rs.getTimestamp("planned_start");
                if (start != null) m.setPlannedStart(start.toLocalDateTime());
                Timestamp end = rs.getTimestamp("planned_end");
                if (end != null) m.setPlannedEnd(end.toLocalDateTime());

                Request req = new Request();
                req.setId(rs.getInt("req_id"));
                req.setType(RequestType.valueOf(rs.getString("req_type")));
                m.setRequest(req);

                Vehicle v = new Vehicle();
                v.setPlate(rs.getString("v_plate"));
                m.setVehicle(v);

                com.tms.model.Driver d = new com.tms.model.Driver();
                d.setFullName(rs.getString("d_name"));
                m.setDriver(d);

                list.add(m);
            }
        }
        return list;
    }
}