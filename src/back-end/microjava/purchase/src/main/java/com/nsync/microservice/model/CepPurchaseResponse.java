package com.nsync.microservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nsync.microservice.entity.Distributor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Represents a response containing date and purchase information related to a CEP query.
 * <p>
 * This class provides the date range and optional product details associated with a CEP (Postal Code) query,
 * including the first and last day of the period, product ID, and distributor details.
 * </p>
 *
 * @author mauroDasChagas
 *
 */

@Getter
public class CepPurchaseResponse extends CepResponse {
    private Long productId;

    public CepPurchaseResponse() {
    }

    public CepPurchaseResponse(LocalDate firstDay, LocalDate lastDay, Long productId, Distributor distributor) {
        super(firstDay, lastDay, distributor);
        this.productId = productId;
    }
}
