package com.tms.controller;

import com.tms.model.Driver;
import com.tms.model.Vehicle;
import com.tms.service.FleetService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;

public class FleetController {

    // Vehicles UI
    @FXML private TableView<Vehicle> vehicleTable;
    @FXML private TableColumn<Vehicle, String> colPlate, colVType;
    @FXML private TableColumn<Vehicle, BigDecimal> colCapKg;
    @FXML private TextField plateField, vTypeField, capKgField;
    @FXML private Label vErrorLabel;

    // Drivers UI
    @FXML private TableView<Driver> driverTable;
    @FXML private TableColumn<Driver, String> colDName, colDPhone, colLicense;
    @FXML private TableColumn<Driver, String> colValidUntil; // Display as string for simplicity
    @FXML private TextField dNameField, dPhoneField, licenseField;
    @FXML private DatePicker validUntilPicker;
    @FXML private Label dErrorLabel;

    private final FleetService service = new FleetService();

    @FXML
    public void initialize() {
        // Setup Vehicle Table
        colPlate.setCellValueFactory(new PropertyValueFactory<>("plate"));
        colVType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colCapKg.setCellValueFactory(new PropertyValueFactory<>("capacityKg"));

        // Setup Driver Table
        colDName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colDPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colLicense.setCellValueFactory(new PropertyValueFactory<>("licenseNumber"));
        colValidUntil.setCellValueFactory(cellData -> {
            if (cellData.getValue().getLicenseValidUntil() != null) {
                return new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLicenseValidUntil().toString());
            }
            return new javafx.beans.property.SimpleStringProperty("Inconnu");
        });

        refreshTables();
    }

    private void refreshTables() {
        try {
            vehicleTable.setItems(FXCollections.observableArrayList(service.listVehicles()));
            driverTable.setItems(FXCollections.observableArrayList(service.listDrivers()));
        } catch (Exception e) {
            vErrorLabel.setText("Erreur de chargement.");
        }
    }

    @FXML
    private void handleSaveVehicle() {
        vErrorLabel.setText("");
        if (plateField.getText().isBlank()) {
            vErrorLabel.setText("L'immatriculation est obligatoire.");
            return;
        }

        Vehicle v = new Vehicle();
        v.setPlate(plateField.getText().trim());
        v.setType(vTypeField.getText().trim());

        try {
            if (!capKgField.getText().isBlank()) {
                v.setCapacityKg(new BigDecimal(capKgField.getText().trim()));
            }
            service.createVehicle(v);
            plateField.clear(); vTypeField.clear(); capKgField.clear();
            refreshTables();
        } catch (NumberFormatException e) {
            vErrorLabel.setText("La capacité doit être un nombre valide.");
        } catch (Exception e) {
            vErrorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleSaveDriver() {
        dErrorLabel.setText("");
        if (dNameField.getText().isBlank()) {
            dErrorLabel.setText("Le nom est obligatoire.");
            return;
        }

        Driver d = new Driver();
        d.setFullName(dNameField.getText().trim());
        d.setPhone(dPhoneField.getText().trim());
        d.setLicenseNumber(licenseField.getText().trim());
        d.setLicenseValidUntil(validUntilPicker.getValue());

        try {
            service.createDriver(d);
            dNameField.clear(); dPhoneField.clear(); licenseField.clear(); validUntilPicker.setValue(null);
            refreshTables();
        } catch (Exception e) {
            dErrorLabel.setText(e.getMessage());
        }
    }
}