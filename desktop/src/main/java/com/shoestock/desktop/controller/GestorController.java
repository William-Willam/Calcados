package com.shoestock.desktop.controller;

import com.shoestock.desktop.MainApp;
import com.shoestock.desktop.model.Product;
import com.shoestock.desktop.model.User;
import com.shoestock.desktop.service.ApiService;
import com.shoestock.desktop.util.SessionManager;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

// Initializable permite executar código ao carregar a tela
public class GestorController implements Initializable {

    // ==========================================
    // Campos da aba Estoquistas
    // ==========================================
    @FXML private Label welcomeLabel;
    @FXML private TextField nomeField;
    @FXML private TextField emailEstField;
    @FXML private PasswordField senhaEstField;
    @FXML private Label msgEstLabel;
    @FXML private TableView<User> tabelaEstoquistas;
    @FXML private TableColumn<User, String> colEstId;
    @FXML private TableColumn<User, String> colEstNome;
    @FXML private TableColumn<User, String> colEstEmail;
    @FXML private TableColumn<User, String> colEstAcoes;

    // ==========================================
    // Campos da aba Produtos
    // ==========================================
    @FXML private TableView<Product> tabelaProdutos;
    @FXML private TableColumn<Product, String> colProdId;
    @FXML private TableColumn<Product, String> colProdNome;
    @FXML private TableColumn<Product, String> colProdTipo;
    @FXML private TableColumn<Product, String> colProdPreco;
    @FXML private TableColumn<Product, String> colProdQtd;
    @FXML private TableColumn<Product, String> colProdAcoes;
    @FXML private ComboBox<String> filtroTipo;

    // Guarda o ID do estoquista sendo editado (null = novo cadastro)
    private Long editandoId = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Exibe o nome do gestor logado
        welcomeLabel.setText("Bem-vindo, " + SessionManager.getName());

        // Configura as colunas das tabelas
        configurarColunaEstoquistas();
        configurarColunasProdutos();

        // Popula o filtro de tipo
        filtroTipo.setItems(FXCollections.observableArrayList(
                "TODOS", "FEMININO", "MASCULINO"));
        filtroTipo.setValue("TODOS");

        // Carrega os dados ao abrir a tela
        carregarEstoquistas();
        carregarProdutos(null);
    }

    // ==========================================
    // Configuração das colunas
    // ==========================================
    private void configurarColunaEstoquistas() {
        // SimpleStringProperty converte o valor do campo para String
        colEstId.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getId())));
        colEstNome.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getName()));
        colEstEmail.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getEmail()));

        // Coluna de ações com botões Editar e Excluir
        colEstAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar = new Button("✏️");
            private final Button btnExcluir = new Button("🗑️");
            private final HBox box = new HBox(5, btnEditar, btnExcluir);

            {
                // Estilo dos botões
                btnEditar.setStyle("-fx-background-color: #0f3460; " +
                        "-fx-text-fill: white; -fx-cursor: hand;");
                btnExcluir.setStyle("-fx-background-color: #e94560; " +
                        "-fx-text-fill: white; -fx-cursor: hand;");

                btnEditar.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    preencherFormulario(user);
                });

                btnExcluir.setOnAction(e -> {
                    User user = getTableView().getItems().get(getIndex());
                    confirmarExclusaoEstoquista(user);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
            }
        });
    }

    private void configurarColunasProdutos() {
        colProdId.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getId())));
        colProdNome.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getName()));
        colProdTipo.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getType()));
        colProdPreco.setCellValueFactory(c ->
                new SimpleStringProperty("R$ " +
                        String.format("%.2f", c.getValue().getPrice())));
        colProdQtd.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getQuantity())));

        // Coluna de ações com botão Excluir (somente gestor pode excluir)
        colProdAcoes.setCellFactory(col -> new TableCell<>() {
            private final Button btnExcluir = new Button("🗑️");

            {
                btnExcluir.setStyle("-fx-background-color: #e94560; " +
                        "-fx-text-fill: white; -fx-cursor: hand;");
                btnExcluir.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    confirmarExclusaoProduto(product);
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnExcluir);
            }
        });
    }

    // ==========================================
    // Carregar dados
    // ==========================================
    private void carregarEstoquistas() {
        new Thread(() -> {
            try {
                List<User> lista = ApiService.getList(
                        "/users/estoquistas", User.class);
                Platform.runLater(() ->
                        tabelaEstoquistas.setItems(
                                FXCollections.observableArrayList(lista)));
            } catch (Exception e) {
                Platform.runLater(() ->
                        msgEstLabel.setText("Erro ao carregar estoquistas!"));
            }
        }).start();
    }

    private void carregarProdutos(String tipo) {
        new Thread(() -> {
            try {
                String endpoint = "/products";
                if (tipo != null && !tipo.equals("TODOS")) {
                    endpoint += "?type=" + tipo;
                }
                List<Product> lista = ApiService.getList(endpoint, Product.class);
                Platform.runLater(() ->
                        tabelaProdutos.setItems(
                                FXCollections.observableArrayList(lista)));
            } catch (Exception e) {
                Platform.runLater(() ->
                        msgEstLabel.setText("Erro ao carregar produtos!"));
            }
        }).start();
    }

    // ==========================================
    // Ações do formulário de estoquistas
    // ==========================================
    @FXML
    private void handleSalvarEstoquista() {
        String nome = nomeField.getText().trim();
        String email = emailEstField.getText().trim();
        String senha = senhaEstField.getText().trim();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            msgEstLabel.setStyle("-fx-text-fill: #e94560;");
            msgEstLabel.setText("Preencha todos os campos!");
            return;
        }

        Map<String, Object> body = Map.of(
                "name", nome,
                "email", email,
                "password", senha,
                "role", "ESTOQUISTA"
        );

        new Thread(() -> {
            try {
                if (editandoId == null) {
                    // Novo cadastro
                    ApiService.post("/users", body, User.class);
                    Platform.runLater(() -> {
                        msgEstLabel.setStyle("-fx-text-fill: #4caf50;");
                        msgEstLabel.setText("Estoquista cadastrado com sucesso!");
                        handleLimparEstoquista();
                        carregarEstoquistas();
                    });
                } else {
                    // Atualização
                    ApiService.put("/users/" + editandoId, body, User.class);
                    Platform.runLater(() -> {
                        msgEstLabel.setStyle("-fx-text-fill: #4caf50;");
                        msgEstLabel.setText("Estoquista atualizado com sucesso!");
                        handleLimparEstoquista();
                        carregarEstoquistas();
                    });
                }
            } catch (Exception e) {
                Platform.runLater(() -> {
                    msgEstLabel.setStyle("-fx-text-fill: #e94560;");
                    msgEstLabel.setText("Erro: " + e.getMessage());
                });
            }
        }).start();
    }

    @FXML
    private void handleLimparEstoquista() {
        editandoId = null;
        nomeField.clear();
        emailEstField.clear();
        senhaEstField.clear();
        msgEstLabel.setText("");
    }

    // Preenche o formulário com os dados do estoquista selecionado
    private void preencherFormulario(User user) {
        editandoId = user.getId();
        nomeField.setText(user.getName());
        emailEstField.setText(user.getEmail());
        senhaEstField.clear();
        msgEstLabel.setStyle("-fx-text-fill: #a8a8b3;");
        msgEstLabel.setText("Editando: " + user.getName());
    }

    // Mostra um diálogo de confirmação antes de excluir
    private void confirmarExclusaoEstoquista(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar exclusão");
        alert.setHeaderText("Excluir estoquista");
        alert.setContentText("Deseja excluir " + user.getName() + "?");

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                new Thread(() -> {
                    try {
                        ApiService.delete("/users/" + user.getId());
                        Platform.runLater(() -> {
                            msgEstLabel.setStyle("-fx-text-fill: #4caf50;");
                            msgEstLabel.setText("Excluído com sucesso!");
                            carregarEstoquistas();
                        });
                    } catch (Exception e) {
                        Platform.runLater(() -> {
                            msgEstLabel.setStyle("-fx-text-fill: #e94560;");
                            msgEstLabel.setText("Erro ao excluir!");
                        });
                    }
                }).start();
            }
        });
    }

    private void confirmarExclusaoProduto(Product product) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar exclusão");
        alert.setHeaderText("Excluir produto");
        alert.setContentText("Deseja excluir " + product.getName() + "?");

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                new Thread(() -> {
                    try {
                        ApiService.delete("/products/" + product.getId());
                        Platform.runLater(() -> carregarProdutos(
                                filtroTipo.getValue()));
                    } catch (Exception e) {
                        Platform.runLater(() ->
                                msgEstLabel.setText("Erro ao excluir produto!"));
                    }
                }).start();
            }
        });
    }

    @FXML
    private void handleFiltrarProdutos() {
        carregarProdutos(filtroTipo.getValue());
    }

    @FXML
    private void handleLogout() {
        SessionManager.clear();
        try {
            MainApp.navigateTo("login.fxml", "ShoeStock — Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}