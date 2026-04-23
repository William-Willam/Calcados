package com.shoestock.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

public class CartItemDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull(message = "Produto é obrigatório")
        private Long productId;

        @Min(value = 1, message = "Quantidade mínima é 1")
        private Integer quantity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Long productId;
        private String productName;
        private BigDecimal productPrice;
        private String productImageUrl;
        private Integer quantity;
        private BigDecimal subtotal; // quantity × price
    }
}