package com.shoestock.backend.controller;

import com.shoestock.backend.dto.ProductDTO;
import com.shoestock.backend.entity.ShoeType;
import com.shoestock.backend.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest extends BaseIntegrationTest {

    @Autowired
    private ProductService productService;

    // Método auxiliar para criar produto nos testes
    private ProductDTO.Request buildProductRequest(String name, BigDecimal price,
                                                   Integer quantity, ShoeType type) {
        return ProductDTO.Request.builder()
                .name(name)
                .description("Descrição de teste")
                .price(price)
                .imageUrl("https://exemplo.com/img.jpg")
                .quantity(quantity)
                .type(type)
                .build();
    }

    @Test
    @DisplayName("Estoquista deve cadastrar produto com sucesso")
    void estoquistaDeveCadastrarProduto() throws Exception {
        mockMvc.perform(post("/api/products")
                        .header("Authorization", estoquistaToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(buildProductRequest(
                                "Scarpin Test",
                                new BigDecimal("189.90"),
                                10,
                                ShoeType.FEMININO
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Scarpin Test"))
                .andExpect(jsonPath("$.type").value("FEMININO"))
                .andExpect(jsonPath("$.price").value(189.90));
    }

    @Test
    @DisplayName("Cliente não deve cadastrar produto")
    void clienteNaoDeveCadastrarProduto() throws Exception {
        mockMvc.perform(post("/api/products")
                        .header("Authorization", clienteToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(buildProductRequest(
                                "Produto Indevido",
                                new BigDecimal("99.90"),
                                5,
                                ShoeType.MASCULINO
                        ))))
                .andExpect(status().isForbidden()); // 403
    }

    @Test
    @DisplayName("Qualquer um deve listar produtos sem token")
    void qualquerUmDeveListarProdutosSemToken() throws Exception {
        // Cria um produto primeiro
        productService.create(buildProductRequest(
                "Oxford Test",
                new BigDecimal("249.90"),
                8,
                ShoeType.MASCULINO
        ));

        // Em vez de verificar o primeiro item (que pode variar),
        // verifica apenas que a lista não está vazia
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    @DisplayName("Deve filtrar produtos por tipo")
    void deveFiltrarProdutosPorTipo() throws Exception {
        productService.create(buildProductRequest(
                "Sandália Test",
                new BigDecimal("129.90"),
                20,
                ShoeType.FEMININO
        ));

        mockMvc.perform(get("/api/products?type=FEMININO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("FEMININO"));
    }

    @Test
    @DisplayName("Somente gestor deve deletar produto")
    void somenteGestorDeveDeletarProduto() throws Exception {
        var created = productService.create(buildProductRequest(
                "Para Deletar",
                new BigDecimal("99.90"),
                5,
                ShoeType.MASCULINO
        ));

        // Estoquista tenta deletar — deve ser negado
        mockMvc.perform(delete("/api/products/" + created.getId())
                        .header("Authorization", estoquistaToken))
                .andExpect(status().isForbidden());

        // Gestor deleta — deve funcionar
        mockMvc.perform(delete("/api/products/" + created.getId())
                        .header("Authorization", gestorToken))
                .andExpect(status().isNoContent());
    }
}