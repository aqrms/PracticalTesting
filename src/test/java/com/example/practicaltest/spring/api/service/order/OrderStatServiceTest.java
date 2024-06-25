package com.example.practicaltest.spring.api.service.order;

import static com.example.practicaltest.spring.domain.product.ProductSellingStatus.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.practicaltest.spring.domain.history.mail.MailSendHistory;
import com.example.practicaltest.spring.domain.history.mail.MailSendHistoryRepository;
import com.example.practicaltest.spring.domain.order.Order;
import com.example.practicaltest.spring.domain.order.OrderRepository;
import com.example.practicaltest.spring.domain.order.OrderStatus;
import com.example.practicaltest.spring.domain.orderproduct.OrderProductRepository;
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
    private OrderProductRepository orderProductRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MailSendHistoryRepository mailSendHistoryRepository;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        mailSendHistoryRepository.deleteAllInBatch();
    }

    @DisplayName("결제완료 주문들을 조회하여 매출 통계 메일을 전송한다.")
    @Test
    void sendOrderStatMail() {
        //given
        LocalDateTime now = LocalDateTime.of(2024, 6, 25, 0, 0);

        Product product1 = createProduct(ProductType.HANDMADE, "001", 1000);
        Product product2 = createProduct(ProductType.HANDMADE, "002", 3000);
        Product product3 = createProduct(ProductType.HANDMADE, "003", 5000);
        List<Product> products = List.of(product1, product2, product3);
        productRepository.saveAll(products);

        Order order1 = createPaymentCompleteOrder(products, LocalDateTime.of(2024, 6, 24, 23, 59));
        Order order2 = createPaymentCompleteOrder(products, now);
        Order order3 = createPaymentCompleteOrder(products, LocalDateTime.of(2024, 6, 25, 23, 59));
        Order order4 = createPaymentCompleteOrder(products, LocalDateTime.of(2024, 6, 26, 0, 0));

        //when
        boolean result = orderStatService.sendOrderStatMail(LocalDate.of(2024, 6, 25), "test@test.com");

        //then
        assertThat(result).isTrue();
        List<MailSendHistory> histories = mailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(1)
            .extracting("content")
            .contains("총 매출 합계는 18000원 입니다.");

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