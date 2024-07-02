package com.musinsa.product_app.products.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LowestBrandPriceItem {

    private String brand;
    private double total;
    private List<CategoryPrice> categories;

    public record CategoryPrice(String category, double price) {
    }
}
