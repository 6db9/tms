package com.tms.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense {
    private int id;
    private Mission mission;
    private String category;
    private BigDecimal amount;
    private LocalDate expenseDate;
    private String reference;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Mission getMission() { return mission; }
    public void setMission(Mission mission) { this.mission = mission; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDate getExpenseDate() { return expenseDate; }
    public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
}