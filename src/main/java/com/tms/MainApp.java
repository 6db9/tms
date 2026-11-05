package com.tms;

import com.tms.controller.FinanceController;
import com.tms.controller.LoginController;
import com.tms.controller.PlanningController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

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
            PlanningController planCtrl = loader4.getController();
            tab4.setOnSelectionChanged(event -> {
                if (tab4.isSelected()) planCtrl.refreshData();
            });

            // Tab 5: Finance
            FXMLLoader loader5 = new FXMLLoader(getClass().getResource("/com/tms/view/FinanceView.fxml"));
            Tab tab5 = new Tab("Finance", loader5.load());
            FinanceController finCtrl = loader5.getController();
            tab5.setOnSelectionChanged(event -> {
                if (tab5.isSelected()) finCtrl.refreshData();
            });

            // Add all tabs ONCE
            tabPane.getTabs().addAll(tab1, tab2, tab3, tab4, tab5);
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