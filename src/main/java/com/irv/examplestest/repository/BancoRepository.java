package com.irv.examplestest.repository;

import com.irv.examplestest.web.model.Banco;

import java.util.List;

public interface BancoRepository {
    List<Banco> findAll();
    Banco findById(Long id);
    void update(Banco banco);
}
