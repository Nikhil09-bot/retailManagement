package com.retailbus.product.service.impl;

import com.retailbus.product.dto.request.ProductRequest;
import com.retailbus.product.dto.response.ProductResponse;
import com.retailbus.product.entity.Product;
import com.retailbus.product.enums.Category;
import com.retailbus.product.enums.Unit;
import com.retailbus.product.exception.*;
import com.retailbus.product.mapper.ProductMapper;
import com.retailbus.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * ProductService Unit Tests.
 * Tests using JUnit 5 and Mockito for the ProductService implementation.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService Tests")
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductRequest productRequest;
    private Product product;
    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        // Setup test data
        productRequest = ProductRequest.builder()
                .productCode("COCA001")
                .barcode("1234567890")
                .name("Coca Cola")
                .description("Classic cola beverage")
                .brand("Coca Cola")
                .category(Category.BEVERAGES)
                .purchasePrice(new BigDecimal("1.50"))
                .sellingPrice(new BigDecimal("2.99"))
                .quantityInStock(100)
                .reorderLevel(20)
                .unit(Unit.BOTTLE)
                .build();

        product = Product.builder()
                .id(1L)
                .productCode("COCA001")
                .barcode("1234567890")
                .name("Coca Cola")
                .description("Classic cola beverage")
                .brand("Coca Cola")
                .category(Category.BEVERAGES)
                .purchasePrice(new BigDecimal("1.50"))
                .sellingPrice(new BigDecimal("2.99"))
                .quantityInStock(100)
                .reorderLevel(20)
                .unit(Unit.BOTTLE)
                .active(true)
                .build();

        productResponse = ProductResponse.builder()
                .id(1L)
                .productCode("COCA001")
                .barcode("1234567890")
                .name("Coca Cola")
                .description("Classic cola beverage")
                .brand("Coca Cola")
                .category(Category.BEVERAGES)
                .purchasePrice(new BigDecimal("1.50"))
                .sellingPrice(new BigDecimal("2.99"))
                .quantityInStock(100)
                .reorderLevel(20)
                .unit(Unit.BOTTLE)
                .active(true)
                .build();
    }

    @Test
    @DisplayName("Should create product successfully")
    void testCreateProduct_Success() {
        // Arrange
        when(productRepository.existsByProductCode("COCA001")).thenReturn(false);
        when(productMapper.toEntity(productRequest)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        // Act
        ProductResponse result = productService.createProduct(productRequest);

        // Assert
        assertNotNull(result);
        assertEquals("COCA001", result.getProductCode());
        assertEquals("Coca Cola", result.getName());
        verify(productRepository, times(1)).existsByProductCode("COCA001");
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw DuplicateProductException when product code exists")
    void testCreateProduct_DuplicateCode() {
        // Arrange
        when(productRepository.existsByProductCode("COCA001")).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateProductException.class, () -> productService.createProduct(productRequest));
        verify(productRepository, times(1)).existsByProductCode("COCA001");
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw BusinessValidationException when selling price <= purchase price")
    void testCreateProduct_InvalidPricing() {
        // Arrange
        productRequest.setSellingPrice(new BigDecimal("1.50"));
        when(productRepository.existsByProductCode("COCA001")).thenReturn(false);

        // Act & Assert
        assertThrows(BusinessValidationException.class, () -> productService.createProduct(productRequest));
    }

    @Test
    @DisplayName("Should update product successfully")
    void testUpdateProduct_Success() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.updateEntity(product, productRequest)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        // Act
        ProductResponse result = productService.updateProduct(1L, productRequest);

        // Assert
        assertNotNull(result);
        assertEquals("COCA001", result.getProductCode());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when product not found for update")
    void testUpdateProduct_NotFound() {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(999L, productRequest));
    }

    @Test
    @DisplayName("Should get product by ID successfully")
    void testGetProductById_Success() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productMapper.toResponse(product)).thenReturn(productResponse);

        // Act
        ProductResponse result = productService.getProductById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when product not found")
    void testGetProductById_NotFound() {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(999L));
    }

    @Test
    @DisplayName("Should disable product successfully")
    void testDisableProduct_Success() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        ProductResponse disabledResponse = new ProductResponse();
        disabledResponse.setActive(false);
        when(productMapper.toResponse(any(Product.class))).thenReturn(disabledResponse);

        // Act
        ProductResponse result = productService.disableProduct(1L);

        // Assert
        assertNotNull(result);
        assertFalse(result.getActive());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should increase stock successfully")
    void testIncreaseStock_Success() {
        // Arrange
        Product productToUpdate = new Product();
        productToUpdate.setId(1L);
        productToUpdate.setQuantityInStock(100);
        when(productRepository.findById(1L)).thenReturn(Optional.of(productToUpdate));
        when(productRepository.save(any(Product.class))).thenReturn(productToUpdate);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        // Act
        ProductResponse result = productService.increaseStock(1L, 50);

        // Assert
        assertNotNull(result);
        assertEquals(150, productToUpdate.getQuantityInStock());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw InvalidStockOperationException when quantity is negative")
    void testIncreaseStock_NegativeQuantity() {

        // Act & Assert
        assertThrows(InvalidStockOperationException.class, () -> productService.increaseStock(1L, -10));
    }

    @Test
    @DisplayName("Should decrease stock successfully")
    void testDecreaseStock_Success() {
        // Arrange
        Product productToUpdate = new Product();
        productToUpdate.setId(1L);
        productToUpdate.setQuantityInStock(100);
        when(productRepository.findById(1L)).thenReturn(Optional.of(productToUpdate));
        when(productRepository.save(any(Product.class))).thenReturn(productToUpdate);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        // Act
        ProductResponse result = productService.decreaseStock(1L, 30);

        // Assert
        assertNotNull(result);
        assertEquals(70, productToUpdate.getQuantityInStock());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("Should throw InvalidStockOperationException when decreasing below zero")
    void testDecreaseStock_BelowZero() {
        // Arrange
        Product productToUpdate = new Product();
        productToUpdate.setId(1L);
        productToUpdate.setQuantityInStock(20);
        when(productRepository.findById(1L)).thenReturn(Optional.of(productToUpdate));

        // Act & Assert
        assertThrows(InvalidStockOperationException.class, () -> productService.decreaseStock(1L, 50));
    }

    @Test
    @DisplayName("Should search products successfully")
    void testSearchProducts_Success() {
        // Arrange
        List<Product> products = Arrays.asList(product);
        when(productRepository.findByNameContainingIgnoreCase("Coca")).thenReturn(products);
        when(productRepository.findByBrandContainingIgnoreCase("Coca")).thenReturn(Arrays.asList());
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        // Act
        List<ProductResponse> result = productService.searchProducts("Coca");

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase("Coca");
    }

    @Test
    @DisplayName("Should get all active products")
    void testGetActiveProducts_Success() {
        // Arrange
        List<Product> activeProducts = Arrays.asList(product);
        when(productRepository.findByActiveTrue()).thenReturn(activeProducts);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        // Act
        List<ProductResponse> result = productService.getActiveProducts();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(productRepository, times(1)).findByActiveTrue();
    }

    @Test
    @DisplayName("Should get products by category")
    void testGetProductsByCategory_Success() {
        // Arrange
        List<Product> beverages = Arrays.asList(product);
        when(productRepository.findByCategoryAndActiveTrue(Category.BEVERAGES)).thenReturn(beverages);
        when(productMapper.toResponse(any(Product.class))).thenReturn(productResponse);

        // Act
        List<ProductResponse> result = productService.getProductsByCategory(Category.BEVERAGES);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(productRepository, times(1)).findByCategoryAndActiveTrue(Category.BEVERAGES);
    }

    @Test
    @DisplayName("Should delete product successfully")
    void testDeleteProduct_Success() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        productService.deleteProduct(1L);

        // Assert
        verify(productRepository, times(1)).delete(product);
    }

    @Test
    @DisplayName("Should throw ProductNotFoundException when deleting non-existent product")
    void testDeleteProduct_NotFound() {
        // Arrange
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(999L));
    }
}

