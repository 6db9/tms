package com.tms.controller;

import com.tms.model.*;
import com.tms.service.FinanceService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FinanceController {

    @FXML private TableView<Payment> paymentTable;
    @FXML private TableColumn<Payment, Integer> colPayId;
    @FXML private TableColumn<Payment, String> colPayReq, colPayClient, colPayAmount, colPayState;
    @FXML private Label payErrorLabel;

    @FXML private TableView<Expense> expenseTable;
    @FXML private TableColumn<Expense, Integer> colExpId;
    @FXML private TableColumn<Expense, String> colExpMission, colExpCat, colExpAmount, colExpDate;
    @FXML private TextField expMissionField, expCatField, expAmountField, expRefField;
    @FXML private DatePicker expDatePicker;
    @FXML private Label expErrorLabel;

    private final FinanceService service = new FinanceService();

    @FXML
    public void initialize() {
        // Payments Table
        colPayId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPayReq.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getRequest().getId())));
        colPayClient.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getRequest().getClient().getName()));
        colPayAmount.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAmount().toString()));
        colPayState.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getState().getLabel()));

        // Expenses Table
        colExpId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colExpMission.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getMission().getId())));
        colExpCat.setCellValueFactory(new PropertyValueFactory<>("category"));
        colExpAmount.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAmount().toString()));
        colExpDate.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getExpenseDate().toString()));

        refreshData();
    }

    public void refreshData() {
        try {
            paymentTable.setItems(FXCollections.observableArrayList(service.listPayments()));
            expenseTable.setItems(FXCollections.observableArrayList(service.listExpenses()));
        } catch (Exception e) {
            payErrorLabel.setText("Erreur de chargement.");
        }
    }

    @FXML
    private void handleConfirm() {
        payErrorLabel.setText("");
        Payment selected = paymentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            payErrorLabel.setText("Sélectionnez un paiement.");
            return;
        }
        try {
            service.confirmPayment(selected);
            refreshData();
        } catch (Exception e) {
            payErrorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleReject() {
        payErrorLabel.setText("");
        Payment selected = paymentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            payErrorLabel.setText("Sélectionnez un paiement.");
            return;
        }
        try {
            service.rejectPayment(selected);
            refreshData();
        } catch (Exception e) {
            payErrorLabel.setText(e.getMessage());
        }
    }

    @FXML
    private void handleAddExpense() {
        expErrorLabel.setText("");
        try {
            int missionId = Integer.parseInt(expMissionField.getText().trim());
            String cat = expCatField.getText().trim();
            BigDecimal amount = new BigDecimal(expAmountField.getText().trim());
            LocalDate date = expDatePicker.getValue();
            String ref = expRefField.getText().trim();

            if (date == null) throw new Exception("La date est obligatoire.");

            Mission m = new Mission();
            m.setId(missionId);

            service.addExpense(m, cat, amount, date, ref);
            refreshData();

            expMissionField.clear(); expCatField.clear(); expAmountField.clear(); expRefField.clear();
        } catch (NumberFormatException e) {
            expErrorLabel.setText("ID Mission ou Montant invalide.");
        } catch (Exception e) {
            expErrorLabel.setText(e.getMessage());
        }
    }
}