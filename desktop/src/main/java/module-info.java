module com.shoestock.desktop {
    // Módulos do JavaFX necessários
    requires javafx.controls;
    requires javafx.fxml;

    // HTTP Client do Java 11+
    requires java.net.http;

    // Gson para converter JSON
    requires com.google.gson;

    // Lombok
    requires static lombok;

    // Exporta e abre os pacotes para o JavaFX conseguir acessar
    opens com.shoestock.desktop to javafx.graphics;
    opens com.shoestock.desktop.controller to javafx.fxml;
    opens com.shoestock.desktop.model to com.google.gson;
    opens com.shoestock.desktop.util to javafx.fxml;

    // Exporta os pacotes principais
    exports com.shoestock.desktop;
    exports com.shoestock.desktop.controller;
    exports com.shoestock.desktop.model;
    exports com.shoestock.desktop.service;
    exports com.shoestock.desktop.util;
}