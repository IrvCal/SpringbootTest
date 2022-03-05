package com.irv.examplestest.services;

import com.irv.examplestest.domain.Cuenta;
import com.irv.examplestest.web.model.BancoDTO;
import com.irv.examplestest.web.model.CuentaDTO;

import java.math.BigDecimal;

public interface CuentaService {
    CuentaDTO findById(Long id);
    Cuenta findByIdCuenta(Long id);
    BancoDTO findByIdBanco(Long id);
    int revisarTotalTransferencias(Long bancoId);
    BigDecimal revisarSaldo(Long cuentaId);
    void transferir(Long cuentaOrigen,Long cuentaDestino,BigDecimal monto,Long bancoId);
}
