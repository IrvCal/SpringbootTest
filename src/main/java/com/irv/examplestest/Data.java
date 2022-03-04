package com.irv.examplestest;

import com.irv.examplestest.web.model.BancoDTO;
import com.irv.examplestest.web.model.CuentaDTO;

import java.math.BigDecimal;
import java.util.List;

public class Data {

    public static final CuentaDTO CUENTA_DTO_1 = CuentaDTO.builder().id(1L).persona("Irving").saldo(new BigDecimal(1000)).build();
    public static final CuentaDTO CUENTA_DTO_2 = CuentaDTO.builder().id(2L).persona("Juan").saldo(new BigDecimal(2000)).build();
    public static final CuentaDTO CUENTA_DTO_3 = CuentaDTO.builder().id(3L).persona("Antonio").saldo(new BigDecimal(3000)).build();
    public static final BancoDTO BANCO_DTO_1 = BancoDTO.builder().id(1L).nombre("Banamex").totalTransferencias(0).build();
    public static final BancoDTO BANCO_DTO_2 = BancoDTO.builder().id(2L).nombre("Patito").totalTransferencias(0).build();
    public static final BancoDTO BANCO_DTO_3 = BancoDTO.builder().id(3L).nombre("BBVA").totalTransferencias(0).build();
    public static final List<CuentaDTO> CUENTA_DTOS = List.of(CUENTA_DTO_2, CUENTA_DTO_1, CUENTA_DTO_3);
    public static final List<BancoDTO> BANCO_DTOS = List.of(
            BancoDTO.builder().id(1L).nombre("Banamex").build(),
            BancoDTO.builder().id(2L).nombre("Patito").build(),
            BancoDTO.builder().id(3L).nombre("BBVA").build()
    );
}
