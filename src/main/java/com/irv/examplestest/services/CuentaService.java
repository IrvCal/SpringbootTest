package com.irv.examplestest.services;

import com.irv.examplestest.web.model.Banco;
import com.irv.examplestest.web.model.Cuenta;

import java.math.BigDecimal;

public interface CuentaService {
    Cuenta findById(Long id);
    Banco findByIdBanco(Long id);
    int revisarTotalTransferencias(Long bancoId);
    BigDecimal revisarSaldo(Long cuentaId);
    void transferir(Long cuentaOrigen,Long cuentaDestino,BigDecimal monto,Long bancoId);
}
