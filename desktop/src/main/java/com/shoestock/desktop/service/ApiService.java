package com.shoestock.desktop.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shoestock.desktop.util.SessionManager;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

// Centraliza todas as chamadas HTTP para a API Spring Boot
public class ApiService {

    // URL base da API — aponta para o backend rodando localmente
    private static final String BASE_URL = "http://localhost:8080/api";

    // HttpClient do Java 11+ para fazer requisições HTTP
    private static final HttpClient client = HttpClient.newHttpClient();

    // Gson converte JSON → objeto Java e objeto Java → JSON
    private static final Gson gson = new Gson();

    // ==========================================
    // Método genérico para requisições GET
    // ==========================================
    public static <T> T get(String endpoint, Class<T> responseType) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                // Adiciona o token JWT no cabeçalho Authorization
                .header("Authorization", "Bearer " + SessionManager.getToken())
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Erro na requisição: " + response.statusCode());
        }

        return gson.fromJson(response.body(), responseType);
    }

    // ==========================================
    // Método genérico para listas (GET que retorna array)
    // ==========================================
    public static <T> List<T> getList(String endpoint, Class<T> itemType) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Authorization", "Bearer " + SessionManager.getToken())
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Erro na requisição: " + response.statusCode());
        }

        // TypeToken é necessário para converter arrays genéricos com Gson
        var listType = TypeToken.getParameterized(List.class, itemType).getType();
        return gson.fromJson(response.body(), listType);
    }

    // ==========================================
    // Método genérico para POST
    // ==========================================
    public static <T> T post(String endpoint, Object body, Class<T> responseType) throws Exception {
        String json = gson.toJson(body); // converte objeto para JSON

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json));

        // Adiciona token apenas se estiver logado
        if (SessionManager.isLoggedIn()) {
            builder.header("Authorization", "Bearer " + SessionManager.getToken());
        }

        HttpResponse<String> response = client.send(builder.build(),
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            // Tenta extrair a mensagem de erro do JSON retornado
            try {
                var error = gson.fromJson(response.body(),
                        java.util.Map.class);
                throw new RuntimeException(
                        error.getOrDefault("error", "Erro desconhecido").toString());
            } catch (Exception e) {
                throw new RuntimeException("Erro: " + response.statusCode());
            }
        }

        return gson.fromJson(response.body(), responseType);
    }

    // ==========================================
    // Método genérico para PUT
    // ==========================================
    public static <T> T put(String endpoint, Object body, Class<T> responseType) throws Exception {
        String json = gson.toJson(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Authorization", "Bearer " + SessionManager.getToken())
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Erro ao atualizar: " + response.statusCode());
        }

        return gson.fromJson(response.body(), responseType);
    }

    // ==========================================
    // Método genérico para DELETE
    // ==========================================
    public static void delete(String endpoint) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Authorization", "Bearer " + SessionManager.getToken())
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Erro ao deletar: " + response.statusCode());
        }
    }
}