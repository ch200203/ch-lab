package com.ch.demo.order.api;

import com.ch.demo.order.api.dtos.CreateOrderRequest;
import com.ch.demo.order.api.dtos.OrderResponse;
import com.ch.demo.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 브라우저나 API 클라이언트가 주문 생성을 요청하는 진입점
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 요청 → 서비스 → JPA 저장소 흐름을 시작하는 POST 엔드포인트
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.ok(response);
    }
}
