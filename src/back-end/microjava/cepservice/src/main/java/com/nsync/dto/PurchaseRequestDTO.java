package com.nsync.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PurchaseRequestDTO {
    private List<ProductRequestDTO> products;
    private String cep;
}
