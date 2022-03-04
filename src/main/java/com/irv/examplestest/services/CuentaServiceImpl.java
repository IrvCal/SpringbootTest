package com.irv.examplestest.services;

import com.irv.examplestest.Data;
import com.irv.examplestest.exceptions.BancoNotFoundException;
import com.irv.examplestest.exceptions.DineroInsuficienteException;
import com.irv.examplestest.repository.BancoRepository;
import com.irv.examplestest.repository.CuentaRepository;
import com.irv.examplestest.web.mappers.BancoMapper;
import com.irv.examplestest.web.mappers.CuentaMapper;
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
    @Autowired
    CuentaMapper cuentaMapper;
    @Autowired
    BancoMapper bancoMapper;

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
        System.out.println(cuentaMapper);
        CuentaDTO cuentaDTO = cuentaMapper.cuentaToCuentaDTO(cuentaRepository.findById(cuentaId).orElseThrow());
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
        try {
            BancoDTO bancoDTO = null;
            bancoDTO = bancoMapper.bancoToBancoDto(
                    bancoRepository.findById(bancoId).orElseThrow(() ->
                            new BancoNotFoundException("No se econtro el banco con el id: "+bancoId))
            );
            int totalTransferencias = bancoDTO.getTotalTransferencias();
            bancoDTO.setTotalTransferencias(++totalTransferencias);
            bancoRepository.save(bancoMapper.bancoDtoToBanco(bancoDTO));

            CuentaDTO cOrigen = cuentaMapper.cuentaToCuentaDTO(cuentaRepository.findById(cuentaOrigen).orElseThrow());
            CuentaDTO cDestino = cuentaMapper.cuentaToCuentaDTO(cuentaRepository.findById(cuentaDestino).orElseThrow());
            cOrigen.debito(monto);
            cuentaRepository.save(cuentaMapper.cuentaDtoToCuenta(cOrigen));
            cDestino.credito(monto);
            cuentaRepository.save(cuentaMapper.cuentaDtoToCuenta(cDestino));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
