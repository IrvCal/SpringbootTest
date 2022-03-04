package com.irv.examplestest.repository;

import com.irv.examplestest.web.model.CuentaDTO;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CuentaRepository {
    List<CuentaDTO> findAll();
    CuentaDTO findById(Long id);
    void update(CuentaDTO cuentaDTO);

}
