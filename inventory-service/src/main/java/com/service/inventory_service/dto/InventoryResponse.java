package com.service.inventory_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class InventoryResponse {
    private  String skuCode;
    private  boolean isInStock;
}
