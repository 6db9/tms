package com.tms.dao;

import com.tms.config.Database;
import com.tms.model.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FinanceDAO {

    public void insertPayment(Payment p) throws SQLException {
        String sql = "INSERT INTO payments (request_id, amount, state) VALUES (?, ?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, p.getRequest().getId());
            ps.setBigDecimal(2, p.getAmount());
            ps.setString(3, p.getState().name());
            ps.executeUpdate();
        }
    }

    public void updatePaymentState(int paymentId, PaymentState newState) throws SQLException {
        String sql = "UPDATE payments SET state = ? WHERE id = ?";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setString(1, newState.name());
            ps.setInt(2, paymentId);
            ps.executeUpdate();
        }
    }

    public List<Payment> findAllPayments() throws SQLException {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT p.id, p.amount, p.state, r.id AS r_id, c.name AS c_name " +
                "FROM payments p " +
                "JOIN requests r ON p.request_id = r.id " +
                "JOIN clients c ON r.client_id = c.id " +
                "ORDER BY p.created_at DESC";
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Payment p = new Payment();
                p.setId(rs.getInt("id"));
                p.setAmount(rs.getBigDecimal("amount"));
                p.setState(PaymentState.valueOf(rs.getString("state")));

                Request r = new Request();
                r.setId(rs.getInt("r_id"));
                Client c = new Client();
                c.setName(rs.getString("c_name"));
                r.setClient(c);
                p.setRequest(r);

                list.add(p);
            }
        }
        return list;
    }

    public void insertExpense(Expense e) throws SQLException {
        String sql = "INSERT INTO expenses (mission_id, category, amount, expense_date, reference) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, e.getMission().getId());
            ps.setString(2, e.getCategory());
            ps.setBigDecimal(3, e.getAmount());
            ps.setDate(4, Date.valueOf(e.getExpenseDate()));
            ps.setString(5, e.getReference());
            ps.executeUpdate();
        }
    }

    public List<Expense> findAllExpenses() throws SQLException {
        List<Expense> list = new ArrayList<>();
        String sql = "SELECT e.id, e.category, e.amount, e.expense_date, e.reference, " +
                "m.id AS m_id, v.plate AS v_plate " +
                "FROM expenses e " +
                "JOIN missions m ON e.mission_id = m.id " +
                "LEFT JOIN vehicles v ON m.vehicle_id = v.id " +
                "ORDER BY e.expense_date DESC";
        try (Statement st = Database.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Expense e = new Expense();
                e.setId(rs.getInt("id"));
                e.setCategory(rs.getString("category"));
                e.setAmount(rs.getBigDecimal("amount"));
                e.setExpenseDate(rs.getDate("expense_date").toLocalDate());
                e.setReference(rs.getString("reference"));

                Mission m = new Mission();
                m.setId(rs.getInt("m_id"));
                Vehicle v = new Vehicle();
                v.setPlate(rs.getString("v_plate"));
                m.setVehicle(v);
                e.setMission(m);

                list.add(e);
            }
        }
        return list;
    }

    // Calcul du solde : Dette - Paiements Confirmés
    public BigDecimal calculateBalance(int requestId) throws SQLException {
        // Ici, on calcule la somme des paiements CONFIRMÉS pour l'exemple.
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM payments WHERE request_id = ? AND state = 'CONFIRMED'";
        try (PreparedStatement ps = Database.getConnection().prepareStatement(sql)) {
            ps.setInt(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal(1);
            }
        }
        return BigDecimal.ZERO;
    }
}