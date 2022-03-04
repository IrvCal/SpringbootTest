package com.irv.examplestest;

import com.irv.examplestest.domain.Cuenta;
import com.irv.examplestest.exceptions.CuentaPersonaNotFoundException;
import com.irv.examplestest.repository.CuentaRepository;
import com.irv.examplestest.web.mappers.CuentaMapper;
import com.irv.examplestest.web.mappers.CuentaMapperImpl;
import com.irv.examplestest.web.model.CuentaDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Aqui se van a hacer tests de los repositorios
 */
@DataJpaTest
public class ExamplesTestApp2 {
    @Autowired
    CuentaRepository cuentaRepository;
//    @Autowired CuentaMapper cuentaMapper; //no lo jala porque no esta cargando como todos el contexto de Spring
    @Test
    void findById() {
        Optional<Cuenta> cuenta = cuentaRepository.findById(1L);
        assertTrue(cuenta.isPresent());
    }
    //ahora se va a probar el query personalizado de persona
    @Test
    void findByPersona() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Irving");
        assertTrue(cuenta.isPresent());
        assertEquals("Irving",cuenta.get().getPersona());
    }
//este es con una lambda
    @Test
    void findByPersonaException() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Irsving");
        assertThrows(NoSuchElementException.class,() -> cuenta.orElseThrow());
    }
//este es con una excepcion personalizada
    @Test
    @DisplayName("Excepcion Personalizada")
    void findByPersonaExceptionPersonalizada() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("ving");
        assertFalse(cuenta.isPresent());
        assertThrows(CuentaPersonaNotFoundException.class,
                () -> cuenta.orElseThrow(() -> new CuentaPersonaNotFoundException("No se encontro")));
    }
    //A metodo de referencia
    @Test
    void findByPersonaExceptionReferenciaAlMetodo() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Irsving");
        assertThrows(NoSuchElementException.class,cuenta::orElseThrow);
    }

    @Test
    void testFindAll() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        assertFalse(cuentas.isEmpty());
        assertEquals(3,cuentas.size());
    }
//////////// CON MAPPER
    @Test
    void saveSomeCuenta() {
        CuentaMapper cuentaMapper = new CuentaMapperImpl();//no jalo el @Autowired
        //Given un contexto
        Cuenta cuenta = cuentaMapper.cuentaDtoToCuenta(
                CuentaDTO.builder().persona("Jose").saldo(new BigDecimal(5000)).build());
        cuentaRepository.save(cuenta);
        //When
        Cuenta cuenta1 = cuentaRepository.findByPersona("Jose").orElseThrow();
        //Then
        assertEquals("Jose",cuenta1.getPersona());
    }
    //casi lo mismo que el de arriba pero ahora con la prueba de con que id se guardo en DB

    @Test
    void saveSomeCuentaRetrieveById() {
        CuentaMapper cuentaMapper = new CuentaMapperImpl();//no jalo el @Autowired
        //Given un contexto
        Cuenta cuenta = cuentaMapper.cuentaDtoToCuenta(
                CuentaDTO.builder().persona("Jose").saldo(new BigDecimal(5000)).build());
        Cuenta saved = cuentaRepository.save(cuenta);
        //When
        Cuenta cuenta1 = cuentaRepository.findById(saved.getId()).orElseThrow();
        //Then
        assertEquals("Jose",cuenta1.getPersona());
    }
    //Este es el de arriba pero ordenado y explicado

    @Test
    void saveSomeCuentaRetrieveByIdOrdenado() {
        CuentaMapper cuentaMapper = new CuentaMapperImpl();
        //Given una cuenta a guardar
        Cuenta cuenta = cuentaMapper.cuentaDtoToCuenta(
                CuentaDTO.builder().persona("Jose").saldo(new BigDecimal(5000)).build());
        //When se guarda
        Cuenta saved = cuentaRepository.save(cuenta);
        //Then se valida
        assertEquals("Jose",saved.getPersona());
    }
    @Test
    void updateSomeCuenta() {
        CuentaMapper cuentaMapper = new CuentaMapperImpl();
        //Given una cuenta a guardar
        Cuenta cuenta = cuentaMapper.cuentaDtoToCuenta(
                CuentaDTO.builder().persona("Jose").saldo(new BigDecimal(5000)).build());
        //When se guarda
        Cuenta saved = cuentaRepository.save(cuenta);
        System.out.println(saved.getSaldo());
        //Then se valida
        assertEquals("Jose",saved.getPersona());
        saved.setSaldo(new BigDecimal("50.898"));
        Cuenta updated = cuentaRepository.save(saved);
        System.out.println(updated.getSaldo());
        assertEquals(new BigDecimal("50.898"),updated.getSaldo());
    }

    @Test
    void deleteTest() {
        //Given una cuenta encontrada
        Cuenta cuenta = cuentaRepository.findById(2L).orElseThrow();
        assertEquals("Juan",cuenta.getPersona());
        //When se borra
        cuentaRepository.delete(cuenta);
        //Then ya no hay cuenta
        assertFalse(cuentaRepository.findById(2L).isPresent());
        assertThrows(NoSuchElementException.class,() ->
                //si no se pone orElseThrow no arroja exception
                cuentaRepository.findByPersona("Juan").orElseThrow());
        assertEquals(2,cuentaRepository.count());
    }
}
