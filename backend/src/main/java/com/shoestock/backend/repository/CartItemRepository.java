package com.shoestock.backend.repository;

import com.shoestock.backend.entity.CarItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CarItem,Long> {

    // Busca todos os itens do carrinho de um usuário específico
    // SELECT * FROM cart_items WHERE user_id = ?
    List<CarItem> findByUserId(Long userId);

    // Verifica se um produto já está no carrinho de um usuário
    // Útil para somar quantidade em vez de duplicar o item
    Optional<CarItem> findByUserIdAndProductId(Long userId, Long productId);

    // Remove todos os itens do carrinho de um usuário
    // Útil para limpar o carrinho após uma compra
    void deleteByUserId(Long userId);
}
