package com.musinsa.product_app.products.entities;

import com.musinsa.product_app.brands.entities.Brand;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "brand_id", nullable = false)
	private Brand brand;

	@Column(nullable = false)
	private String categoryName;

	@Column(nullable = false)
	private double price;

	public Product(double price, String categoryName, Brand brand) {
		this.price = price;
		this.categoryName = categoryName;
		this.brand = brand;
	}
}