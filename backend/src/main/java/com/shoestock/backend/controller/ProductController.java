package com.shoestock.backend.controller;

import com.shoestock.backend.dto.ProductDTO;
import com.shoestock.backend.entity.ShoeType;
import com.shoestock.backend.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // POST /api/products
    // Cadastra um novo produto (estoquista)
    @PostMapping
    public ResponseEntity<ProductDTO.Response> create(@Valid @RequestBody ProductDTO.Request dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(dto));
    }

    // GET /api/products
    // Lista todos os produtos
    // Aceita filtro opcional por tipo: GET /api/products?type=FEMININO
    // @RequestParam(required = false) = parâmetro opcional na URL
    @GetMapping
    public ResponseEntity<List<ProductDTO.Response>> listAll(
            @RequestParam(required = false) ShoeType type) {
        if (type != null) {
            return ResponseEntity.ok(productService.listByType(type));
        }
        return ResponseEntity.ok(productService.listAll());
    }

    // GET /api/products/{id}
    // Busca produto pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO.Response> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    // PUT /api/products/{id}
    // Atualiza produto (estoquista)
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO.Response> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO.Request dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    // DELETE /api/products/{id}
    // Remove produto (somente gestor)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}