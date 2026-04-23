package com.shoestock.backend.service;

import com.shoestock.backend.dto.CartItemDTO;
import com.shoestock.backend.entity.CartItem;
import com.shoestock.backend.entity.Product;
import com.shoestock.backend.entity.User;
import com.shoestock.backend.repository.CartItemRepository;
import com.shoestock.backend.repository.ProductRepository;
import com.shoestock.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private CartItemDTO.Response toResponse(CartItem item) {
        BigDecimal subtotal = item.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity())); // preço × quantidade

        return CartItemDTO.Response.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .productPrice(item.getProduct().getPrice())
                .productImageUrl(item.getProduct().getImageUrl())
                .quantity(item.getQuantity())
                .subtotal(subtotal)
                .build();
    }

    // Adiciona produto ao carrinho
    // Se o produto já estiver no carrinho, soma a quantidade
    public CartItemDTO.Response addToCart(Long userId, CartItemDTO.Request dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // Verifica se o produto já está no carrinho
        Optional<CartItem> existing = cartItemRepository
                .findByUserIdAndProductId(userId, dto.getProductId());

        CartItem item;
        if (existing.isPresent()) {
            // Produto já no carrinho: soma a quantidade
            item = existing.get();
            item.setQuantity(item.getQuantity() + dto.getQuantity());
        } else {
            // Produto novo no carrinho: cria o item
            item = CartItem.builder()
                    .user(user)
                    .product(product)
                    .quantity(dto.getQuantity())
                    .build();
        }

        return toResponse(cartItemRepository.save(item));
    }

    // Lista todos os itens do carrinho de um usuário
    public List<CartItemDTO.Response> getCart(Long userId) {
        return cartItemRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Remove um item específico do carrinho
    public void removeItem(Long itemId) {
        cartItemRepository.deleteById(itemId);
    }

    // Limpa o carrinho inteiro do usuário
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}