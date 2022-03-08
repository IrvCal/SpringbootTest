package com.irv.examplestest.services;

import com.irv.examplestest.domain.Cuenta;
import com.irv.examplestest.web.model.BancoDTO;
import com.irv.examplestest.web.model.CuentaDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CuentaService {
    List<Cuenta> findAll();
    Cuenta save(Cuenta cuenta);
    CuentaDTO findById(Long id);
    Optional<Cuenta> findByIdCuenta(Long id);
    BancoDTO findByIdBanco(Long id);
    int revisarTotalTransferencias(Long bancoId);
    BigDecimal revisarSaldo(Long cuentaId);
    void transferir(Long cuentaOrigen,Long cuentaDestino,BigDecimal monto,Long bancoId);
    void delete(Long cuentaId);
}
