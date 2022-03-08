package com.irv.examplestest.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irv.examplestest.web.model.TransaccionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * esta clase se genera para hacer pruebas
 * para consumir Api REST
 * Aqui ya no es necesario utilizar un
 * object Mapper para pasar el obj a json
 * solito lo hace
 */
@SpringBootTest(webEnvironment = RANDOM_PORT) //se agrega de forma autoamtica con esta anotacion lo que trae WebTestAutoConfiguration
class CuentaControllerWebClientTest {

    private ObjectMapper objectMapper;
    @Autowired
    private WebTestClient webTestClient;//esta es la implementacion real para consumir servicios rest con caracteristicas con pruebas unitarias

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void transferirTest() {
        //Given una trasaccion
        TransaccionDto transaccionDto = TransaccionDto.builder()
                .idCuentaOrigen(1L)
                .idCuentaDestino(2L)
                .idBanco(1L)
                .monto(new BigDecimal("500"))
                .build();
        //when se ejecuta post (tiene que estar levantada la aplicacion y VERDADERAMENTE hace cambios a db)
        webTestClient.post().uri("http://localhost:8080/api-cuenta/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transaccionDto)//comparando con el otro test, aqui se manda directamente el obj no un mapper del json
                // (tambien esta body(Publisher<>) pero eso es para programacion reactiva con webFlux
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.Mensaje").isNotEmpty()
                .jsonPath("$.Mensaje").value(is("Transferencia con exito"))// esta forma se puede hacer con lambda tambien // pero crei ques es mejor con los matchers
                .jsonPath("$.Mensaje").value(valor -> assertEquals("Transferencia con exito",valor))
                .jsonPath("$.Transaccion.idCuentaOrigen").value(is(1))
        ;
    }
    //este es igual que el de arriba pero ahora se compara el json completo que devolvio la peticion
    @Test
    void transferirTestJSON() throws JsonProcessingException {
        //Given una trasaccion
        TransaccionDto transaccionDto = TransaccionDto.builder()
                .idCuentaOrigen(1L)
                .idCuentaDestino(2L)
                .idBanco(1L)
                .monto(new BigDecimal("500"))
                .build();

        Map<String,Object> response = new HashMap<>();
        response.put("Date", LocalDate.now().toString());
        response.put("Status","OK");
        response.put("Mensaje","Transferencia con exito");
        response.put("Transaccion",transaccionDto);


        //when se ejecuta post (tiene que estar levantada la aplicacion y VERDADERAMENTE hace cambios a db)
        webTestClient.post().uri("http://localhost:8080/api-cuenta/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transaccionDto)//comparando con el otro test, aqui se manda directamente el obj no un mapper del json
                // (tambien esta body(Publisher<>) pero eso es para programacion reactiva con webFlux
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.Mensaje").isNotEmpty()
                .jsonPath("$.Mensaje").value(is("Transferencia con exito"))// esta forma se puede hacer con lambda tambien // pero crei ques es mejor con los matchers
                .jsonPath("$.Mensaje").value(valor -> assertEquals("Transferencia con exito",valor))
                .jsonPath("$.Transaccion.idCuentaOrigen").value(is(1))
                .json(objectMapper.writeValueAsString(response))
        ;
    }
}