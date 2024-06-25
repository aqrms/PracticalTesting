package com.example.practicaltest.spring.api.service.order;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.practicaltest.spring.api.service.mail.MailService;
import com.example.practicaltest.spring.domain.order.Order;
import com.example.practicaltest.spring.domain.order.OrderRepository;
import com.example.practicaltest.spring.domain.order.OrderStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderStatService {

    private final OrderRepository orderRepository;
    private final MailService mailService;

    public boolean sendOrderStatMail(LocalDate date, String email) {

        List<Order> orders = orderRepository.findOrdersBy(
            date.atStartOfDay(),
            date.plusDays(1).atStartOfDay(),
            OrderStatus.PAYMENT_COMPLETED
        );

        int totalSales = orders.stream()
            .mapToInt(Order::getTotalPrice)
            .sum();

        boolean result = mailService.sendMail(
            "no-reply@cafe.com",
            email,
            String.format("매출통계] %s", date),
            String.format("총 매출 합계는 %s원 입니다.", totalSales)
        );
        if (!result) {
            throw new IllegalArgumentException("매출 통계 메일 전송에 실패했습니다.");
        }

        return true;

    }
}
