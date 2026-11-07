package com.tms.service;

import com.tms.dao.FinanceDAO;
import com.tms.model.*;
import com.tms.util.Session;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class FinanceService {

    private final FinanceDAO financeDAO = new FinanceDAO();

    // Le commercial peut annoncer, mais seul le financier confirme.
    public void announcePayment(Request request, BigDecimal amount) throws Exception {
        Session.requireRole(Role.ADMIN); // ou COMMERCIAL
        Payment p = new Payment();
        p.setRequest(request);
        p.setAmount(amount);
        p.setState(PaymentState.ANNOUNCED);
        financeDAO.insertPayment(p);
    }

    public void confirmPayment(Payment p) throws Exception {
        // Autorisation stricte au point d'exécution
        Session.requireRole(Role.FINANCIER);
        if (p.getState() != PaymentState.ANNOUNCED && p.getState() != PaymentState.PENDING) {
            throw new Exception("Seul un paiement annoncé ou en attente peut être confirmé.");
        }
        financeDAO.updatePaymentState(p.getId(), PaymentState.CONFIRMED);

        AuditService.log("PAYMENT_CONFIRM", "Payment", p.getId(), p.getState().name(), PaymentState.CONFIRMED.name()); // logs
    }

    public void rejectPayment(Payment p) throws Exception {
        Session.requireRole(Role.FINANCIER);
        financeDAO.updatePaymentState(p.getId(), PaymentState.REJECTED);

        AuditService.log("PAYMENT_REJECT", "Payment", p.getId(), p.getState().name(), PaymentState.REJECTED.name()); // logs
    }

    public void addExpense(Mission mission, String category, BigDecimal amount, java.time.LocalDate date, String ref) throws Exception {
        Session.requireRole(Role.FINANCIER);
        Expense e = new Expense();
        e.setMission(mission);
        e.setCategory(category);
        e.setAmount(amount);
        e.setExpenseDate(date);
        e.setReference(ref);
        financeDAO.insertExpense(e);

        AuditService.log("ADD_EXPENSE", "Expense", e.getId(), null, e.getAmount().toString()); // logs
    }

    public List<Payment> listPayments() throws SQLException {
        Session.touch();
        return financeDAO.findAllPayments();
    }

    public List<Expense> listExpenses() throws SQLException {
        Session.touch();
        return financeDAO.findAllExpenses();
    }
}