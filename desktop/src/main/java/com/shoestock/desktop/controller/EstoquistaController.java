package com.shoestock.desktop.controller;

import com.shoestock.desktop.MainApp;
import com.shoestock.desktop.model.Product;
import com.shoestock.desktop.service.ApiService;
import com.shoestock.desktop.util.SessionManager;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class EstoquistaController implements Initializable {

    // Campos do formulário
    @FXML private Label welcomeLabel;
    @FXML private TextField nomeField;
    @FXML private TextField precoField;
    @FXML private TextField quantidadeField;
    @FXML private TextField descricaoField;
    @FXML private TextField imagemField;
    @FXML private ComboBox<String> tipoField;
    @FXML private Label msgLabel;

    // Tabela
    @FXML private TableView<Product> tabelaProdutos;
    @FXML private TableColumn<Product, String> colId;
    @FXML private TableColumn<Product, String> colNome;
    @FXML private TableColumn<Product, String> colTipo;
    @FXML private TableColumn<Product, String> colPreco;
    @FXML private TableColumn<Product, String> colQtd;
    @FXML private TableColumn<Product, String> colDesc;
    @FXML private TableColumn<Product, String> colAcoes;

    // ID do produto sendo editado (null = novo produto)
    private Long editandoId = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        welcomeLabel.setText("Bem-vindo, " + SessionManager.getName());

        // Opções do ComboBox de tipo
        tipoField.setItems(FXCollections.observableArrayList(
                "FEMININO", "MASCULINO"));

        configurarColunas();
        carregarProdutos();
    }

    private void configurarColunas() {
        colId.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getId())));
        colNome.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getName()));
        colTipo.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getType()));
        colPreco.setCellValueFactory(c ->
                new SimpleStringProperty(
                        "R$ " + String.format("%.2f", c.getValue().getPrice())));
        colQtd.setCellValueFactory(c ->
                new SimpleStringProperty(
                        String.valueOf(c.getValue().getQuantity())));
        colDesc.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDescription()));

        // Coluna de ações — estoquista só pode editar, não deletar
        colAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar = new Button("✏️ Editar");

            {
                btnEditar.setStyle("-fx-background-color: #0f3460; " +
                        "-fx-text-fill: white; -fx-cursor: hand; " +
                        "-fx-background-radius: 5;");

                btnEditar.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    preencherFormulario(product);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnEditar);
            }
        });
    }

    private void carregarProdutos() {
        new Thread(() -> {
            try {
                List<Product> lista = ApiService.getList(
                        "/products", Product.class);
                Platform.runLater(() ->
                        tabelaProdutos.setItems(
                                FXCollections.observableArrayList(lista)));
            } catch (Exception e) {
                Platform.runLater(() -> {
                    msgLabel.setStyle("-fx-text-fill: #e94560;");
                    msgLabel.setText("Erro ao carregar produtos!");
                });
            }
        }).start();
    }

    @FXML
    private void handleSalvarProduto() {
        String nome = nomeField.getText().trim();
        String precoStr = precoField.getText().trim();
        String qtdStr = quantidadeField.getText().trim();
        String tipo = tipoField.getValue();

        // Validações
        if (nome.isEmpty() || precoStr.isEmpty() ||
                qtdStr.isEmpty() || tipo == null) {
            mostrarErro("Preencha todos os campos obrigatórios!");
            return;
        }

        // Valida se preço e quantidade são números válidos
        BigDecimal preco;
        int quantidade;
        try {
            preco = new BigDecimal(precoStr.replace(",", "."));
            quantidade = Integer.parseInt(qtdStr);
        } catch (NumberFormatException e) {
            mostrarErro("Preço ou quantidade inválidos!");
            return;
        }

        Map<String, Object> body = Map.of(
                "name", nome,
                "description", descricaoField.getText().trim(),
                "price", preco,
                "imageUrl", imagemField.getText().trim(),
                "quantity", quantidade,
                "type", tipo
        );

        new Thread(() -> {
            try {
                if (editandoId == null) {
                    ApiService.post("/products", body, Product.class);
                    Platform.runLater(() -> {
                        mostrarSucesso("Produto cadastrado com sucesso!");
                        handleLimpar();
                        carregarProdutos();
                    });
                } else {
                    ApiService.put("/products/" + editandoId,
                            body, Product.class);
                    Platform.runLater(() -> {
                        mostrarSucesso("Produto atualizado com sucesso!");
                        handleLimpar();
                        carregarProdutos();
                    });
                }
            } catch (Exception e) {
                Platform.runLater(() ->
                        mostrarErro("Erro ao salvar: " + e.getMessage()));
            }
        }).start();
    }

    @FXML
    private void handleLimpar() {
        editandoId = null;
        nomeField.clear();
        precoField.clear();
        quantidadeField.clear();
        descricaoField.clear();
        imagemField.clear();
        tipoField.setValue(null);
        msgLabel.setText("");
    }

    private void preencherFormulario(Product product) {
        editandoId = product.getId();
        nomeField.setText(product.getName());
        precoField.setText(String.valueOf(product.getPrice()));
        quantidadeField.setText(String.valueOf(product.getQuantity()));
        descricaoField.setText(product.getDescription());
        imagemField.setText(product.getImageUrl());
        tipoField.setValue(product.getType());
        mostrarInfo("Editando: " + product.getName());
    }

    @FXML
    private void handleLogout() {
        SessionManager.clear();
        try {
            MainApp.navigateTo("login.fxml", "ShoeStock — Login");
        } catch (Exception e) {
            mostrarErro("Erro ao sair!");
        }
    }

    private void mostrarErro(String msg) {
        msgLabel.setStyle("-fx-text-fill: #e94560;");
        msgLabel.setText(msg);
    }

    private void mostrarSucesso(String msg) {
        msgLabel.setStyle("-fx-text-fill: #4caf50;");
        msgLabel.setText(msg);
    }

    private void mostrarInfo(String msg) {
        msgLabel.setStyle("-fx-text-fill: #a8a8b3;");
        msgLabel.setText(msg);
    }
}