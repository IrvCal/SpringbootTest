package com.irv.examplestest;

import com.irv.examplestest.exceptions.DineroInsuficienteException;
import com.irv.examplestest.repository.BancoRepository;
import com.irv.examplestest.repository.CuentaRepository;
import com.irv.examplestest.services.CuentaService;
import com.irv.examplestest.services.CuentaServiceImpl;
import com.irv.examplestest.web.model.Banco;
import com.irv.examplestest.web.model.Cuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Para esta primera clase de pruebas
 * intente hacerlo con autowired y no me salio
 * para empezar me decia que no habia algun Bean
 * luego para poder usar los when y todos lo del
 * primerTest() hay que definir las instancias
 * como MOCKS e inyectar InjectMocks porque si no
 * no va a jalar lo que que se ha visto hasta el momento
 * con los ejercicios de Mockito y JUnit
 * En el Service deje dos metodos de find cuenta y banco
 * con un stream que me gusto
 */
@SpringBootTest
class ExamplesTestApplicationTests {

//	@Mock//No se por que no me esta dando con AUTOWIRED (solucion era @Service en CuentaServiceImpl)
	@MockBean
	CuentaRepository cuentaRepository;
//	@Mock
	@MockBean
	BancoRepository bancoRepository;
//	@InjectMocks
	@Autowired
	CuentaServiceImpl service;

	@Test
	void primerTest() {
		//Aqui se mockearon los datos porque no se tiene una
		// implementacion de los metodos de la interfaz
		// (Hasta ahora)
		when(cuentaRepository.findById(1L)).thenReturn(Data.CUENTA_1);
		when(cuentaRepository.findById(2L)).thenReturn(Data.CUENTA_2);
		when(bancoRepository.findById(1L)).thenReturn(Data.BANCO_1);

		Cuenta cuentaOrigen = service.findById(1L);
		Cuenta cuentaDestino = service.findById(2L);
		Banco banco = service.findByIdBanco(1L);
		assertNotNull(banco);
		assertNotNull(cuentaOrigen);
		assertNotNull(cuentaDestino);
		assertEquals("Irving",cuentaOrigen.getPersona());
		assertEquals("Juan",cuentaDestino.getPersona());

		service.transferir(cuentaOrigen.getId(),cuentaDestino.getId(),new BigDecimal(100),banco.getId());
		BigDecimal saldoOrigen = service.revisarSaldo(cuentaOrigen.getId());
		BigDecimal saldoDestino = service.revisarSaldo(cuentaDestino.getId());

//		Cuantas veces se ejecutaron los metodos siguientes
		verify(cuentaRepository, times(2)).findById(1L);
		verify(cuentaRepository, times(2)).findById(2L);
		verify(cuentaRepository, times(4)).findById(anyLong());
		verify(cuentaRepository,times(2)).update(any(Cuenta.class));

		verify(bancoRepository,times(1)).findById(anyLong());
		verify(bancoRepository,times(1)).update(any(Banco.class));

		int totalTransferencias = service.revisarTotalTransferencias(banco.getId());//este no me salio
		System.out.println(totalTransferencias);
	}

	//checar este metodo con el codigo original
	@Test
	void manejoExceptions() {
		when(cuentaRepository.findById(1L)).thenReturn(Data.CUENTA_1);
		when(cuentaRepository.findById(2L)).thenReturn(Data.CUENTA_2);
		when(bancoRepository.findById(1L)).thenReturn(Data.BANCO_1);

		Cuenta cuentaOrigen = service.findById(1L);
		Cuenta cuentaDestino = service.findById(2L);
		Banco banco = service.findByIdBanco(1L);

		assertEquals("1000",cuentaOrigen.getSaldo().toPlainString());
		assertEquals("2000",cuentaDestino.getSaldo().toPlainString());
		assertThrows(DineroInsuficienteException.class,
				() -> service.transferir(1L,2L, new BigDecimal(5000),1L));

		
	}
	//Assert Same
	@Test
	void testAssertSame() {
		when(cuentaRepository.findById(1L)).thenReturn(Data.CUENTA_1);
		Cuenta cuenta = service.findById(1L);
		Cuenta cuenta2 = service.findById(1L);
		assertNotNull(cuenta);
		assertNotNull(cuenta2);
		assertSame(cuenta,cuenta2);//es el mismo objeto
		verify(cuentaRepository, times(2)).findById(1L);//tampoco me jalo pero ya perdi mucho tiempo
	}
}
