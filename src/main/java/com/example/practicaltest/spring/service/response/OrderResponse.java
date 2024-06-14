package com.example.practicaltest.spring.service.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.practicaltest.spring.domain.order.Order;
import com.example.practicaltest.spring.domain.order.OrderStatus;
import com.example.practicaltest.spring.domain.orderproduct.OrderProduct;
import com.example.practicaltest.spring.domain.response.ProductResponse;

import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderResponse {

    private Long id;
    private int totalPrice;
    private LocalDateTime registeredDateTime;
    private List<ProductResponse> products;

    @Builder
    public OrderResponse(Long id, int totalPrice, LocalDateTime registeredDateTime, List<ProductResponse> products) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.registeredDateTime = registeredDateTime;
        this.products = products;
    }

    public static OrderResponse of(Order order) {
        return OrderResponse.builder()
            .id(order.getId())
            .totalPrice(order.getTotalPrice())
            .registeredDateTime(order.getRegisteredDateTime())
            .products(order.getOrderProducts().stream()
                .map(orderProduct -> ProductResponse.of(orderProduct.getProduct()))
                .toList())
            .build();
    }
}
