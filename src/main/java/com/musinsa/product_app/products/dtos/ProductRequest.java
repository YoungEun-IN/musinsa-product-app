package com.musinsa.product_app.products.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

	@NotNull(message = "category is mandatory")
	private String categoryName;

	@NotNull(message = "brandId is mandatory")
	private Long brandId;

	@Min(value = 0, message = "Price must be greater than or equal to 0")
	private double price;
}
