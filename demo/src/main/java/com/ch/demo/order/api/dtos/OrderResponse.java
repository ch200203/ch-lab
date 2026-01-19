package com.ch.demo.order.api.dtos;

import com.ch.demo.domain.Order;
import com.ch.demo.domain.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record OrderResponse(
        Long id,
        String customerName,
        BigDecimal amount,
        OrderStatus status,
        Instant createdAt
) {
    public static OrderResponse from(Order order) {
        return new OrderResponse(order.getId(), order.getCustomerName(), order.getAmount(), order.getStatus(), order.getCreatedAt());
    }
}
