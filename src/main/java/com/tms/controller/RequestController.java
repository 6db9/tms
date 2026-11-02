package com.tms.controller;

import com.tms.dao.ClientDAO;
import com.tms.model.Client;
import com.tms.model.Request;
import com.tms.model.RequestType;
import com.tms.service.RequestService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.time.format.DateTimeFormatter;

public class RequestController {

    @FXML private TableView<Request> requestTable;
    @FXML private TableColumn<Request, Integer> colId;
    @FXML private TableColumn<Request, String> colClient, colType, colState, colDate;

    @FXML private ComboBox<Client> clientCombo;
    @FXML private ComboBox<RequestType> typeCombo;
    @FXML private Label errorLabel;

    private final RequestService service = new RequestService();
    private final ClientDAO clientDAO = new ClientDAO();

    @FXML
    public void initialize() {
        // Setup Table
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colClient.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getClient().getName()));
        colType.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getType().getLabel()));
        colState.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getState().getLabel()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        colDate.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCreatedAt().format(formatter)));

        // Setup Combos
        typeCombo.setItems(FXCollections.observableArrayList(RequestType.values()));

        // show clients by name
        clientCombo.setConverter(new StringConverter<Client>() {
            @Override
            public String toString(Client client) {
                return client == null ? "" : client.getName();
            }
            @Override
            public Client fromString(String string) { return null; }
        });

        // 3. Load initial data
        refreshClientCombo();
        refreshTable();
    }

    public void refreshClientCombo() {
        try {
            Client currentSelection = clientCombo.getValue();

            clientCombo.setItems(FXCollections.observableArrayList(clientDAO.findAllActive()));

            if (currentSelection != null) {
                clientCombo.getSelectionModel().select(currentSelection);
            }
        } catch (Exception e) {
            errorLabel.setText("Erreur lors du chargement des clients.");
        }
    }

    private void refreshTable() {
        try {
            requestTable.setItems(FXCollections.observableArrayList(service.listRequests()));
        } catch (Exception e) {
            errorLabel.setText("Erreur de chargement.");
        }
    }

    @FXML
    private void handleCreate() {
        errorLabel.setText("");

        refreshClientCombo();

        Client selectedClient = clientCombo.getValue();
        RequestType selectedType = typeCombo.getValue();

        if (selectedClient == null || selectedType == null) {
            errorLabel.setText("Veuillez sélectionner un client et un type.");
            return;
        }

        Request r = new Request();
        r.setClient(selectedClient);
        r.setType(selectedType);

        try {
            service.createRequest(r);
            refreshTable();
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleValidate() {
        errorLabel.setText("");
        Request selected = requestTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Veuillez sélectionner une demande dans le tableau.");
            return;
        }

        try {
            service.validateRequest(selected);
            refreshTable();
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        errorLabel.setText("");
        Request selected = requestTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorLabel.setText("Veuillez sélectionner une demande dans le tableau.");
            return;
        }

        try {
            service.cancelRequest(selected);
            refreshTable();
        } catch (Exception e) {
            errorLabel.setText(e.getMessage());
        }
    }
}