package com.musinsa.product_app.services;

import com.musinsa.product_app.brands.entities.Brand;
import com.musinsa.product_app.brands.repositories.BrandRepository;
import com.musinsa.product_app.exceptions.DataNotFoundException;
import com.musinsa.product_app.products.dtos.ProductRequest;
import com.musinsa.product_app.products.entities.Product;
import com.musinsa.product_app.products.repositories.ProductRepository;
import com.musinsa.product_app.products.services.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.musinsa.product_app.fixtures.ProductFixture.createProductRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BrandRepository brandRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    void testSaveProduct() {
        ProductRequest request = createProductRequest();

        Brand brand = new Brand(1L, "Test Brand");

        when(brandRepository.findById(anyLong())).thenReturn(Optional.of(brand));

        Product savedProduct = Product.builder()
                .brand(brand)
                .categoryName(request.getCategoryName())
                .price(request.getPrice())
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Product resultProduct = productService.saveProduct(request);

        assertEquals(savedProduct.getBrand(), resultProduct.getBrand());
        assertEquals(savedProduct.getCategoryName(), resultProduct.getCategoryName());
        assertEquals(savedProduct.getPrice(), resultProduct.getPrice());
    }

    @Test
    @DisplayName("상품 저장 시 브랜드를 찾을 수 없는 경우")
    void testSaveProduct_BrandNotFound() {
        ProductRequest request = createProductRequest();

        when(brandRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> {
            productService.saveProduct(request);
        });
    }

    @Test
    @DisplayName("상품 저장 시 카테고리를 찾을 수 없는 경우")
    void testSaveProduct_CategoryNotFound() {
        ProductRequest request = createProductRequest();

        when(brandRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> {
            productService.saveProduct(request);
        });
    }
}
