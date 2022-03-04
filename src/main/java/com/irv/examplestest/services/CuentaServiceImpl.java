package com.irv.examplestest.services;

import com.irv.examplestest.Data;
import com.irv.examplestest.exceptions.DineroInsuficienteException;
import com.irv.examplestest.repository.BancoRepository;
import com.irv.examplestest.repository.CuentaRepository;
import com.irv.examplestest.web.model.Banco;
import com.irv.examplestest.web.model.Cuenta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CuentaServiceImpl implements CuentaService {
    private CuentaRepository cuentaRepository;
    private BancoRepository bancoRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository, BancoRepository bancoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.bancoRepository = bancoRepository;
    }

    @Override
    public Cuenta findById(Long id) {
        return Data.CUENTAS.stream()
                .filter(cuenta -> cuenta.getId().equals(id))
                .findAny().orElse(null);
    }

    @Override
    public Banco findByIdBanco(Long id) {
        return Data.BANCOS.stream()
                .filter(banco -> banco.getId().equals(id)).findAny().orElse(null);
    }

    @Override
    public int revisarTotalTransferencias(Long bancoId) {
        return Data.BANCOS.stream()
                .filter(banco -> banco.getId().equals(bancoId)).findAny().orElse(null).getTotalTransferencias();
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
