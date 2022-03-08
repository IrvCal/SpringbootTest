package com.irv.examplestest.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irv.examplestest.domain.Cuenta;
import com.irv.examplestest.web.model.CuentaDTO;
import com.irv.examplestest.web.model.TransaccionDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * esta clase se genera para hacer pruebas
 * para consumir Api REST
 * Aqui ya no es necesario utilizar un
 * object Mapper para pasar el obj a json
 * solito lo hace
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)//->define el orden de ejecucion de los test habilita la anotacion @Order(1)
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
    @Order(1)
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
                //Then
                .expectStatus().isOk()
                .expectBody().jsonPath("$.Mensaje").isNotEmpty()
                .jsonPath("$.Mensaje").value(is("Transferencia con exito"))// esta forma se puede hacer con lambda tambien // pero crei ques es mejor con los matchers
                .jsonPath("$.Mensaje").value(valor -> assertEquals("Transferencia con exito",valor))
                .jsonPath("$.Transaccion.idCuentaOrigen").value(is(1))
        ;
    }
    //este es igual que el de arriba pero ahora se compara el json completo que devolvio la peticion
    @Test
    @Order(2)
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
                //Then
                .expectStatus().isOk()
                .expectBody().jsonPath("$.Mensaje").isNotEmpty()
                .jsonPath("$.Mensaje").value(is("Transferencia con exito"))// esta forma se puede hacer con lambda tambien // pero crei ques es mejor con los matchers
                .jsonPath("$.Mensaje").value(valor -> assertEquals("Transferencia con exito",valor))
                .jsonPath("$.Transaccion.idCuentaOrigen").value(is(1))
                .json(objectMapper.writeValueAsString(response))
        ;
    }
    /*
    hasta este punto si nos fijamos en la uri para el webclient vemos que se tiene el puerto
    en donde se levanta el aplicativo, lo que hace que debamos tener corriendo el proyecto para
    que se puedan hacer las pruebas de integracion y se manipulan los datos en tiempo real
     */

    /**
     * Si no esta, como en este caso el puerto del servicio donde se esta levantando el aplicativo
     * se puede correr el test sin ningun problema, pero hay que tomar en cuenta que se ejecutan
     * en el mismo puerto, tanto el test como la app
     * @throws JsonProcessingException
     */
    @Order(4)
    @Test
    void transferirTestSinPort() throws JsonProcessingException {
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


        //when se ejecuta post
        webTestClient.post().uri("/api-cuenta/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(transaccionDto)
                .exchange()
                //Then
                .expectStatus().isOk()
                .expectBody()
                //por defecto el expectBody es bytes[] pero se puede poner String.class como argumento y ya se puede poner entityExchangeResult.getResponseBody() como string
                //se puede sustituir lo de .jsonPath con lo siguiente
                .consumeWith(entityExchangeResult -> {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(entityExchangeResult.getResponseBody());
                        // y se usan los assert
                        assertEquals("Transferencia con exito",jsonNode.path("Mensaje").asText());//funciona bien sin el asText pero el del curso lo pone
                        assertEquals("Transferencia con exito",jsonNode.path("Mensaje").asText());
                        //aqui ya empieza a cambiar por ejemplo si se quiere acceder a un atributo dentro de otro
                        assertEquals(1L,jsonNode.path("Transaccion").path("idCuentaOrigen").asLong());
                        assertEquals(LocalDate.now().toString(),jsonNode.path("Date").asText());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
    /**
     * Ahora se va a hacer un test parecido al de arriba pero obteniendo 
     * el BODY que se le aplica como un cast si se llama a una clase con
     * exactamente los mismos atributos que el Json (camparar este y testDetalle2)
     */
    @Test
    @Order(3)
    void testDetalle() {
        webTestClient.get().uri("/api-cuenta/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.persona").isEqualTo("Irving")
        ;
    }
    //la difeencia es que en el Consumidor (en la lambda) la cuentaDTOEntityExchangeResult
    // es automaticamente del tipo CuentaDTO
    @Test
    @Order(5)
    void testDetalle2() {
        webTestClient.get().uri("/api-cuenta/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CuentaDTO.class)
                .consumeWith(cuentaDTOEntityExchangeResult -> {
                    assertEquals("Irving",cuentaDTOEntityExchangeResult.getResponseBody().getPersona());
                    assertEquals(1,cuentaDTOEntityExchangeResult.getResponseBody().getId());
                })
        ;
    }

    @Test
    void listarTodos() {
        webTestClient.get().uri("/api-cuenta/")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].persona").isEqualTo("Irving")
                .jsonPath("$[0].id").isEqualTo(1)
                .jsonPath("$[1].id").isEqualTo(2)
                .jsonPath("$[1].persona").isEqualTo("Juan")
                .jsonPath("$").isArray()
                .jsonPath("$").value(hasSize(3))
        ;
    }
    @Test
    void listarTodos2() {
        webTestClient.get().uri("/api-cuenta/")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cuenta.class)//este es como el de transferirTestSinPort pero se espera una lista
                .consumeWith(listEntityExchangeResult -> {
                    assertNotNull(listEntityExchangeResult.getResponseBody());
                    assertEquals(3,listEntityExchangeResult.getResponseBody().stream().count());
                }).value(hasSize(3))
        ;
    }

    @Test
    void guardarTest() {
        webTestClient.post().uri("/api-cuenta/")
                .contentType(MediaType.APPLICATION_JSON)
                //Gvien una cuenta
                .bodyValue(CuentaDTO.builder().persona("IRving 2").saldo(new BigDecimal("345678")).build())
                //When se manda
                .exchange()
                //Then se espera
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CuentaDTO.class)
                .consumeWith(cuentaDTOEntityExchangeResult -> {
                    assertNotNull(cuentaDTOEntityExchangeResult);
                    assertEquals("IRving 2",cuentaDTOEntityExchangeResult.getResponseBody().getPersona());
                });
    }
    @Test
    void deleteTest() {

        webTestClient.get().uri("http://localhost:8080/api-cuenta/")
                .exchange()
                .expectBodyList(Cuenta.class)
                .value(hasSize(3))
        ;

        webTestClient.delete().uri("http://localhost:8080/api-cuenta/1")
                //Gvien una cuenta
                .exchange()
                .expectStatus()
                .isNoContent()
                .expectBody().isEmpty();

        webTestClient.get().uri("http://localhost:8080/api-cuenta/")
                .exchange()
                .expectBodyList(Cuenta.class)
                .value(hasSize(2))
        ;
        webTestClient.get().uri("http://localhost:8080/api-cuenta/1")
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody().isEmpty()
        ;
    }
}