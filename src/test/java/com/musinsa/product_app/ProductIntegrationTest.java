package com.musinsa.product_app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.product_app.products.dtos.ProductRequest;
import com.musinsa.product_app.products.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.musinsa.product_app.fixtures.ProductFixture.createProductRequest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        productRequest = createProductRequest();
    }

    @Test
    @DisplayName("최저가 리스트 조회 테스트")
    public void testGetLowestPrices() throws Exception {
        mockMvc.perform(get("/api/products/lowest-prices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("브랜드별 최저가 조회 테스트")
    public void testGetLowestTotal() throws Exception {
        mockMvc.perform(get("/api/products/lowest-brand-price"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("카테고리별 가격 조회 테스트")
    public void testGetCategoryPrices() throws Exception {
        String categoryName = "상의";
        mockMvc.perform(get("/api/products/category/prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("categoryName", categoryName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("상품 추가 테스트")
    public void testAddProduct() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Product added successfully"));
    }

    @Test
    @DisplayName("상품 수정 테스트")
    public void testUpdateProduct() throws Exception {
        Long productId = productService.saveProduct(productRequest).getId();

        mockMvc.perform(put("/api/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Product updated successfully"));
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    public void testDeleteProduct() throws Exception {
        Long productId = productService.saveProduct(productRequest).getId();

        mockMvc.perform(delete("/api/products/" + productId))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted successfully"));
    }
}