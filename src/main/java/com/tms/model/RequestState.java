package com.tms.model;

public enum RequestState {
    DRAFT("Brouillon"),
    VALIDATED("Validée"),
    CLOSED("Clôturée"),
    CANCELLED("Annulée");

    private final String label;
    RequestState(String label) { this.label = label; }
    public String getLabel() { return label; }
}