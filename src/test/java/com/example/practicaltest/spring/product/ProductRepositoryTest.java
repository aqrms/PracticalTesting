package com.example.practicaltest.spring.product;

import static com.example.practicaltest.spring.domain.product.ProductSellingStatus.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.practicaltest.spring.domain.product.Product;
import com.example.practicaltest.spring.domain.product.ProductRepository;
import com.example.practicaltest.spring.domain.product.ProductSellingStatus;
import com.example.practicaltest.spring.domain.product.ProductType;

@ActiveProfiles("test")
//@SpringBootTest
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다")
    @Test
    public void findAllBySellignStatusIn() {
        //given
        Product product1 = Product.builder()
            .productNumber("001")
            .type(ProductType.HANDMADE)
            .sellingStatus(SELLING)
            .name("아메리카노")
            .price(4000)
            .build();
        Product product2 = Product.builder()
            .productNumber("002")
            .type(ProductType.HANDMADE)
            .sellingStatus(ProductSellingStatus.HOLD)
            .name("카페라떼")
            .price(4500)
            .build();
        Product product3 = Product.builder()
            .productNumber("003")
            .type(ProductType.HANDMADE)
            .sellingStatus(ProductSellingStatus.STOP_SELLING)
            .name("팥빙수")
            .price(7000)
            .build();

        productRepository.saveAll(List.of(product1,product2,product3));

        //when
        List<Product> products = productRepository.findAllBySellingStatusIn(
            List.of(SELLING, ProductSellingStatus.HOLD));

        //then
        Assertions.assertThat(products).hasSize(2)
            .extracting("productNumber","name","sellingStatus")
            .containsExactlyInAnyOrder(
                tuple("001","아메리카노",SELLING),
                tuple("002","카페라떼",HOLD)
            );
    }
    @DisplayName("상품번호 리스트로 상품들을 조회한다")
    @Test
    public void findAllByProductNumbersIn() {
        //given
        Product product1 = Product.builder()
            .productNumber("001")
            .type(ProductType.HANDMADE)
            .sellingStatus(SELLING)
            .name("아메리카노")
            .price(4000)
            .build();
        Product product2 = Product.builder()
            .productNumber("002")
            .type(ProductType.HANDMADE)
            .sellingStatus(ProductSellingStatus.HOLD)
            .name("카페라떼")
            .price(4500)
            .build();
        Product product3 = Product.builder()
            .productNumber("003")
            .type(ProductType.HANDMADE)
            .sellingStatus(ProductSellingStatus.STOP_SELLING)
            .name("팥빙수")
            .price(7000)
            .build();

        productRepository.saveAll(List.of(product1,product2,product3));

        //when
        List<Product> products = productRepository.findAllByProductNumberIn(
            List.of("001", "002"));

        //then
        Assertions.assertThat(products).hasSize(2)
            .extracting("productNumber","name","sellingStatus")
            .containsExactlyInAnyOrder(
                tuple("001","아메리카노",SELLING),
                tuple("002","카페라떼",HOLD)
            );
    }

}
