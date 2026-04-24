package com.shoestock.desktop.model;

import lombok.Data;

// Representa a resposta do login da API
@Data
public class AuthResponse {
    private String token;
    private String name;
    private String email;
    private String role;
}