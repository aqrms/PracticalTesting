package com.example.practicaltest.spring.service.order;

import static com.example.practicaltest.spring.domain.product.ProductSellingStatus.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.practicaltest.spring.controller.request.OrderCreateRequest;
import com.example.practicaltest.spring.domain.product.Product;
import com.example.practicaltest.spring.domain.product.ProductRepository;
import com.example.practicaltest.spring.domain.product.ProductType;
import com.example.practicaltest.spring.service.response.OrderResponse;

@ActiveProfiles("test")
@SpringBootTest
// @DataJpaTest
class OrderServiceTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderService orderService;

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    public void test() {
        //given
        Product product1 = createProduct(ProductType.HANDMADE, "001", 1000);
        Product product2 = createProduct(ProductType.HANDMADE, "002", 3000);
        Product product3 = createProduct(ProductType.HANDMADE, "003", 5000);
        productRepository.saveAll(List.of(product1,product2,product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
            .productNumbers(List.of("001", "002"))
            .build();
        //when
        LocalDateTime registeredDateTime = LocalDateTime.now();
        OrderResponse response = orderService.createOrder(request, registeredDateTime);

        //then
        assertThat(response.getId()).isNotNull();
        assertThat(response)
            .extracting("registeredDateTime", "totalPrice")
            .contains(registeredDateTime, 4000);
        assertThat(response.getProducts()).hasSize(2)
            .extracting("productNumber", "price")
            .containsExactlyInAnyOrder(
                tuple("001", 1000),
                tuple("002", 3000)
            );

    }

    private Product createProduct (ProductType type, String productNumber, int price) {
        return Product.builder()
            .type(type)
            .productNumber(productNumber)
            .price(price)
            .sellingStatus(SELLING)
            .name("메뉴 이름")
            .build();
    }

}