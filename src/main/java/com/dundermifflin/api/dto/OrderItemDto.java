package com.dundermifflin.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    @NotBlank
    private String productId;

    @NotNull
    @Min(1)
    private Integer quantity;

    private BigDecimal pricePerBox;
    private BigDecimal subtotal;
}
