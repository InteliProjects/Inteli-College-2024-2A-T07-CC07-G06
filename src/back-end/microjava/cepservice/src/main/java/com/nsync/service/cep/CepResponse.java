package com.nsync.service.cep;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nsync.entity.Distributor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bytebuddy.asm.Advice;

import java.time.LocalDate;

/**
 * Represents a response containing date information related to a CEP query.
 * <p>
 * This class provides the date range associated with a CEP (Postal Code) query,
 * including the first and last day of the period.
 * </p>
 *
 * @author mauroDasChagas
 *
 */

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CepResponse {
    public LocalDate firstDay;
    public LocalDate lastDay;
    public Distributor distributor;

    public CepResponse(LocalDate firstDay, LocalDate lastDay) {
        this.firstDay = firstDay;
        this.lastDay = lastDay;
    }

    public CepResponse(LocalDate firstDay, LocalDate lastDay, Distributor distributor) {
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.distributor = distributor;
    }
}
