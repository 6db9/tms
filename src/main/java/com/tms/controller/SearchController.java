package com.tms.controller;

import com.tms.model.Request;
import com.tms.service.RequestService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class SearchController {

    @FXML private TextField searchField;
    @FXML private TableView<Request> searchTable;
    @FXML private TableColumn<Request, Integer> colId;
    @FXML private TableColumn<Request, String> colClient, colType, colState, colWeight, colDate;

    private final RequestService service = new RequestService();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colClient.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getClient().getName()));
        colType.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType().getLabel()));
        colState.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getState().getLabel()));

        // F4: Display weight or "Inconnu"
        colWeight.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getTotalWeight() != null ? c.getValue().getTotalWeight() + " kg" : "Inconnu"
        ));

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        colDate.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCreatedAt().format(fmt)));

        loadData();
    }

    public void loadData() {
        try {
            List<Request> all = service.listRequests();
            searchTable.setItems(FXCollections.observableArrayList(all));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().toLowerCase().trim();
        if (query.isEmpty()) {
            handleReset();
            return;
        }

        try {
            List<Request> all = service.listRequests();

            // F10: Filter by client name, request ID, or load description
            List<Request> filtered = all.stream().filter(r ->
                    r.getClient().getName().toLowerCase().contains(query) ||
                            String.valueOf(r.getId()).equals(query) ||
                            (r.getLoadDescription() != null && r.getLoadDescription().toLowerCase().contains(query))
            ).collect(Collectors.toList());

            searchTable.setItems(FXCollections.observableArrayList(filtered));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        loadData();
    }
}