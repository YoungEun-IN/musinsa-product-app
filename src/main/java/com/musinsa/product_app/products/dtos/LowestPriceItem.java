package com.musinsa.product_app.products.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LowestPriceItem {
    private String categoryName;
    private String brand;
    private double price;
}
