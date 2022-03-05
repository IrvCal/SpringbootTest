package com.irv.examplestest.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransaccionDto {
    private Long idCuentaOrigen;
    private Long idCuentaDestino;
    private Long idBanco;
    private BigDecimal monto;
}
