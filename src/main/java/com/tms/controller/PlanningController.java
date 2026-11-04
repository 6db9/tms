package com.tms.controller;

import com.tms.dao.DriverDAO;
import com.tms.dao.VehicleDAO;
import com.tms.model.*;
import com.tms.service.PlanningService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PlanningController {

    @FXML private TableView<Mission> missionTable;
    @FXML private TableColumn<Mission, Integer> colId;
    @FXML private TableColumn<Mission, String> colReqId, colVehicle, colDriver, colStart, colEnd, colState;
    @FXML private TableColumn<Mission, String> colArrival, colWait, colUnload;

    @FXML private ComboBox<Request> requestCombo;
    @FXML private ComboBox<Vehicle> vehicleCombo;
    @FXML private ComboBox<Driver> driverCombo;
    @FXML private DatePicker startDatePicker, endDatePicker;
    @FXML private TextField startTimeField, endTimeField;
    @FXML private Label errorLabel;

    private final PlanningService service = new PlanningService();
    private final VehicleDAO vehicleDAO = new VehicleDAO();
    private final DriverDAO driverDAO = new DriverDAO();

    @FXML
    public void initialize() {
        // Setup Table
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colReqId.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cell.getValue().getRequest().getId())));
        colVehicle.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getVehicle() != null ? cell.getValue().getVehicle().getPlate() : "-"));
        colDriver.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDriver() != null ? cell.getValue().getDriver().getFullName() : "-"));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        colStart.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPlannedStart() != null ? cell.getValue().getPlannedStart().format(fmt) : "-"));
        colEnd.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getPlannedEnd() != null ? cell.getValue().getPlannedEnd().format(fmt) : "-"));
        colState.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getState().getLabel()));

        // Setup F8 Columns
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("dd/MM HH:mm");
        colArrival.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getActualArrival() != null ? cell.getValue().getActualArrival().format(timeFmt) : "—"
        ));

        colWait.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getWaitingMinutes() != null ? String.valueOf(cell.getValue().getWaitingMinutes()) : "—"
        ));

        colUnload.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getUnloadingMinutes() != null ? String.valueOf(cell.getValue().getUnloadingMinutes()) : "—"
        ));

        // Setup Combos (Converters)
        requestCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Request r) { return r == null ? "" : "Demande #" + r.getId() + " - " + r.getClient().getName(); }
            @Override public Request fromString(String s) { return null; }
        });
        vehicleCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Vehicle v) { return v == null ? "" : v.getPlate(); }
            @Override public Vehicle fromString(String s) { return null; }
        });
        driverCombo.setConverter(new StringConverter<>() {
            @Override public String toString(Driver d) { return d == null ? "" : d.getFullName(); }
            @Override public Driver fromString(String s) { return null; }
        });

        refreshData();
    }

    public void refreshData() {
        try {
            requestCombo.setItems(FXCollections.observableArrayList(service.getValidatedRequests()));
            vehicleCombo.setItems(FXCollections.observableArrayList(vehicleDAO.findAllActive()));
            driverCombo.setItems(FXCollections.observableArrayList(driverDAO.findAllActive()));
            missionTable.setItems(FXCollections.observableArrayList(service.listMissions()));
        } catch (Exception e) {
            errorLabel.setText("Erreur de chargement des données.");
        }
    }

    @FXML
    private void handlePlan() {
        errorLabel.setText("");

        Request req = requestCombo.getValue();
        Vehicle veh = vehicleCombo.getValue();
        Driver drv = driverCombo.getValue();

        if (req == null || veh == null || drv == null || startDatePicker.getValue() == null || endDatePicker.getValue() == null) {
            errorLabel.setText("Veuillez remplir tous les champs (Demande, Véhicule, Chauffeur, Dates).");
            return;
        }

        try {
            LocalDateTime start = parseDateTime(startDatePicker.getValue(), startTimeField.getText());
            LocalDateTime end = parseDateTime(endDatePicker.getValue(), endTimeField.getText());

            service.planMission(req, veh, drv, start, end);
            refreshData();

            // Clear inputs
            startTimeField.clear(); endTimeField.clear();
            startDatePicker.setValue(null); endDatePicker.setValue(null);

        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

    // --- F7/F8 Lifecycle Actions ---

    @FXML
    private void handleStart() {
        executeLifecycleAction(service::markInProgress);
    }

    @FXML
    private void handleArrive() {
        executeLifecycleAction(service::markArrived);
    }

    @FXML
    private void handleUnload() {
        executeLifecycleAction(service::startUnloading);
    }

    @FXML
    private void handleDone() {
        executeLifecycleAction(service::markDone);
    }

    private void executeLifecycleAction(UpdateAction action) {
        errorLabel.setText("");
        Mission selected = missionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Veuillez sélectionner une mission dans le tableau.");
            return;
        }
        try {
            action.execute(selected);
            refreshData(); // Reloads table to show new state & times
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FunctionalInterface
    private interface UpdateAction {
        void execute(Mission m) throws Exception;
    }

    // --- Date Parsing Helper ---

    private LocalDateTime parseDateTime(LocalDate date, String timeStr) throws Exception {
        if (timeStr == null || timeStr.isBlank()) {
            throw new Exception("Format d'heure invalide. Utilisez HH:MM (ex: 08:30).");
        }
        try {
            LocalTime time = LocalTime.parse(timeStr.trim(), DateTimeFormatter.ofPattern("HH:mm"));
            return LocalDateTime.of(date, time);
        } catch (Exception e) {
            throw new Exception("Format d'heure invalide. Utilisez HH:MM (ex: 08:30).");
        }
    }
}