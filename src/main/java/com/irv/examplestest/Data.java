package com.irv.examplestest;

import com.irv.examplestest.web.model.Banco;
import com.irv.examplestest.web.model.Cuenta;

import java.math.BigDecimal;
import java.util.List;

public class Data {

    public static final Cuenta CUENTA_1 =Cuenta.builder().id(1L).persona("Irving").saldo(new BigDecimal(1000)).build();
    public static final Cuenta CUENTA_2 =Cuenta.builder().id(2L).persona("Juan").saldo(new BigDecimal(2000)).build();
    public static final Cuenta CUENTA_3 =Cuenta.builder().id(3L).persona("Antonio").saldo(new BigDecimal(3000)).build();
    public static final Banco BANCO_1 = Banco.builder().id(1L).nombre("Banamex").totalTransferencias(0).build();
    public static final Banco BANCO_2 = Banco.builder().id(2L).nombre("Patito").totalTransferencias(0).build();
    public static final Banco BANCO_3 = Banco.builder().id(3L).nombre("BBVA").totalTransferencias(0).build();
    public static final List<Cuenta>  CUENTAS = List.of(CUENTA_2,CUENTA_1,CUENTA_3);
    public static final List<Banco> BANCOS = List.of(
            Banco.builder().id(1L).nombre("Banamex").build(),
            Banco.builder().id(2L).nombre("Patito").build(),
            Banco.builder().id(3L).nombre("BBVA").build()
    );
}
