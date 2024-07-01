package com.musinsa.product_app.products.dtos;

import com.musinsa.product_app.products.entities.Product;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProductResponse {
	private Long id;
	private String categoryName;
	private Long brandId;
	private double price;

	public static ProductResponse fromEntity(Product product) {
		return ProductResponse.builder()
				.id(product.getId())
				.categoryName(product.getCategoryName())
				.brandId(product.getBrand().getId())
				.price(product.getPrice())
				.build();
	}
}
