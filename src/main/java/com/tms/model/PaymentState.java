package com.tms.model;

public enum PaymentState {
    ANNOUNCED("Annoncé"),
    PENDING("En attente"),
    CONFIRMED("Confirmé"),
    REJECTED("Rejeté"),
    CANCELLED("Annulé");

    private final String label;
    PaymentState(String label) { this.label = label; }
    public String getLabel() { return label; }
}