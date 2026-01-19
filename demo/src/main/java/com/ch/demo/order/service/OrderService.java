package com.ch.demo.order.service;

import com.ch.demo.domain.Order;
import com.ch.demo.domain.OrderRepository;
import com.ch.demo.order.api.dtos.CreateOrderRequest;
import com.ch.demo.order.api.dtos.OrderResponse;
import com.ch.demo.outbox.OutboxMessage;
import com.ch.demo.outbox.OutboxMessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 주문 생성과 Outbox 적재를 하나의 트랜잭션으로 묶어주는 서비스 계층
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OutboxMessageRepository outboxMessageRepository;
    private final ObjectMapper objectMapper;


    // REST 컨트롤러 → 서비스 → 저장소 순으로 호출되며, 같은 트랜잭션에서 주문과 Outbox 이벤트를 저장한다.
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        Order order = orderRepository.save(Order.pending(request.customerName(), request.amount()));

        OutboxMessage outboxMessage = OutboxMessage.pending(
                "ORDER",
                order.getId().toString(),
                "ORDER_CREATED",
                serializePayload(new OrderCreatedPayload(order.getId(), order.getCustomerName(), order.getAmount()))
        );

        outboxMessageRepository.save(outboxMessage);
        return OrderResponse.from(order);
    }

    private String serializePayload(OrderCreatedPayload payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("이벤트 페이로드 직렬화에 실패했습니다.", e);
        }
    }

    private record OrderCreatedPayload(Long orderId, String customerName, java.math.BigDecimal amount) {
    }
}
