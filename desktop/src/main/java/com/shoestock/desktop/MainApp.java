package com.shoestock.desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    // primaryStage é a janela principal da aplicação
    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        // Carrega a tela de login como tela inicial
        navigateTo("login.fxml", "ShoeStock — Login");
    }

    // Método estático para navegar entre telas
    // Qualquer controller pode chamar esse método para trocar de tela
    public static void navigateTo(String fxml, String title) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource(fxml)
        );
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}