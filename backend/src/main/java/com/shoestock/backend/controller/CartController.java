package com.shoestock.backend.controller;

import com.shoestock.backend.dto.CartItemDTO;
import com.shoestock.backend.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // POST /api/cart/{userId}
    // Adiciona produto ao carrinho do usuário
    @PostMapping("/{userId}")
    public ResponseEntity<CartItemDTO.Response> addToCart(
            @PathVariable Long userId,
            @Valid @RequestBody CartItemDTO.Request dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cartService.addToCart(userId, dto));
    }

    // GET /api/cart/{userId}
    // Lista todos os itens do carrinho do usuário
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemDTO.Response>> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    // DELETE /api/cart/item/{itemId}
    // Remove um item específico do carrinho
    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId) {
        cartService.removeItem(itemId);
        return ResponseEntity.noContent().build();
    }

    // DELETE /api/cart/{userId}
    // Limpa todo o carrinho do usuário
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}