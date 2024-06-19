package com.example.practicaltest.spring.controller.order;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.practicaltest.spring.controller.order.request.OrderCreateRequest;
import com.example.practicaltest.spring.service.order.OrderService;
import com.example.practicaltest.spring.service.response.OrderResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/api/v1/orders/new")
    public OrderResponse createOrder(@RequestBody OrderCreateRequest request) {
        LocalDateTime registeredDateTime = LocalDateTime.now();

        return orderService.createOrder(request, registeredDateTime);
    }

}
