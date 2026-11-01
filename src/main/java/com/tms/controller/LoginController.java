package com.tms.controller;

import com.tms.model.User;
import com.tms.service.AuthException;
import com.tms.service.AuthService;
import com.tms.util.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final AuthService authService = new AuthService();
    private Runnable onSuccess;

    public void setOnSuccess(Runnable onSuccess) {
        this.onSuccess = onSuccess;
    }

    @FXML
    private void onLogin() {
        errorLabel.setText("");
        try {
            User user = authService.login(usernameField.getText(), passwordField.getText());
            Session.start(user);
            if (onSuccess != null) onSuccess.run();
        } catch (AuthException e) {
            errorLabel.setText(e.getMessage());
            passwordField.clear();
            passwordField.requestFocus();
        }
    }

    @FXML
    private void initialize() {
        // Wire the button programmatically since FXML action wiring is via #onLogin
        // (we rely on fx:controller auto-binding: use onAction="#onLogin" in FXML instead)
    }
}