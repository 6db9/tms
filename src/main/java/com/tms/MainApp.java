package com.tms;

import com.tms.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainApp extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        stage.setTitle("TMS — Transport Management System");
        showLogin();
        stage.show();
    }

    private void showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/tms/view/LoginView.fxml"));
            Parent root = loader.load();
            LoginController ctrl = loader.getController();
            ctrl.setOnSuccess(this::showDashboard);
            primaryStage.setScene(new Scene(root, 400, 280));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot load LoginView.fxml", e);
        }
    }

    private void showDashboard() {
        TabPane tabPane = new TabPane();

        try {
            // Tab 1: Clients
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/com/tms/view/ClientsView.fxml"));
            Tab tab1 = new Tab("Clients", loader1.load());

            // Tab 2: Fleet
            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/com/tms/view/FleetView.fxml"));
            Tab tab2 = new Tab("Flotte (Véhicules & Chauffeurs)", loader2.load());

            // Tab 3: Requests
            FXMLLoader loader3 = new FXMLLoader(getClass().getResource("/com/tms/view/RequestsView.fxml"));
            Tab tab3 = new Tab("Demandes", loader3.load());

            // Tab 4: Planning
            FXMLLoader loader4 = new FXMLLoader(getClass().getResource("/com/tms/view/PlanningView.fxml"));
            Tab tab4 = new Tab("Planning & Missions", loader4.load());

            // Refresh Planning Combos when tab is clicked
            com.tms.controller.PlanningController planCtrl = loader4.getController();
            tab4.setOnSelectionChanged(event -> {
                if (tab4.isSelected()) {
                    planCtrl.refreshData();
                }
            });

            tabPane.getTabs().addAll(tab1, tab2, tab3, tab4);
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

            primaryStage.setScene(new Scene(tabPane, 1100, 650));
            primaryStage.setTitle("TMS - Tableau de bord");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}