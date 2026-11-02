package com.tms.service;

import com.tms.dao.DriverDAO;
import com.tms.dao.VehicleDAO;
import com.tms.model.Driver;
import com.tms.model.Role;
import com.tms.model.Vehicle;
import com.tms.util.Session;

import java.sql.SQLException;
import java.util.List;

public class FleetService {

    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final DriverDAO driverDAO = new DriverDAO();

    // --- Vehicles ---
    public void createVehicle(Vehicle v) throws Exception {
        Session.requireRole(Role.ADMIN); // Or COORD
        if (vehicleDAO.existsPlate(v.getPlate())) {
            throw new Exception("Un véhicule avec cette immatriculation existe déjà.");
        }
        vehicleDAO.insert(v);
    }

    public List<Vehicle> listVehicles() throws SQLException {
        Session.touch();
        return vehicleDAO.findAllActive();
    }

    // --- Drivers ---
    public void createDriver(Driver d) throws Exception {
        Session.requireRole(Role.ADMIN);
        driverDAO.insert(d);
    }

    public List<Driver> listDrivers() throws SQLException {
        Session.touch();
        return driverDAO.findAllActive();
    }
}