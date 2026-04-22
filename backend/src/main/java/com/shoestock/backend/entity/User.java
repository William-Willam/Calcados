package com.shoestock.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data               // Lombok: gera getters, setters, toString e equals automaticamente
@Builder            // Lombok: permite criar objetos com padrão builder (User.builder().name("x").build())
@NoArgsConstructor  // Lombok: gera construtor vazio (obrigatório para o JPA)
@AllArgsConstructor // Lombok: gera construtor com todos os campos
@Entity             // JPA: diz que essa classe vira uma tabela no banco
@Table(name = "users") // JPA: define o nome da tabela (evita conflito com palavra reservada "user")
public class User {

    @Id // Define que esse campo é a chave primária
    @GeneratedValue(strategy = GenerationType.IDENTITY) // O banco gera o ID automaticamente (auto increment)
    private Long id;

    @NotBlank(message = "Nome é obrigatório") // Validação: não pode ser vazio
    @Column(nullable = false) // No banco: coluna não pode ser nula
    private String name;

    @Email(message = "Email inválido") // Validação: deve ser um email válido
    @NotBlank(message = "Email é obrigatório")
    @Column(nullable = false, unique = true) // unique = true: não pode ter dois usuários com mesmo email
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Column(nullable = false)
    private String password; // Vai ser salvo criptografado com BCrypt

    @Enumerated(EnumType.STRING) // Salva o enum como texto no banco ("GESTOR", "ESTOQUISTA", "CLIENTE")
    @Column(nullable = false)
    private Role role;

    @Column(name = "created_at", updatable = false) // updatable = false: nunca atualiza esse campo
    private LocalDateTime createdAt;

    @PrePersist // Executa esse método automaticamente antes de salvar no banco pela primeira vez
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}