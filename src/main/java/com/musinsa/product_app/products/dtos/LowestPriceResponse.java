package com.musinsa.product_app.products.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LowestPriceResponse {
    private List<LowestPriceItem> categories;
    private double totalPrice;
}
