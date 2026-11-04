package com.tms.model;

public enum MissionState {
    TO_PLAN("À planifier"),
    PLANNED("Planifiée"),
    IN_PROGRESS("En cours"),
    ARRIVED("Arrivée"),
    WAITING("En attente"),
    UNLOADING("En déchargement"),
    DONE("Terminée"),
    CANCELLED("Annulée");

    private final String label;
    MissionState(String label) { this.label = label; }
    public String getLabel() { return label; }
}