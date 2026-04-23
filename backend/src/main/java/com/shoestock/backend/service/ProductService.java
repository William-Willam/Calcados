package com.shoestock.backend.service;

import com.shoestock.backend.dto.ProductDTO;
import com.shoestock.backend.entity.Product;
import com.shoestock.backend.entity.ShoeType;
import com.shoestock.backend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private ProductDTO.Response toResponse(Product product) {
        return ProductDTO.Response.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .quantity(product.getQuantity())
                .type(product.getType())
                .build();
    }

    public ProductDTO.Response create(ProductDTO.Request dto) {
        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .imageUrl(dto.getImageUrl())
                .quantity(dto.getQuantity())
                .type(dto.getType())
                .build();

        return toResponse(productRepository.save(product));
    }

    public List<ProductDTO.Response> listAll() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ProductDTO.Response> listByType(ShoeType type) {
        return productRepository.findByType(type)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ProductDTO.Response findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + id));
        return toResponse(product);
    }

    public ProductDTO.Response update(Long id, ProductDTO.Request dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + id));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImageUrl(dto.getImageUrl());
        product.setQuantity(dto.getQuantity());
        product.setType(dto.getType());

        return toResponse(productRepository.save(product));
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Produto não encontrado: " + id);
        }
        productRepository.deleteById(id);
    }
}