package com.tms.dao;

import com.tms.config.Database;
import com.tms.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    public void insert(Client c) throws SQLException {
        String sql = "INSERT INTO clients (name, phone, email, address, tax_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getAddress());
            ps.setString(5, c.getTaxId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) c.setId(keys.getInt(1));
            }
        }
    }

    public void update(Client c) throws SQLException {
        String sql = "UPDATE clients SET name=?, phone=?, email=?, address=?, tax_id=? WHERE id=?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getAddress());
            ps.setString(5, c.getTaxId());
            ps.setInt(6, c.getId());
            ps.executeUpdate();
        }
    }

    public List<Client> findAllActive() throws SQLException {
        List<Client> list = new ArrayList<>();
        String sql = "SELECT * FROM clients WHERE archived = FALSE ORDER BY name";
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public boolean hasDuplicateTaxId(String taxId) throws SQLException {
        if (taxId == null || taxId.isBlank()) return false;
        String sql = "SELECT 1 FROM clients WHERE tax_id = ? AND archived = FALSE";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, taxId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private Client mapRow(ResultSet rs) throws SQLException {
        Client c = new Client();
        c.setId(rs.getInt("id"));
        c.setName(rs.getString("name"));
        c.setPhone(rs.getString("phone"));
        c.setEmail(rs.getString("email"));
        c.setAddress(rs.getString("address"));
        c.setTaxId(rs.getString("tax_id"));
        c.setArchived(rs.getBoolean("archived"));
        return c;
    }
}