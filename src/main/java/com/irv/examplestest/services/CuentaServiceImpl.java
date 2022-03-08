package com.irv.examplestest.services;

import com.irv.examplestest.Data;
import com.irv.examplestest.domain.Cuenta;
import com.irv.examplestest.exceptions.BancoNotFoundException;
import com.irv.examplestest.exceptions.CuentaPersonaNotFoundException;
import com.irv.examplestest.repository.BancoRepository;
import com.irv.examplestest.repository.CuentaRepository;
import com.irv.examplestest.web.mappers.BancoMapper;
import com.irv.examplestest.web.mappers.CuentaMapper;
import com.irv.examplestest.web.model.BancoDTO;
import com.irv.examplestest.web.model.CuentaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService {
    private CuentaRepository cuentaRepository;
    private BancoRepository bancoRepository;
    @Autowired
    CuentaMapper cuentaMapper;
    @Autowired
    BancoMapper bancoMapper;
    @Autowired
    public CuentaServiceImpl(CuentaRepository cuentaRepository, BancoRepository bancoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.bancoRepository = bancoRepository;
    }

    @Override
    public List<Cuenta> findAll() {
        return cuentaRepository.findAll();
    }

    @Override
    public Cuenta save(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    @Override
    @Transactional(readOnly = true)
    public CuentaDTO findById(Long id) {
        return Data.CUENTA_DTOS.stream()
                .filter(cuenta -> cuenta.getId().equals(id))
                .findAny().orElseThrow(() -> new CuentaPersonaNotFoundException("No existe esta cuenta"));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cuenta> findByIdCuenta(Long id) {
        return cuentaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public BancoDTO findByIdBanco(Long id) {
        return Data.BANCO_DTOS.stream()
                .filter(banco -> banco.getId().equals(id)).findAny().orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public int revisarTotalTransferencias(Long bancoId) {
        return Data.BANCO_DTOS.stream()
                .filter(banco -> banco.getId().equals(bancoId)).findAny().orElse(null).getTotalTransferencias();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal revisarSaldo(Long cuentaId) {
        return cuentaMapper.cuentaToCuentaDTO(
                cuentaRepository.findById(cuentaId).orElseThrow()).getSaldo();

    }

    /**
     * tiene que incrementar en 1 el total de transferencias
     * @param cuentaOrigen
     * @param cuentaDestino
     * @param monto
     */
    @Override
    @Transactional
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

    @Override
    @Transactional
    public void delete(Long cuenta) {
        cuentaRepository.deleteById(cuenta);
    }
}
