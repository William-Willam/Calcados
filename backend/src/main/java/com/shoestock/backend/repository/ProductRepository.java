package com.shoestock.backend.repository;

import com.shoestock.backend.entity.Product;
import com.shoestock.backend.entity.ShoeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Lista produtos por tipo (FEMININO ou MASCULINO)
    // SELECT * FROM products WHERE type = ?
    List<Product> findByType(ShoeType type);

    // Busca produtos pelo nome (ignora maiúsculas/minúsculas)
    // SELECT * FROM products WHERE LOWER(name) LIKE LOWER(?)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Lista produtos com estoque maior que zero
    // SELECT * FROM products WHERE quantity > ?
    List<Product> findByQuantityGreaterThan(Integer quantity);
}
