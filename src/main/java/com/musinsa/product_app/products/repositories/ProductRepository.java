package com.musinsa.product_app.products.repositories;

import com.musinsa.product_app.brands.entities.Brand;
import com.musinsa.product_app.products.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	@Query("SELECT distinct p FROM Product p join fetch p.brand  WHERE p.categoryName = :categoryName")
	List<Product> findByCategoryName(String categoryName);
	List<Product> findAllByBrand(Brand brand);

}
