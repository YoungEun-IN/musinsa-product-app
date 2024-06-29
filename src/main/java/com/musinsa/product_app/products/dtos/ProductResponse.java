package com.musinsa.product_app.products.dtos;

import com.musinsa.product_app.products.entities.Product;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
	private Long id;
	private String category;
	private Long brandId;
	private double price;

	public static ProductResponse fromEntity(Product product) {
		return ProductResponse.builder()
				.id(product.getId())
				.category(product.getCategoryName())
				.brandId(product.getId())
				.price(product.getPrice())
				.build();
	}
}
