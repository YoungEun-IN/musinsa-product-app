package com.musinsa.product_app.brands.services;

import com.musinsa.product_app.brands.dtos.BrandRequest;
import com.musinsa.product_app.brands.dtos.BrandResponse;
import com.musinsa.product_app.brands.repositories.BrandRepository;
import com.musinsa.product_app.brands.entities.Brand;
import com.musinsa.product_app.exceptions.DataNotFoundException;
import com.musinsa.product_app.products.entities.Product;
import com.musinsa.product_app.products.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandService {

	private final BrandRepository brandRepository;
	private final ProductService productService;

	public Brand addBrand(BrandRequest brandRequest) {
		Brand brand = new Brand();
		brand.setName(brandRequest.getName());
		return brandRepository.save(brand);
	}

	public void updateBrand(Long id, BrandRequest brandRequest) {
		Brand brand = brandRepository.findById(id)
			.orElseThrow(() -> new DataNotFoundException("Brand not found"));

		brand.setName(brandRequest.getName());
		brandRepository.save(brand);
	}

	public void deleteBrand(Long id) {

		Brand brand = brandRepository.findById(id)
			.orElseThrow(() -> new DataNotFoundException("Brand not found"));

		List<Product> products = productService.findAllByBrand(brand);
		productService.deleteAllInBatch(products);
		brandRepository.delete(brand);
	}

	public List<BrandResponse> getAllBrands() {
		return brandRepository.findAll().stream()
			.map(brand -> new BrandResponse(brand.getId(), brand.getName()))
			.collect(Collectors.toList());
	}

	public BrandResponse getBrandById(Long id) {
		Brand brand = brandRepository.findById(id)
			.orElseThrow(() -> new DataNotFoundException("Brand not found"));

		return new BrandResponse(brand.getId(), brand.getName());
	}
}
