package com.ch.demo.order.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record CreateOrderRequest(
        @NotBlank(message = "고객 이름은 필수입니다.") String customerName,
        @NotNull(message = "주문 금액을 입력하세요.") @Positive(message = "주문 금액은 양수여야 합니다.") BigDecimal amount
) {
}
