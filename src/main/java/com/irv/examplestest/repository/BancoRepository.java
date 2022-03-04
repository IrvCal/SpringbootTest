package com.irv.examplestest.repository;

import com.irv.examplestest.domain.Banco;
import com.irv.examplestest.web.model.BancoDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface BancoRepository extends JpaRepository<Banco,Long> {
//    List<BancoDTO> findAll();
//
//    BancoDTO findById(Long id);
//
//    void update(BancoDTO bancoDTO);
}
