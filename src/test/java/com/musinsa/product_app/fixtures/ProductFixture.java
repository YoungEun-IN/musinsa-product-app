package com.musinsa.product_app.fixtures;

import com.musinsa.product_app.brands.entities.Brand;
import com.musinsa.product_app.products.dtos.ProductRequest;
import com.musinsa.product_app.products.entities.Product;

import java.util.Arrays;
import java.util.List;

public class ProductFixture {
    public static Brand createBrand() {
        return Brand.builder()
                .id(1L)
                .name("Brand1")
                .build();
    }

    public static Product createProduct() {
        return Product.builder()
                .id(1L)
                .brand(Brand.builder()
                        .id(1L)
                        .name("Brand1")
                        .build())
                .categoryName("상의")
                .price(100.0)
                .build();
    }

    public static ProductRequest createProductRequest() {
        return ProductRequest.builder()
                .brandId(1L)
                .categoryName("상의")
                .price(10000)
                .build();
    }

    public static List<Product> createProductList() {
        Brand brandA = new Brand(1L, "BrandA");
        Brand brandB = new Brand(2L, "BrandB");

        return Arrays.asList(
                new Product(1L, brandA, "Category1", 10.0),
                new Product(2L, brandA, "Category2", 20.0),
                new Product(3L, brandB, "Category1", 5.0),
                new Product(4L, brandB, "Category2", 15.0)
        );
    }

}
