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
            ps.setTimestamp(3, end);
            ps.setTimestamp(4, start);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // THIS WAS MISSING
    public void updateProgress(int missionId, MissionState newState,
                               java.time.LocalDateTime arrival,
                               java.time.LocalDateTime start,
                               java.time.LocalDateTime end) throws SQLException {
        String sql = "UPDATE missions SET state = ?, actual_arrival = ?, actual_start = ?, actual_end = ? WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, newState.name());
            ps.setTimestamp(2, arrival != null ? Timestamp.valueOf(arrival) : null);
            ps.setTimestamp(3, start != null ? Timestamp.valueOf(start) : null);
            ps.setTimestamp(4, end != null ? Timestamp.valueOf(end) : null);
            ps.setInt(5, missionId);
            ps.executeUpdate();
        }
    }

    public List<Mission> findAll() throws SQLException {
        List<Mission> list = new ArrayList<>();
        String sql = "SELECT m.id, m.state, m.planned_start, m.planned_end, " +
                "m.actual_arrival, m.actual_start, m.actual_end, " + // ADDED ACTUAL TIMES HERE
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

                // Fetch the new F8 Times
                Timestamp arr = rs.getTimestamp("actual_arrival");
                if (arr != null) m.setActualArrival(arr.toLocalDateTime());
                Timestamp stTime = rs.getTimestamp("actual_start");
                if (stTime != null) m.setActualStart(stTime.toLocalDateTime());
                Timestamp en = rs.getTimestamp("actual_end");
                if (en != null) m.setActualEnd(en.toLocalDateTime());

                Request req = new Request();
                req.setId(rs.getInt("req_id"));
                req.setType(RequestType.valueOf(rs.getString("req_type")));
                m.setRequest(req);

                Vehicle v = new Vehicle();
                v.setPlate(rs.getString("v_plate"));
                m.setVehicle(v);

                // Fixed the ambiguous Driver reference here
                com.tms.model.Driver d = new com.tms.model.Driver();
                d.setFullName(rs.getString("d_name"));
                m.setDriver(d);

                list.add(m);
            }
        }
        return list;
    }
}