package com.musinsa.product_app.products.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryPriceResponse {
    private String categoryName;
    private PriceDetail lowest;
    private PriceDetail highest;

    @Data
    @Builder
    public static class PriceDetail {
        private String brand;
        private double price;
    }
}
