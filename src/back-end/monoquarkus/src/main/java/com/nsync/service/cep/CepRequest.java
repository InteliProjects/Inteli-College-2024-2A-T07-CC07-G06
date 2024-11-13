package com.nsync.service.cep;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CepRequest {
    private String cep;
    private Long productId;
}
