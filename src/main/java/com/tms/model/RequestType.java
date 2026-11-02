package com.tms.model;

public enum RequestType {
    TRANSPORT_ONLY("Transport seul"),
    SALE_WITH_DELIVERY("Vente avec livraison");

    private final String label;
    RequestType(String label) { this.label = label; }
    public String getLabel() { return label; }
}