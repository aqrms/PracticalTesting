package com.example.practicaltest.spring.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.practicaltest.spring.service.response.OrderResponse;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
