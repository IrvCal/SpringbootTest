package com.irv.examplestest.web.mappers;

import com.irv.examplestest.domain.Cuenta;
import com.irv.examplestest.web.model.CuentaDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = {})
public interface CuentaMapper {
    Cuenta cuentaDtoToCuenta(CuentaDTO cuentaDTO);
    CuentaDTO cuentaToCuentaDTO(Cuenta cuenta);
    List<Cuenta> cuentasDtoToCuentas(List<CuentaDTO> cuentaDTOS);
}
