package com.example.practicaltest.spring.service.product;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.practicaltest.spring.controller.product.request.ProductCreateRequest;
import com.example.practicaltest.spring.domain.product.Product;
import com.example.practicaltest.spring.domain.product.ProductRepository;
import com.example.practicaltest.spring.domain.product.ProductSellingStatus;
import com.example.practicaltest.spring.domain.response.ProductResponse;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        String nextProductNumber = createNextProductNumber();
        Product product = request.toEntity(nextProductNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.of(savedProduct);
    }

    public List<ProductResponse> getSellingProducts() {
        List<Product> products = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return products.stream()
            .map(ProductResponse::of)
            .toList();

    }

    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();
        if (latestProductNumber == null) {
            return "001";
        }

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        int nextProductNumberInt = latestProductNumberInt + 1;

        return String.format("%03d", nextProductNumberInt);
    }

}
