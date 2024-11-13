package com.nsync.microservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private Long productId;
    private Integer quantity;
}
