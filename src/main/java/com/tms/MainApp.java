package com.tms;

import com.tms.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tms/view/ClientsView.fxml"));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root, 700, 500));
            primaryStage.setTitle("TMS - Gestion des Clients");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}