package com.irv.examplestest.web.mappers;

import com.irv.examplestest.domain.Banco;
import com.irv.examplestest.web.model.BancoDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BancoMapper {
    Banco bancoDtoToBanco(BancoDTO bancoDTO);
    BancoDTO bancoToBancoDto(Banco banco);
}
