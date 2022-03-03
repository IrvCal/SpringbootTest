package com.irv.examplestest.repository;

import com.irv.examplestest.web.model.Cuenta;

import java.util.List;

public interface CuentaRepository {
    List<Cuenta> findAll();
    Cuenta findById(Long id);
    void update(Cuenta cuenta);

}
