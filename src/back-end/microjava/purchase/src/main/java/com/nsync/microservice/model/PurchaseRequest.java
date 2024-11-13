package com.nsync.microservice.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PurchaseRequest {
    private List<ProductRequest> products;
    private String cep;
}
