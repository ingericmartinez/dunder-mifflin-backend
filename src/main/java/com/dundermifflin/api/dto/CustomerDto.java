package com.dundermifflin.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CustomerDto {
    private String customerId;

    @NotBlank
    private String name;

    private String contactPerson;

    @NotBlank
    @Email
    private String email;

    private String phone;
    private String address;
}
