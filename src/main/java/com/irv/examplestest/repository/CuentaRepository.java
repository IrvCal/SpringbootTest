package com.irv.examplestest.repository;

import com.irv.examplestest.domain.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta,Long> {
    @Query("select c from Cuenta c where c.persona =?1")//-> consulta hibernateQueryLenguage
    Optional<Cuenta> findByPersona(String persona);
}
