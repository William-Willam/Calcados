package com.shoestock.backend.dto;

import com.shoestock.backend.entity.ShoeType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

public class ProductDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "Nome é obrigatório")
        private String name;

        private String description;

        @NotNull(message = "Preço é obrigatório")
        @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
        private BigDecimal price;

        private String imageUrl;

        @Min(value = 0, message = "Quantidade não pode ser negativa")
        private Integer quantity;

        @NotNull(message = "Tipo é obrigatório")
        private ShoeType type;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private String imageUrl;
        private Integer quantity;
        private ShoeType type;
    }
}