package com.tms.model;

import java.time.LocalDateTime;

public class Mission {
    private int id;
    private Request request;
    private Vehicle vehicle;
    private Driver driver;
    private MissionState state;
    private LocalDateTime plannedStart;
    private LocalDateTime plannedEnd;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Request getRequest() { return request; }
    public void setRequest(Request request) { this.request = request; }
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }
    public MissionState getState() { return state; }
    public void setState(MissionState state) { this.state = state; }
    public LocalDateTime getPlannedStart() { return plannedStart; }
    public void setPlannedStart(LocalDateTime plannedStart) { this.plannedStart = plannedStart; }
    public LocalDateTime getPlannedEnd() { return plannedEnd; }
    public void setPlannedEnd(LocalDateTime plannedEnd) { this.plannedEnd = plannedEnd; }
}