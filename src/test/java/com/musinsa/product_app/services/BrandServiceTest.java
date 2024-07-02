package com.musinsa.product_app.services;

import com.musinsa.product_app.brands.dtos.BrandRequest;
import com.musinsa.product_app.brands.dtos.BrandResponse;
import com.musinsa.product_app.brands.entities.Brand;
import com.musinsa.product_app.brands.repositories.BrandRepository;
import com.musinsa.product_app.brands.services.BrandService;
import com.musinsa.product_app.exceptions.DataNotFoundException;
import com.musinsa.product_app.products.repositories.ProductRepository;
import com.musinsa.product_app.products.services.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.musinsa.product_app.fixtures.ProductFixture.createBrand;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BrandServiceTest {

    @InjectMocks
    private BrandService brandService;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    @DisplayName("모든 브랜드를 가져오는 테스트")
    void testGetAllBrands() {
        when(brandRepository.findAll()).thenReturn(Collections.singletonList(createBrand()));

        List<BrandResponse> brands = brandService.getAllBrands();

        assertNotNull(brands);
        assertEquals(1, brands.size());
        assertEquals("Brand1", brands.get(0).getName());
        verify(brandRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("ID로 브랜드를 가져오는 테스트")
    void testGetBrandById() {
        when(brandRepository.findById(anyLong())).thenReturn(Optional.of(createBrand()));

        BrandResponse brandResponse = brandService.getBrandById(1L);

        assertNotNull(brandResponse);
        assertEquals("Brand1", brandResponse.getName());
        verify(brandRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("ID로 브랜드를 가져올 때 브랜드를 찾지 못하는 경우 예외를 던지는 테스트")
    void testGetBrandById_BrandNotFound() {
        when(brandRepository.findById(anyLong())).thenReturn(Optional.empty());

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> brandService.getBrandById(1L));
        assertEquals("Brand not found", exception.getMessage());
    }

    @Test
    @DisplayName("브랜드 추가 테스트")
    public void testAddBrand() {
        BrandRequest request = new BrandRequest();
        request.setName("Test Brand");

        Brand expectedBrand = new Brand();
        expectedBrand.setId(1L);
        expectedBrand.setName("Test Brand");

        when(brandRepository.save(any(Brand.class))).thenReturn(expectedBrand);

        Brand resultBrand = brandService.addBrand(request);

        // 결과가 예상한 값과 일치하는지 확인합니다.
        assertEquals(expectedBrand.getName(), resultBrand.getName());
        assertEquals(expectedBrand.getId(), resultBrand.getId());
    }

    @Test
    @DisplayName("브랜드 업데이트 테스트")
    public void testUpdateBrand() {
        BrandRequest request = new BrandRequest();
        request.setName("Updated Brand");

        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("Old Brand");

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));

        brandService.updateBrand(1L, request);

        verify(brandRepository, times(1)).save(brand);
        assertEquals("Updated Brand", brand.getName());
    }

    @Test
    @DisplayName("브랜드 업데이트 - 브랜드를 찾을 수 없는 경우")
    public void testUpdateBrand_NotFound() {
        BrandRequest request = new BrandRequest();
        request.setName("Updated Brand");

        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> brandService.updateBrand(1L, request));
    }

    @Test
    @DisplayName("브랜드 삭제 테스트")
    public void testDeleteBrand() {
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("Test Brand");

        when(brandRepository.findById(1L)).thenReturn(Optional.of(brand));

        brandService.deleteBrand(1L);

        verify(brandRepository, times(1)).delete(brand);
    }

    @Test
    @DisplayName("브랜드 삭제 - 브랜드를 찾을 수 없는 경우")
    public void testDeleteBrand_NotFound() {
        when(brandRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> brandService.deleteBrand(1L));
    }
}
