package com.dundermifflin.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Data
public class OrderDto {
    private String orderId;

    @NotBlank
    private String customerId;

    @NotNull
    @NotEmpty
    @Valid
    private List<OrderItemDto> items;

    @NotBlank
    private String shippingAddress;

    private String status;
    private Timestamp orderDate;
    private BigDecimal totalAmount;
}
