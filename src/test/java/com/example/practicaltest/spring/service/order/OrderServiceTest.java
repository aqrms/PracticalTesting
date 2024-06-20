package com.example.practicaltest.spring.service.order;

import static com.example.practicaltest.spring.domain.product.ProductSellingStatus.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.example.practicaltest.spring.api.controller.order.request.OrderCreateRequest;
import com.example.practicaltest.spring.api.service.order.OrderService;
import com.example.practicaltest.spring.api.service.response.OrderResponse;
import com.example.practicaltest.spring.domain.order.OrderRepository;
import com.example.practicaltest.spring.domain.orderproduct.OrderProductRepository;
import com.example.practicaltest.spring.domain.product.Product;
import com.example.practicaltest.spring.domain.product.ProductRepository;
import com.example.practicaltest.spring.domain.product.ProductType;
import com.example.practicaltest.spring.domain.stock.Stock;
import com.example.practicaltest.spring.domain.stock.StockRepository;

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private StockRepository stockRepository;

    @AfterEach
    void tearDown() {
        orderProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        stockRepository.deleteAllInBatch();
    }

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    void createOrder() {
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(ProductType.HANDMADE, "001", 1000);
        Product product2 = createProduct(ProductType.HANDMADE, "002", 3000);
        Product product3 = createProduct(ProductType.HANDMADE, "003", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
            .productNumbers(List.of("001", "002"))
            .build();
        //when
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

    @DisplayName("재고가 있는 상품이 포함된 주문번호 리스트로 주문을 생성한다.")
    @Test
    void createOrderWithStock() {
        //given
        LocalDateTime registeredDateTime = LocalDateTime.now();

        Product product1 = createProduct(ProductType.BOTTLE, "001", 1000);
        Product product2 = createProduct(ProductType.BAKERY, "002", 3000);
        Product product3 = createProduct(ProductType.HANDMADE, "003", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        Stock stock1 = Stock.create("001", 2);
        Stock stock2 = Stock.create("002", 2);
        stockRepository.saveAll(List.of(stock1, stock2));

        OrderCreateRequest request = OrderCreateRequest.builder()
            .productNumbers(List.of("001", "001", "002", "003"))
            .build();

        //when
        OrderResponse response = orderService.createOrder(request, registeredDateTime);

        //then
        assertThat(response.getId()).isNotNull();
        assertThat(response)
            .extracting("registeredDateTime", "totalPrice")
            .contains(registeredDateTime, 10000);
        assertThat(response.getProducts()).hasSize(4)
            .extracting("productNumber", "price")
            .containsExactlyInAnyOrder(
                tuple("001", 1000),
                tuple("001", 1000),
                tuple("002", 3000),
                tuple("003", 5000)
            );

        List<Stock> stocks = stockRepository.findAll();
        assertThat(stocks).hasSize(2)
            .extracting("productNumber", "quantity")
            .containsExactlyInAnyOrder(
                tuple("001", 0),
                tuple("002", 1)
            );
    }

    @DisplayName("중복 상품번호 리스트로 주문을 생성할 수 있다.")
    @Test
    void createOrderWithDuplicateProductNumbers() {
        //given
        Product product1 = createProduct(ProductType.HANDMADE, "001", 1000);
        Product product2 = createProduct(ProductType.HANDMADE, "002", 3000);
        Product product3 = createProduct(ProductType.HANDMADE, "003", 5000);
        productRepository.saveAll(List.of(product1, product2, product3));

        OrderCreateRequest request = OrderCreateRequest.builder()
            .productNumbers(List.of("001", "001"))
            .build();
        //when
        LocalDateTime registeredDateTime = LocalDateTime.now();
        OrderResponse response = orderService.createOrder(request, registeredDateTime);

        //then
        assertThat(response.getId()).isNotNull();
        assertThat(response)
            .extracting("registeredDateTime", "totalPrice")
            .contains(registeredDateTime, 2000);
        assertThat(response.getProducts()).hasSize(2)
            .extracting("productNumber", "price")
            .containsExactlyInAnyOrder(
                tuple("001", 1000),
                tuple("001", 1000)
            );

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