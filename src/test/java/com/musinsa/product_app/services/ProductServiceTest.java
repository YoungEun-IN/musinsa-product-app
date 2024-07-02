package com.musinsa.product_app.services;

import com.musinsa.product_app.brands.entities.Brand;
import com.musinsa.product_app.brands.repositories.BrandRepository;
import com.musinsa.product_app.exceptions.DataNotFoundException;
import com.musinsa.product_app.products.dtos.CategoryPriceResponse;
import com.musinsa.product_app.products.dtos.LowestBrandPriceResponse;
import com.musinsa.product_app.products.dtos.LowestPriceItem;
import com.musinsa.product_app.products.dtos.LowestPriceResponse;
import com.musinsa.product_app.products.dtos.ProductRequest;
import com.musinsa.product_app.products.dtos.ProductResponse;
import com.musinsa.product_app.products.entities.Product;
import com.musinsa.product_app.products.repositories.ProductRepository;
import com.musinsa.product_app.products.services.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.musinsa.product_app.fixtures.ProductFixture.createBrand;
import static com.musinsa.product_app.fixtures.ProductFixture.createProduct;
import static com.musinsa.product_app.fixtures.ProductFixture.createProductList;
import static com.musinsa.product_app.fixtures.ProductFixture.createProductRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
    @DisplayName("상품 저장 시 브랜드를 찾을 수 없는 경우 테스트")
    void testSaveProduct_BrandNotFound() {
        ProductRequest request = createProductRequest();

        when(brandRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> {
            productService.saveProduct(request);
        });
    }

    @Test
    @DisplayName("상품 저장 시 카테고리를 찾을 수 없는 경우 테스트")
    void testSaveProduct_CategoryNotFound() {
        ProductRequest request = createProductRequest();

        when(brandRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> {
            productService.saveProduct(request);
        });
    }

    @Test
    @DisplayName("모든 제품을 찾는 테스트")
    void testFindAllProducts() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(createProduct()));

        List<Product> products = productService.findAllProducts();

        assertNotNull(products);
        assertEquals(1, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("브랜드별로 모든 제품을 찾는 테스트")
    void testFindAllByBrand() {
        when(productRepository.findAllByBrand(any(Brand.class))).thenReturn(Collections.singletonList(createProduct()));

        List<Product> products = productService.findAllByBrand(createBrand());

        assertEquals(1, products.size());
        verify(productRepository, times(1)).findAllByBrand(any(Brand.class));
    }

    @Test
    @DisplayName("모든 제품을 일괄 삭제하는 테스트")
    void testDeleteAllInBatch() {
        List<Product> products = Collections.singletonList(createProduct());

        productService.deleteAllInBatch(products);

        verify(productRepository, times(1)).deleteAllInBatch(products);
    }

    @Test
    @DisplayName("제품 목록을 가져오는 테스트")
    void testGetProductList() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(createProduct()));

        List<ProductResponse> productResponses = productService.getProductList();

        assertNotNull(productResponses);
        assertEquals(1, productResponses.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("제품을 업데이트하는 테스트")
    void testUpdateProduct() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(createProduct()));
        when(brandRepository.findById(anyLong())).thenReturn(Optional.of(createBrand()));
        when(productRepository.save(any(Product.class))).thenReturn(createProduct());

        productService.updateProduct(1L, createProductRequest());

        verify(productRepository, times(1)).findById(anyLong());
        verify(brandRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("제품을 삭제하는 테스트")
    void testDeleteProduct() {
        when(productRepository.existsById(anyLong())).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).existsById(anyLong());
        verify(productRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("제품 업데이트 시 제품을 찾을 수 없는 경우 예외를 던지는 테스트")
    void testUpdateProduct_ProductNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> productService.updateProduct(1L, createProductRequest()));
    }

    @Test
    @DisplayName("제품 업데이트 시 브랜드를 찾을 수 없는 경우 예외를 던지는 테스트")
    void testUpdateProduct_BrandNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(createProduct()));
        when(brandRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> productService.updateProduct(1L, createProductRequest()));
    }

    @Test
    @DisplayName("제품 삭제 시 제품을 찾을 수 없는 경우 예외를 던지는 테스트")
    void testDeleteProduct_ProductNotFound() {
        when(productRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(DataNotFoundException.class, () -> productService.deleteProduct(1L));
    }

    @Test
    @DisplayName("브랜드별 최저가를 가져오는 테스트")
    void testGetLowestBrandPrice() {
        List<Product> productList = createProductList();

        when(productRepository.findAll()).thenReturn(productList);

        LowestBrandPriceResponse response = productService.getLowestBrandPrice();

        assertEquals("BrandB", response.lowestPrice().getBrand());
        assertEquals(20.0, response.lowestPrice().getTotal());
    }

    @Test
    @DisplayName("카테고리별 가격 정보를 가져오는 테스트")
    void testGetCategoryPrices() {
        String categoryName = "Category1";
        when(productRepository.findByCategoryName(categoryName)).thenReturn(createProductList());

        CategoryPriceResponse response = productService.getCategoryPrices(categoryName);

        assertEquals(categoryName, response.getCategoryName());
        assertEquals("BrandB", response.getLowest().getBrand());
        assertEquals(5.0, response.getLowest().getPrice());
        assertEquals("BrandA", response.getHighest().getBrand());
        assertEquals(20.0, response.getHighest().getPrice());
    }

    @Test
    @DisplayName("카테고리에 해당하는 제품이 없는 경우 예외 처리 테스트")
    void testGetCategoryPrices_NoProductsFound() {
        String categoryName = "Category2";
        when(productRepository.findByCategoryName(categoryName)).thenReturn(List.of());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
            productService.getCategoryPrices(categoryName);
        });

        assertEquals("해당 카테고리의 제품을 찾을 수 없습니다. : " + categoryName, exception.getMessage());
    }

    @Test
    @DisplayName("카테고리별 최저가 정보를 가져오는 테스트")
    void testGetLowestPrices() {
        when(productRepository.findAll()).thenReturn(createProductList());

        LowestPriceResponse response = productService.getLowestPrices();

        assertEquals(2, response.getCategories().size());
        assertEquals(20.0, response.getTotalPrice());

        LowestPriceItem category1Item = response.getCategories().stream()
                .filter(item -> "Category1".equals(item.getCategoryName()))
                .findFirst()
                .orElseThrow();
        assertEquals("BrandB", category1Item.getBrand());
        assertEquals(5.0, category1Item.getPrice());

        LowestPriceItem category2Item = response.getCategories().stream()
                .filter(item -> "Category2".equals(item.getCategoryName()))
                .findFirst()
                .orElseThrow();
        assertEquals("BrandB", category2Item.getBrand());
        assertEquals(15.0, category2Item.getPrice());
    }
}
