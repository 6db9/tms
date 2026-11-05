package com.tms.model;

import java.math.BigDecimal;

public class Payment {
    private int id;
    private Request request;
    private BigDecimal amount;
    private PaymentState state;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Request getRequest() { return request; }
    public void setRequest(Request request) { this.request = request; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public PaymentState getState() { return state; }
    public void setState(PaymentState state) { this.state = state; }
}