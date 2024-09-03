package com.service.product_service.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ProductResponse {

    private Integer id;
    private String name;
    private String description;
    private String price;
}
