package com.irv.examplestest.web.model;

import com.irv.examplestest.exceptions.DineroInsuficienteException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CuentaDTO {
    private Long id;
    private String persona;
    private BigDecimal saldo;


    //YO ESTO LO PONDRIA EN UN SERVICE

    /**
     * Cuanto tengo en la cuenta
     */
    public void debito(BigDecimal monto) throws DineroInsuficienteException {
        BigDecimal nuevoSaldo= this.saldo.subtract(monto);
        if(nuevoSaldo.compareTo(BigDecimal.ZERO)<0)
            throw new DineroInsuficienteException("Dinero insuficiente");
        this.saldo = nuevoSaldo;
    }

    /**
     * Cuanto se transfirio de otra
     * cuenta
     */
    public void credito(BigDecimal monto){
        this.saldo = this.saldo.add(monto);
    }

}
