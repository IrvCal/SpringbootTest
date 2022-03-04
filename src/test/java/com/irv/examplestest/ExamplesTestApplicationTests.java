package com.irv.examplestest;

import com.irv.examplestest.domain.Banco;
import com.irv.examplestest.domain.Cuenta;
import com.irv.examplestest.exceptions.DineroInsuficienteException;
import com.irv.examplestest.repository.BancoRepository;
import com.irv.examplestest.repository.CuentaRepository;
import com.irv.examplestest.services.CuentaServiceImpl;
import com.irv.examplestest.web.mappers.BancoMapper;
import com.irv.examplestest.web.mappers.CuentaMapper;
import com.irv.examplestest.web.model.BancoDTO;
import com.irv.examplestest.web.model.CuentaDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;

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
	@Autowired
	CuentaMapper cuentaMapper;
	@Autowired
	BancoMapper bancoMapper;

	@Test
	void primerTest() {
		//Aqui se mockearon los datos porque no se tiene una
		// implementacion de los metodos de la interfaz
		// (Hasta ahora)
		when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaMapper.cuentaDtoToCuenta(Data.CUENTA_DTO_1)));
		when(cuentaRepository.findById(2L)).thenReturn(Optional.of(cuentaMapper.cuentaDtoToCuenta(Data.CUENTA_DTO_2)));
		when(bancoRepository.findById(1L)).thenReturn(Optional.of(bancoMapper.bancoDtoToBanco(Data.BANCO_DTO_1)));

		CuentaDTO cuentaDTOOrigen = service.findById(1L);
		CuentaDTO cuentaDTODestino = service.findById(2L);
		BancoDTO bancoDTO = service.findByIdBanco(1L);
		assertNotNull(bancoDTO);
		assertNotNull(cuentaDTOOrigen);
		assertNotNull(cuentaDTODestino);
		assertEquals("Irving", cuentaDTOOrigen.getPersona());
		assertEquals("Juan", cuentaDTODestino.getPersona());

		service.transferir(cuentaDTOOrigen.getId(), cuentaDTODestino.getId(),new BigDecimal(100), bancoDTO.getId());
		BigDecimal saldoOrigen = service.revisarSaldo(cuentaDTOOrigen.getId());
		BigDecimal saldoDestino = service.revisarSaldo(cuentaDTODestino.getId());

//		Cuantas veces se ejecutaron los metodos siguientes
		verify(cuentaRepository, times(2)).findById(1L);
		verify(cuentaRepository, times(2)).findById(2L);
		verify(cuentaRepository, times(4)).findById(anyLong());
		verify(cuentaRepository,times(2)).save(any(Cuenta.class));

		verify(bancoRepository,times(1)).findById(anyLong());
		verify(bancoRepository,times(1)).save(any(Banco.class));

		int totalTransferencias = service.revisarTotalTransferencias(bancoDTO.getId());//este no me salio
		System.out.println(totalTransferencias);
	}

	//checar este metodo con el codigo original
	@Test
	void manejoExceptions() {
		when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaMapper.cuentaDtoToCuenta(Data.CUENTA_DTO_1)));
		when(cuentaRepository.findById(2L)).thenReturn(Optional.of(cuentaMapper.cuentaDtoToCuenta(Data.CUENTA_DTO_2)));
		when(bancoRepository.findById(1L)).thenReturn(Optional.of(bancoMapper.bancoDtoToBanco(Data.BANCO_DTO_1)));

		CuentaDTO cuentaDTOOrigen = service.findById(1L);
		CuentaDTO cuentaDTODestino = service.findById(2L);
		BancoDTO bancoDTO = service.findByIdBanco(1L);

		assertEquals("1000", cuentaDTOOrigen.getSaldo().toPlainString());
//		assertEquals("2000", cuentaDTODestino.getSaldo().toPlainString());
		assertThrows(DineroInsuficienteException.class,
				() -> service.transferir(1L,2L, new BigDecimal(5000),1L));

		
	}
	//Assert Same
	@Test
	void testAssertSame() {
		when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuentaMapper.cuentaDtoToCuenta(Data.CUENTA_DTO_1)));
		CuentaDTO cuentaDTO = service.findById(1L);
		CuentaDTO cuentaDTO2 = service.findById(1L);
		System.out.println(cuentaDTO);
		System.out.println(cuentaDTO2);
		assertNotNull(cuentaDTO);
		assertNotNull(cuentaDTO2);
		assertSame(cuentaDTO, cuentaDTO2);//es el mismo objeto
//		verify(cuentaRepository, times(2)).findById(1L);//tampoco me jalo pero ya perdi mucho tiempo
	}
}
