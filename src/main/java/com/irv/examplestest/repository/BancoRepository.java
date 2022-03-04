package com.irv.examplestest.repository;

import com.irv.examplestest.web.model.BancoDTO;

import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface BancoRepository {
    List<BancoDTO> findAll();

    BancoDTO findById(Long id);

    void update(BancoDTO bancoDTO);
}
