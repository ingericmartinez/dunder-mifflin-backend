package com.dundermifflin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    @NotBlank
    @Pattern(regexp = "created|processing|shipped|delivered|canceled",
             message = "status must be one of: created, processing, shipped, delivered, canceled")
    private String status;

    private String trackingNumber;
}
