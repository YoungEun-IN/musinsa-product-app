package com.musinsa.product_app.brands.controllers;

import com.musinsa.product_app.brands.dtos.BrandRequest;
import com.musinsa.product_app.brands.dtos.BrandResponse;
import com.musinsa.product_app.brands.services.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
@Tag(name = "Brands API", description = "브랜드 관리 api")
public class BrandApiController {

	private final BrandService brandService;

	@Operation(summary = "브랜드 추가")
	@PostMapping
	public ResponseEntity<String> addBrand(@RequestBody BrandRequest brandRequest) {
		brandService.addBrand(brandRequest);
		return ResponseEntity.ok("Brand added successfully");
	}

	@Operation(summary = "브랜드 수정")
	@PutMapping("/{id}")
	public ResponseEntity<String> updateBrand(@PathVariable Long id, @RequestBody BrandRequest brandRequest) {
		brandService.updateBrand(id, brandRequest);
		return ResponseEntity.ok("Brand updated successfully");
	}

	@Operation(summary = "브랜드 삭제")
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteBrand(@PathVariable Long id) {
		brandService.deleteBrand(id);
		return ResponseEntity.ok("Brand deleted successfully");
	}

	@Operation(summary = "브랜드 리스트 조회")
	@GetMapping
	public ResponseEntity<List<BrandResponse>> getAllBrands() {
		List<BrandResponse> brands = brandService.getAllBrands();
		return ResponseEntity.ok(brands);
	}

	@Operation(summary = "브랜드 조회")
	@GetMapping("/{id}")
	public ResponseEntity<BrandResponse> getBrandById(@PathVariable Long id) {
		BrandResponse brand = brandService.getBrandById(id);
		return ResponseEntity.ok(brand);
	}
}
