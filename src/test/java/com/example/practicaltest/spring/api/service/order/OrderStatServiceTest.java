package com.example.practicaltest.spring.api.service.order;

import static com.example.practicaltest.spring.domain.product.ProductSellingStatus.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.practicaltest.spring.domain.order.Order;
import com.example.practicaltest.spring.domain.order.OrderRepository;
import com.example.practicaltest.spring.domain.order.OrderStatus;
import com.example.practicaltest.spring.domain.product.Product;
import com.example.practicaltest.spring.domain.product.ProductRepository;
import com.example.practicaltest.spring.domain.product.ProductType;

@SpringBootTest
class OrderStatServiceTest {

    @Autowired
    private OrderStatService orderStatService;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
    }

    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    @Test
    void sendOrderStatMail() {
        //given
        LocalDateTime now = LocalDateTime.now();

        Product product1 = createProduct(ProductType.HANDMADE, "001", 1000);
        Product product2 = createProduct(ProductType.HANDMADE, "002", 3000);
        Product product3 = createProduct(ProductType.HANDMADE, "003", 5000);
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);

        Order order1 = createPaymentCompleteOrder(products, now);
        Order order2 = createPaymentCompleteOrder(products, now);
        Order order3 = createPaymentCompleteOrder(products, now);

        //when

        //then

    }

    private Order createPaymentCompleteOrder(List<Product> products, LocalDateTime now) {
        Order order1 = Order.builder()
            .products(products)
            .orderStatus(OrderStatus.PAYMENT_COMPLETED)
            .registeredDateTime(now)
            .build();
        return orderRepository.save(order1);
    }

    private Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
            .type(type)
            .productNumber(productNumber)
            .price(price)
            .sellingStatus(SELLING)
            .name("메뉴 이름")
            .build();
    }
}