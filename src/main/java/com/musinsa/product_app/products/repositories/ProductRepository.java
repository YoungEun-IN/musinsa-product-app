package com.musinsa.product_app.products.repositories;

import com.musinsa.product_app.brands.entities.Brand;
import com.musinsa.product_app.products.entities.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByCategoryName(String name);
	List<Product> findByCategoryId(Long id);
	List<Product> findAllByBrand(Brand brand);

}
