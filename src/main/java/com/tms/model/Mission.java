package com.tms.model;

import java.time.LocalDateTime;
import java.time.Duration;

public class Mission {
    private int id;
    private Request request;
    private Vehicle vehicle;
    private Driver driver;
    private MissionState state;
    private LocalDateTime plannedStart;
    private LocalDateTime plannedEnd;

    private LocalDateTime actualArrival;
    private LocalDateTime actualStart;
    private LocalDateTime actualEnd;

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

    public LocalDateTime getActualArrival() { return actualArrival; }
    public void setActualArrival(LocalDateTime actualArrival) { this.actualArrival = actualArrival; }
    public LocalDateTime getActualStart() { return actualStart; }
    public void setActualStart(LocalDateTime actualStart) { this.actualStart = actualStart; }
    public LocalDateTime getActualEnd() { return actualEnd; }
    public void setActualEnd(LocalDateTime actualEnd) { this.actualEnd = actualEnd; }

    public Long getWaitingMinutes() {
        if (actualArrival == null || actualStart == null) return null;
        return Duration.between(actualArrival, actualStart).toMinutes();
    }

    public Long getUnloadingMinutes() {
        if (actualStart == null || actualEnd == null) return null;
        return Duration.between(actualStart, actualEnd).toMinutes();
    }
}