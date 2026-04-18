package com.dundermifflin.api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaperProductDto {
    private String id;

    @NotBlank
    @Size(min = 2, max = 255)
    private String name;

    @Size(max = 1000)
    private String description;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal pricePerBox;

    @Size(max = 50)
    private String paperWeight;

    @Size(max = 50)
    private String color;
}
