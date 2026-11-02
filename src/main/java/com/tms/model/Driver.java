package com.tms.model;

import java.time.LocalDate;

public class Driver {
    private int id;
    private String fullName;
    private String phone;
    private String licenseNumber;
    private LocalDate licenseValidUntil;

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    public LocalDate getLicenseValidUntil() { return licenseValidUntil; }
    public void setLicenseValidUntil(LocalDate licenseValidUntil) { this.licenseValidUntil = licenseValidUntil; }
}