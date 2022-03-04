package com.irv.examplestest.repository;

import com.irv.examplestest.domain.Cuenta;
import com.irv.examplestest.web.model.CuentaDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta,Long> {
//    List<CuentaDTO> findAll();
//    Optional<CuentaDTO> findById(Long id);
//    void update(CuentaDTO cuentaDTO);

}
