package com.example.practicaltest.spring.controller.product;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.example.practicaltest.spring.api.controller.product.ProductController;
import com.example.practicaltest.spring.api.controller.product.request.ProductCreateRequest;
import com.example.practicaltest.spring.api.service.product.ProductService;
import com.example.practicaltest.spring.domain.product.ProductSellingStatus;
import com.example.practicaltest.spring.domain.product.ProductType;
import com.example.practicaltest.spring.domain.response.ProductResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;

    @DisplayName("신규 상품을 생성한다.")
    @Test
    void createProduct() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
            .type(ProductType.HANDMADE)
            .sellingStatus(ProductSellingStatus.SELLING)
            .name("아메리카노")
            .price(4000)
            .build();

        //when //then
        mockMvc.perform(post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk());

    }

    @DisplayName("신규 상품을 등록할 때, 상품 타입은 필수값이다.")
    @Test
    void createProductWithoutType() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
            .sellingStatus(ProductSellingStatus.SELLING)
            .name("아메리카노")
            .price(4000)
            .build();

        //when //then
        mockMvc.perform(post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("상품 타입은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty()
            );

    }

    @DisplayName("신규 상품을 등록할 때, 판매상태는 필수값이다.")
    @Test
    void createProductWithoutSellingStatus() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
            .type(ProductType.HANDMADE)
            .name("아메리카노")
            .price(4000)
            .build();

        //when //then
        mockMvc.perform(post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("상품 판매상태는 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty()
            );

    }

    @DisplayName("신규 상품을 등록할 때, 이름은 필수값이다.")
    @Test
    void createProductWithoutName() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
            .type(ProductType.HANDMADE)
            .sellingStatus(ProductSellingStatus.SELLING)
            .price(4000)
            .build();

        //when //then
        mockMvc.perform(post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("상품이름은 필수입니다."))
            .andExpect(jsonPath("$.data").isEmpty()
            );

    }

    @DisplayName("신규 상품을 등록할 때, 가격은 양수여야 한다.")
    @Test
    void createProductWithPositiveprice() throws Exception {
        //given
        ProductCreateRequest request = ProductCreateRequest.builder()
            .type(ProductType.HANDMADE)
            .sellingStatus(ProductSellingStatus.SELLING)
            .name("아메리카노")
            .price(-4000)
            .build();

        //when //then
        mockMvc.perform(post("/api/v1/products/new")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(jsonPath("$.message").value("상품가격은 양수여야 합니다."))
            .andExpect(jsonPath("$.data").isEmpty()
            );

    }

    @DisplayName("판매 상품을 조회한다.")
    @Test
    void getSellignProducts() throws Exception {
        //given
        List<ProductResponse> result = List.of();
        when(productService.getSellingProducts()).thenReturn(result);

        //when  //then
        mockMvc.perform(
                get("/api/v1/products/selling")
            )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.data").isEmpty()
            );

    }

}