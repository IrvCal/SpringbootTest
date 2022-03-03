package com.irv.examplestest.services;

import com.irv.examplestest.exceptions.DineroInsuficienteException;
import com.irv.examplestest.repository.BancoRepository;
import com.irv.examplestest.repository.CuentaRepository;
import com.irv.examplestest.web.model.Banco;
import com.irv.examplestest.web.model.Cuenta;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class CuentaServiceImpl implements CuentaService {
    @Autowired
    private CuentaRepository cuentaRepository;
    @Autowired
    private BancoRepository bancoRepository;

    @Override
    public Cuenta findById(Long id) {
        return null;
    }

    @Override
    public int revisarTotalTransferencias(Long bancoId) {
        return 0;
    }

    @Override
    public BigDecimal revisarSaldo(Long cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId);
        return cuenta.getSaldo();
    }

    /**
     * tiene que incrementar en 1 el total de transferencias
     * @param cuentaOrigen
     * @param cuentaDestino
     * @param monto
     */
    @Override
    public void transferir(Long cuentaOrigen, Long cuentaDestino, BigDecimal monto,Long bancoId) {
        Banco banco = bancoRepository.findById(bancoId);
        int totalTransferencias =banco.getTotalTransferencias();
        banco.setTotalTransferencias(++totalTransferencias);
        bancoRepository.update(banco);

        Cuenta cOrigen = cuentaRepository.findById(cuentaOrigen);
        Cuenta cDestino = cuentaRepository.findById(cuentaDestino);

        try {
            cOrigen.debito(monto);
            cuentaRepository.update(cOrigen);
            cDestino.credito(monto);
            cuentaRepository.update(cDestino);
        } catch (DineroInsuficienteException e) {
            e.printStackTrace();
        }
    }
}
