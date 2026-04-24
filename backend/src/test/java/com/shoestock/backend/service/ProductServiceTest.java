package com.shoestock.backend.service;

import com.shoestock.backend.dto.ProductDTO;
import com.shoestock.backend.entity.Product;
import com.shoestock.backend.entity.ShoeType;
import com.shoestock.backend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductDTO.Request requestDTO;
    private Product savedProduct;

    @BeforeEach
    void setUp() {
        requestDTO = ProductDTO.Request.builder()
                .name("Scarpin Elegance")
                .description("Scarpin de couro")
                .price(new BigDecimal("189.90"))
                .imageUrl("https://exemplo.com/scarpin.jpg")
                .quantity(15)
                .type(ShoeType.FEMININO)
                .build();

        savedProduct = Product.builder()
                .id(1L)
                .name("Scarpin Elegance")
                .description("Scarpin de couro")
                .price(new BigDecimal("189.90"))
                .imageUrl("https://exemplo.com/scarpin.jpg")
                .quantity(15)
                .type(ShoeType.FEMININO)
                .build();
    }

    @Test
    @DisplayName("Deve cadastrar produto com sucesso")
    void deveCadastrarProdutoComSucesso() {
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductDTO.Response response = productService.create(requestDTO);

        assertNotNull(response);
        assertEquals("Scarpin Elegance", response.getName());
        assertEquals(new BigDecimal("189.90"), response.getPrice());
        assertEquals(ShoeType.FEMININO, response.getType());
        assertEquals(15, response.getQuantity());

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Deve listar todos os produtos")
    void deveListarTodosOsProdutos() {
        when(productRepository.findAll()).thenReturn(List.of(savedProduct));

        List<ProductDTO.Response> result = productService.listAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Scarpin Elegance", result.get(0).getName());
    }

    @Test
    @DisplayName("Deve filtrar produtos por tipo FEMININO")
    void deveFiltrarProdutosPorTipo() {
        when(productRepository.findByType(ShoeType.FEMININO)).thenReturn(List.of(savedProduct));

        List<ProductDTO.Response> result = productService.listByType(ShoeType.FEMININO);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ShoeType.FEMININO, result.get(0).getType());
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não encontrado")
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productService.findById(99L)
        );

        assertEquals("Produto não encontrado: 99", exception.getMessage());
    }

    @Test
    @DisplayName("Deve deletar produto com sucesso")
    void deveDeletarProdutoComSucesso() {
        when(productRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> productService.delete(1L));

        verify(productRepository, times(1)).deleteById(1L);
    }
}