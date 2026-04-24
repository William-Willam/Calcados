package com.shoestock.desktop.controller;

import com.shoestock.desktop.MainApp;
import com.shoestock.desktop.model.AuthResponse;
import com.shoestock.desktop.service.ApiService;
import com.shoestock.desktop.util.SessionManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Map;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    @FXML
    private void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // Validação básica no frontend
        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Preencha email e senha!");
            return;
        }

        // Desabilita o botão enquanto faz a requisição
        loginButton.setDisable(true);
        errorLabel.setText("Entrando...");

        // Faz a chamada em uma thread separada para não travar a interface
        new Thread(() -> {
            try {
                // Monta o body do login
                Map<String, String> body = Map.of(
                        "email", email,
                        "password", password
                );

                // Chama a API
                AuthResponse response = ApiService.post(
                        "/auth/login", body, AuthResponse.class);

                // Salva os dados na sessão
                SessionManager.setSession(
                        response.getToken(),
                        response.getRole(),
                        response.getName()
                );

                // Navega para a tela correta baseado no perfil
                // Platform.runLater garante que a atualização da UI
                // acontece na thread do JavaFX
                Platform.runLater(() -> {
                    try {
                        if ("GESTOR".equals(response.getRole())) {
                            MainApp.navigateTo("gestor.fxml",
                                    "ShoeStock — Gestor");
                        } else if ("ESTOQUISTA".equals(response.getRole())) {
                            MainApp.navigateTo("estoquista.fxml",
                                    "ShoeStock — Estoquista");
                        } else {
                            errorLabel.setText("Perfil sem acesso ao sistema!");
                            loginButton.setDisable(false);
                        }
                    } catch (Exception e) {
                        errorLabel.setText("Erro ao carregar tela!");
                        loginButton.setDisable(false);
                    }
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    errorLabel.setText("Email ou senha inválidos!");
                    loginButton.setDisable(false);
                });
            }
        }).start();
    }
}