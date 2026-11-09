package com.tms.controller;

import com.tms.dao.AuditDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AuditController {

    @FXML private TableView<String[]> auditTable;
    @FXML private TableColumn<String[], String> colDate, colUser, colAction, colEntity, colId, colOld, colNew;

    private final AuditDAO dao = new AuditDAO();

    @FXML
    public void initialize() {
        colDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0]));
        colUser.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1]));
        colAction.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2]));
        colEntity.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[3]));
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[4]));
        colOld.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[5]));
        colNew.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[6]));

        loadData();
    }

    public void loadData() {
        try {
            auditTable.setItems(FXCollections.observableArrayList(dao.findAllLogs()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}