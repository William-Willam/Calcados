package com.shoestock.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="cart_items")
public class CarItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento: vários itens do carrinho pertencem a um único usuário (cliente)
    // @ManyToOne = muitos CartItems → um User
    // FetchType.LAZY = só carrega o User do banco quando for necessário (mais performático)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // cria uma chave estrangeira
    private User user;

    // Relacionamento: vários itens do carrinho podem referenciar o mesmo produto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity; // Quantidade do produto no carrinho
}
