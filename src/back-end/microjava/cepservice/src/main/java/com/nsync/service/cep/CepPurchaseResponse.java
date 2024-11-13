package com.nsync.service.cep;

import com.nsync.entity.Distributor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CepPurchaseResponse extends CepResponse {
    private Long productId;

    public CepPurchaseResponse(LocalDate firstDay, LocalDate lastDay, Long productId, Distributor distributor) {
        super(firstDay, lastDay, distributor);
        this.productId = productId;
    }
}
