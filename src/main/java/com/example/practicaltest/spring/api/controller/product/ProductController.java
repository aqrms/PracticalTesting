package com.example.practicaltest.spring.api.controller.product;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.practicaltest.spring.api.ApiResponse;
import com.example.practicaltest.spring.api.controller.product.request.ProductCreateRequest;
import com.example.practicaltest.spring.api.service.product.ProductService;
import com.example.practicaltest.spring.domain.response.ProductResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ProductController {

    private final ProductService productService;

    @PostMapping("api/v1/products/new")
    public ApiResponse<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        return ApiResponse.of(HttpStatus.OK, productService.createProduct(request));
    }

    @GetMapping("api/v1/products/selling")
    public List<ProductResponse> getSellingProducts() {
        return productService.getSellingProducts();
    }

}
