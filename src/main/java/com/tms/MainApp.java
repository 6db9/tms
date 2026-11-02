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
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/com/tms/view/ClientsView.fxml"));
            Tab tab1 = new Tab("Clients", loader1.load());

            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/com/tms/view/FleetView.fxml"));
            Tab tab2 = new Tab("Flotte (Véhicules & Chauffeurs)", loader2.load());

            tabPane.getTabs().addAll(tab1, tab2);
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

            primaryStage.setScene(new Scene(tabPane, 900, 600));
            primaryStage.setTitle("TMS - Tableau de bord");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}