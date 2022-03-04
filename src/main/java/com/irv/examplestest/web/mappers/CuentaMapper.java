package com.irv.examplestest.web.mappers;

import com.irv.examplestest.domain.Cuenta;
import com.irv.examplestest.web.model.CuentaDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CuentaMapper {
    Cuenta cuentaDtoToCuenta(CuentaDTO cuentaDTO);
    CuentaDTO cuentaToCuentaDTO(Cuenta cuenta);
}
