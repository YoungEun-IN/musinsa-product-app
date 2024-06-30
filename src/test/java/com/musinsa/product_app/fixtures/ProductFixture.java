package com.musinsa.product_app.fixtures;

import com.musinsa.product_app.products.dtos.ProductRequest;

public class ProductFixture {
    public static ProductRequest createProductRequest() {
        return ProductRequest.builder()
                .brandId(1L)
                .categoryName("상의")
                .price(10000)
                .build();
    }

}
