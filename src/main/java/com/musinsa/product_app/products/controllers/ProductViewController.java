package com.musinsa.product_app.products.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view")
@RequiredArgsConstructor
public class ProductViewController {

    @GetMapping("/brands")
    public String getBrands() {
        return "brands";
    }

    @GetMapping("/products")
    public String getProducts() {
        return "products";
    }

    @GetMapping("/category/prices")
    public String getCategoryPrices() {
        return "category-prices";
    }

    @GetMapping("/lowest-brand-price")
    public String getLowestBrandPrice() {
        return "lowest-brand-price";
    }

    @GetMapping("/lowest-prices")
    public String getLowestPrices() {
        return "lowest-prices";
    }
}
