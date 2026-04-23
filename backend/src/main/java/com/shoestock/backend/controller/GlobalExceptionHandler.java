package com.shoestock.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

// @RestControllerAdvice = intercepta exceções de todos os controllers
// Centraliza o tratamento de erros em um único lugar
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura erros de validação (@Valid)
    // Ex: campo obrigatório vazio, email inválido
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        // Para cada campo com erro, pega o nome do campo e a mensagem
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors); // 400
    }

    // Captura RuntimeException lançadas nos Services
    // Ex: "Usuário não encontrado", "Email já cadastrado"
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error); // 400
    }
}