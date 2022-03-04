package com.irv.examplestest.services;

import com.irv.examplestest.Data;
import com.irv.examplestest.exceptions.DineroInsuficienteException;
import com.irv.examplestest.repository.BancoRepository;
import com.irv.examplestest.repository.CuentaRepository;
import com.irv.examplestest.web.model.BancoDTO;
import com.irv.examplestest.web.model.CuentaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService {
    private CuentaRepository cuentaRepository;
    private BancoRepository bancoRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository, BancoRepository bancoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.bancoRepository = bancoRepository;
    }

    @Override
    public CuentaDTO findById(Long id) {
        return Data.CUENTA_DTOS.stream()
                .filter(cuenta -> cuenta.getId().equals(id))
                .findAny().orElse(null);
    }

    @Override
    public BancoDTO findByIdBanco(Long id) {
        return Data.BANCO_DTOS.stream()
                .filter(banco -> banco.getId().equals(id)).findAny().orElse(null);
    }

    @Override
    public int revisarTotalTransferencias(Long bancoId) {
        return Data.BANCO_DTOS.stream()
                .filter(banco -> banco.getId().equals(bancoId)).findAny().orElse(null).getTotalTransferencias();
    }

    @Override
    public BigDecimal revisarSaldo(Long cuentaId) {
        CuentaDTO cuentaDTO = cuentaRepository.findById(cuentaId);
        return cuentaDTO.getSaldo();
    }

    /**
     * tiene que incrementar en 1 el total de transferencias
     * @param cuentaOrigen
     * @param cuentaDestino
     * @param monto
     */
    @Override
    public void transferir(Long cuentaOrigen, Long cuentaDestino, BigDecimal monto,Long bancoId) {
        BancoDTO bancoDTO = bancoRepository.findById(bancoId);
        int totalTransferencias = bancoDTO.getTotalTransferencias();
        bancoDTO.setTotalTransferencias(++totalTransferencias);
        bancoRepository.update(bancoDTO);

        CuentaDTO cOrigen = cuentaRepository.findById(cuentaOrigen);
        CuentaDTO cDestino = cuentaRepository.findById(cuentaDestino);
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
