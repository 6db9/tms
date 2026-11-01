package org.example.tms;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private final TextField display = new TextField();
    private double firstOperand = 0;
    private String pendingOp = null;
    private boolean startNewNumber = true;

    @Override
    public void start(Stage stage) {
        display.setEditable(false);
        display.setPrefHeight(50);
        display.setStyle("-fx-font-size: 20px;");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(5);
        grid.setVgap(5);

        // Row 0: display spans 4 columns
        grid.add(display, 0, 0, 4, 1);

        // Buttons layout
        String[][] keys = {
                {"7", "8", "9", "/"},
                {"4", "5", "6", "*"},
                {"1", "2", "3", "-"},
                {"0", ".", "=", "+"}
        };

        for (int r = 0; r < keys.length; r++) {
            for (int c = 0; c < keys[r].length; c++) {
                String label = keys[r][c];
                Button btn = new Button(label);
                btn.setPrefSize(60, 60);
                btn.setOnAction(e -> onButton(label));
                grid.add(btn, c, r + 1);
            }
        }

        Button clear = new Button("C");
        clear.setPrefSize(260, 40);
        clear.setOnAction(e -> {
            display.clear();
            firstOperand = 0;
            pendingOp = null;
            startNewNumber = true;
        });
        grid.add(clear, 0, 5, 4, 1);

        Scene scene = new Scene(grid, 280, 360);
        stage.setTitle("Calculatrice");
        stage.setScene(scene);
        stage.show();
    }

    private void onButton(String label) {
        if (label.matches("[0-9.]")) {
            if (startNewNumber) {
                display.setText(label.equals(".") ? "0." : label);
                startNewNumber = false;
            } else {
                display.appendText(label);
            }
        } else if (label.equals("=")) {
            compute();
            pendingOp = null;
            startNewNumber = true;
        } else { // + - * /
            firstOperand = Double.parseDouble(display.getText().isEmpty() ? "0" : display.getText());
            pendingOp = label;
            startNewNumber = true;
        }
    }

    private void compute() {
        if (pendingOp == null) return;
        double second = Double.parseDouble(display.getText().isEmpty() ? "0" : display.getText());
        double result = switch (pendingOp) {
            case "+" -> firstOperand + second;
            case "-" -> firstOperand - second;
            case "*" -> firstOperand * second;
            case "/" -> second == 0 ? Double.NaN : firstOperand / second;
            default -> second;
        };
        display.setText(String.valueOf(result));
    }

    public static void main(String[] args) {
        launch(args);
    }
}