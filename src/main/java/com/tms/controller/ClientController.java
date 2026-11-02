package com.tms.controller;

import com.tms.model.Client;
import com.tms.service.ClientService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ClientController {

    @FXML private TableView<Client> clientTable;
    @FXML private TableColumn<Client, String> colName, colPhone, colEmail, colTaxId;

    @FXML private TextField nameField, phoneField, emailField, taxIdField, addressField;
    @FXML private Label errorLabel;

    private final ClientService service = new ClientService();

    @FXML
    public void initialize() {
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTaxId.setCellValueFactory(new PropertyValueFactory<>("taxId"));
        refreshTable();
    }

    private void refreshTable() {
        try {
            clientTable.setItems(FXCollections.observableArrayList(service.listClients()));
        } catch (Exception e) {
            showError("Erreur de chargement : " + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        errorLabel.setText("");
        if (nameField.getText().isBlank()) {
            showError("Le nom est obligatoire.");
            return;
        }

        Client c = new Client();
        c.setName(nameField.getText().trim());
        c.setPhone(phoneField.getText().trim());
        c.setEmail(emailField.getText().trim());
        c.setTaxId(taxIdField.getText().trim());
        c.setAddress(addressField.getText().trim());

        try {
            service.createClient(c);
            nameField.clear(); phoneField.clear(); emailField.clear(); taxIdField.clear(); addressField.clear();
            refreshTable();
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void showError(String msg) {
        errorLabel.setText(msg);
    }
}