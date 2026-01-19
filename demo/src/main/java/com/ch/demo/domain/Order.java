package com.ch.demo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;

// 주문 자체를 표현하는 JPA 엔티티로 트랜잭션 내에서 Outbox 레코드와 함께 저장된다.
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected Order() {
        // JPA용 기본 생성자
    }

    private Order(String customerName, BigDecimal amount, OrderStatus status) {
        this.customerName = customerName;
        this.amount = amount;
        this.status = status;
    }

    public static Order pending(String customerName, BigDecimal amount) {
        return new Order(customerName, amount, OrderStatus.PENDING);
    }

    public Long getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    @PrePersist
    void onPersist() {
        this.createdAt = Instant.now();
    }
}
