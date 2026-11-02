package com.tms.model;

import java.math.BigDecimal;

public class Vehicle {
    private int id;
    private String plate;
    private String type;
    private BigDecimal capacityKg;
    private BigDecimal capacityM3;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public BigDecimal getCapacityKg() { return capacityKg; }
    public void setCapacityKg(BigDecimal capacityKg) { this.capacityKg = capacityKg; }
    public BigDecimal getCapacityM3() { return capacityM3; }
    public void setCapacityM3(BigDecimal capacityM3) { this.capacityM3 = capacityM3; }
}