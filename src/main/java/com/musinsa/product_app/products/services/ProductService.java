package com.musinsa.product_app.products.services;

import com.musinsa.product_app.brands.entities.Brand;
import com.musinsa.product_app.brands.repositories.BrandRepository;
import com.musinsa.product_app.exceptions.DataNotFoundException;
import com.musinsa.product_app.products.dtos.CategoryPriceResponse;
import com.musinsa.product_app.products.dtos.LowestBrandPriceItem;
import com.musinsa.product_app.products.dtos.LowestBrandPriceResponse;
import com.musinsa.product_app.products.dtos.LowestPriceItem;
import com.musinsa.product_app.products.dtos.LowestPriceResponse;
import com.musinsa.product_app.products.dtos.ProductRequest;
import com.musinsa.product_app.products.dtos.ProductResponse;
import com.musinsa.product_app.products.entities.Product;
import com.musinsa.product_app.products.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> findAllByBrand(Brand brand) {
        return productRepository.findAllByBrand(brand);
    }

    public void deleteAllInBatch(List<Product> products) {
        productRepository.deleteAllInBatch(products);
    }

    public List<ProductResponse> getProductList() {
        return this.findAllProducts().stream()
                .map(ProductResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public Product saveProduct(ProductRequest productRequest) {
        Brand brand = brandRepository.findById(productRequest.getBrandId())
                .orElseThrow(() -> new DataNotFoundException("Brand not found"));
        Product product = new Product(productRequest.getPrice(), productRequest.getCategoryName(), brand);
        return productRepository.save(product);
    }

    public void updateProduct(Long id, ProductRequest productRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));
        Brand brand = brandRepository.findById(productRequest.getBrandId())
                .orElseThrow(() -> new DataNotFoundException("Brand not found"));

        product.setCategoryName(productRequest.getCategoryName());
        product.setBrand(brand);
        product.setPrice(productRequest.getPrice());

        productRepository.save(product);
    }

    public void deleteProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new DataNotFoundException("Product not found");
        }
        productRepository.deleteById(productId);
    }

    public LowestPriceResponse getLowestPrices() {
        List<Product> products = findAllProducts();
        Map<String, Product> categoryNameProductMap = new HashMap<>();
        for (Product product : products) {
            categoryNameProductMap.merge(
                    product.getCategoryName(),
                    product,
                    (oldValue, newValue) -> oldValue.getPrice() > newValue.getPrice() ? newValue : oldValue);
        }

        List<LowestPriceItem> categories = getLowestPriceItems(categoryNameProductMap);

        return LowestPriceResponse.builder()
                .categories(categories)
                .totalPrice(categories.stream().mapToDouble(LowestPriceItem::getPrice).sum())
                .build();
    }

    private List<LowestPriceItem> getLowestPriceItems(Map<String, Product> categoryNameProductMap) {
        return categoryNameProductMap.values().stream()
                .map(product -> new LowestPriceItem(
                        product.getCategoryName(),
                        product.getBrand().getName(),
                        product.getPrice()))
                .toList();
    }

    public LowestBrandPriceResponse getLowestBrandPrice() {
        Map<Brand, Double> brandTotals = new HashMap<>();
        Map<Brand, List<Product>> brandProductListMap = new HashMap<>();

        for (Product product : findAllProducts()) {
            brandTotals.merge(product.getBrand(), product.getPrice(), Double::sum);
            brandProductListMap.computeIfAbsent(product.getBrand(), k -> new ArrayList<>()).add(product);
        }

        return new LowestBrandPriceResponse(getLowestBrandPriceItem(brandTotals, brandProductListMap));
    }

    private LowestBrandPriceItem getLowestBrandPriceItem(Map<Brand, Double> brandTotals, Map<Brand, List<Product>> brandProductListMap) {
        Map.Entry<Brand, Double> brandPricEntry = brandTotals.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .orElseThrow(IllegalStateException::new);

        return LowestBrandPriceItem.builder()
                .brand(brandPricEntry.getKey().getName())
                .categories(getCategoryPrices(brandProductListMap, brandPricEntry))
                .total(brandPricEntry.getValue())
                .build();
    }

    private List<LowestBrandPriceItem.CategoryPrice> getCategoryPrices(Map<Brand, List<Product>> brandProductListMap, Map.Entry<Brand, Double> brandPricEntry) {
        return brandProductListMap.get(brandPricEntry.getKey()).stream()
                .map(product -> new LowestBrandPriceItem.CategoryPrice(product.getCategoryName(), product.getPrice()))
                .collect(Collectors.toList());
    }

    public CategoryPriceResponse getCategoryPrices(String categoryName) {
        List<Product> products = productRepository.findByCategoryName(categoryName);

        if (products.isEmpty()) {
            throw new DataNotFoundException("해당 카테고리의 제품을 찾을 수 없습니다. : " + categoryName);
        }

        Product lowestPriceProduct = getLowestPriceProduct(products);
        Product highestPriceProduct = getHighestPriceProduct(products);

        return new CategoryPriceResponse(
                products.get(0).getCategoryName(),
                CategoryPriceResponse.PriceDetail.builder()
                        .brand(lowestPriceProduct.getBrand().getName())
                        .price(lowestPriceProduct.getPrice())
                        .build(),
                CategoryPriceResponse.PriceDetail.builder()
                        .brand(highestPriceProduct.getBrand().getName())
                        .price(highestPriceProduct.getPrice())
                        .build());
    }

    private Product getHighestPriceProduct(List<Product> products) {
        return products.stream()
                .max(Comparator.comparingDouble(Product::getPrice))
                .orElseThrow(IllegalStateException::new);
    }

    private Product getLowestPriceProduct(List<Product> products) {
        return products.stream()
                .min(Comparator.comparingDouble(Product::getPrice))
                .orElseThrow(IllegalStateException::new);
    }
}
