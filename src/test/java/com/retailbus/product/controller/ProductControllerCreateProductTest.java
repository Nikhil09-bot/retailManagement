package com.retailbus.product.controller;

import com.retailbus.exception.GlobalExceptionHandler;
import com.retailbus.product.dto.response.ProductResponse;
import com.retailbus.product.enums.Category;
import com.retailbus.product.enums.Unit;
import com.retailbus.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class ProductControllerCreateProductTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    @DisplayName("Should create product when category and unit use display labels")
    void shouldCreateProductWhenUsingDisplayLabels() throws Exception {
        ProductResponse response = ProductResponse.builder()
                .id(1L)
                .productCode("COCA001")
                .name("Coca Cola")
                .category(Category.BEVERAGES)
                .unit(Unit.BOTTLE)
                .purchasePrice(new BigDecimal("1.50"))
                .sellingPrice(new BigDecimal("2.99"))
                .quantityInStock(50)
                .reorderLevel(10)
                .active(true)
                .build();

        when(productService.createProduct(any())).thenReturn(response);

        String payload = """
                {
                  "productCode": "COCA001",
                  "barcode": "1234567890",
                  "name": "Coca Cola",
                  "description": "Classic cola beverage",
                  "brand": "Coca Cola",
                  "category": "Beverages",
                  "purchasePrice": 1.50,
                  "sellingPrice": 2.99,
                  "quantityInStock": 50,
                  "reorderLevel": 10,
                  "unit": "Bottle"
                }
                """;

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Product created successfully"))
                .andExpect(jsonPath("$.data.category").value("BEVERAGES"))
                .andExpect(jsonPath("$.data.unit").value("BOTTLE"));

        ArgumentCaptor<com.retailbus.product.dto.request.ProductRequest> captor =
                ArgumentCaptor.forClass(com.retailbus.product.dto.request.ProductRequest.class);
        verify(productService).createProduct(captor.capture());
        assertEquals(Category.BEVERAGES, captor.getValue().getCategory());
        assertEquals(Unit.BOTTLE, captor.getValue().getUnit());
    }

    @Test
    @DisplayName("Should return bad request for unsupported enum values instead of generic server error")
    void shouldReturnBadRequestForUnsupportedEnumValues() throws Exception {
        String payload = """
                {
                  "productCode": "COCA001",
                  "barcode": "1234567890",
                  "name": "Coca Cola",
                  "description": "Classic cola beverage",
                  "brand": "Coca Cola",
                  "category": "DrinkType",
                  "purchasePrice": 1.50,
                  "sellingPrice": 2.99,
                  "quantityInStock": 50,
                  "reorderLevel": 10,
                  "unit": "Bottle"
                }
                """;

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message", containsString("Unsupported category: DrinkType")));
    }
}


