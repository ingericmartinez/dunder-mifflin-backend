package com.dundermifflin.api.dto;

import lombok.Data;

@Data
public class InventoryItemDto {
    private String productId;
    private String productName;
    private String branchId;
    private String branchName;
    private Integer quantityInStock;
}
