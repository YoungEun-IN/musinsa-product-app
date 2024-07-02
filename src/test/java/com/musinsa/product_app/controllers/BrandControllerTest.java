package com.musinsa.product_app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.product_app.brands.controllers.BrandApiController;
import com.musinsa.product_app.brands.dtos.BrandRequest;
import com.musinsa.product_app.brands.dtos.BrandResponse;
import com.musinsa.product_app.brands.services.BrandService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BrandApiController.class)
public class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BrandService brandService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("브랜드 추가 테스트")
    public void testAddBrand() throws Exception {
        BrandRequest request = new BrandRequest();
        request.setName("Test Brand");

        mockMvc.perform(post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Brand added successfully"));

        verify(brandService, times(1)).addBrand(any(BrandRequest.class));
    }

    @Test
    @DisplayName("브랜드 수정 테스트")
    public void testUpdateBrand() throws Exception {
        BrandRequest request = new BrandRequest();
        request.setName("Updated Brand");

        mockMvc.perform(put("/api/brands/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Brand updated successfully"));

        verify(brandService, times(1)).updateBrand(eq(1L), any(BrandRequest.class));
    }

    @Test
    @DisplayName("브랜드 삭제 테스트")
    public void testDeleteBrand() throws Exception {
        mockMvc.perform(delete("/api/brands/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Brand deleted successfully"));

        verify(brandService, times(1)).deleteBrand(1L);
    }

    @Test
    @DisplayName("전체 브랜드 조회 테스트")
    public void testGetAllBrands() throws Exception {
        List<BrandResponse> brands = Arrays.asList(
                new BrandResponse(1L, "Brand 1"),
                new BrandResponse(2L, "Brand 2")
        );

        when(brandService.getAllBrands()).thenReturn(brands);

        mockMvc.perform(get("/api/brands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Brand 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Brand 2"));

        verify(brandService, times(1)).getAllBrands();
    }

    @Test
    @DisplayName("브랜드 ID로 조회 테스트")
    public void testGetBrandById() throws Exception {
        BrandResponse brand = new BrandResponse(1L, "Brand 1");

        when(brandService.getBrandById(1L)).thenReturn(brand);

        mockMvc.perform(get("/api/brands/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Brand 1"));

        verify(brandService, times(1)).getBrandById(1L);
    }
}