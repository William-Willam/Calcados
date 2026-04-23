package com.shoestock.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT") // TEXT permite textos longos no banco
    private String description;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    @Column(nullable = false, precision = 10, scale = 2) // precision = total de dígitos, scale = casas decimais
    private BigDecimal price; // BigDecimal é o tipo correto para dinheiro (evita erros de arredondamento)

    @Column(name = "image_url", length = 1000)
    private String imageUrl; // Caminho ou URL da imagem do produto

    @Min(value = 0, message = "Quantidade não pode ser negativa")
    @Column(nullable = false)
    private Integer quantity; // Quantidade em estoque

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShoeType type; // FEMININO ou MASCULINO

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate // Executa automaticamente antes de atualizar um registro existente
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}