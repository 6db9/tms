package com.tms.dao;

import com.tms.config.Database;
import com.tms.model.Client;
import com.tms.model.Request;
import com.tms.model.RequestState;
import com.tms.model.RequestType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO {

    public void insert(Request r) throws SQLException {
        String sql = "INSERT INTO requests (type, client_id, state) VALUES (?, ?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getType().name());
            ps.setInt(2, r.getClient().getId());
            ps.setString(3, r.getState().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) r.setId(keys.getInt(1));
            }
        }
    }

    public void updateState(int requestId, RequestState newState) throws SQLException {
        String sql = "UPDATE requests SET state = ? WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, newState.name());
            ps.setInt(2, requestId);
            ps.executeUpdate();
        }
    }

    public List<Request> findAll() throws SQLException {
        List<Request> list = new ArrayList<>();
        String sql = "SELECT r.id, r.type, r.state, r.created_at, " +
                "c.id AS c_id, c.name AS c_name, c.phone AS c_phone " +
                "FROM requests r " +
                "JOIN clients c ON r.client_id = c.id " +
                "ORDER BY r.created_at DESC";

        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Request r = new Request();
                r.setId(rs.getInt("id"));
                r.setType(RequestType.valueOf(rs.getString("type")));
                r.setState(RequestState.valueOf(rs.getString("state")));
                r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                Client c = new Client();
                c.setId(rs.getInt("c_id"));
                c.setName(rs.getString("c_name"));
                c.setPhone(rs.getString("c_phone"));
                r.setClient(c);

                list.add(r);
            }
        }
        return list;
    }
}