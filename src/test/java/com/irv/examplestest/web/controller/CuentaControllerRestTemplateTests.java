package com.irv.examplestest.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irv.examplestest.domain.Cuenta;
import com.irv.examplestest.web.model.TransaccionDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CuentaControllerRestTemplateTests {

    @Autowired
    private TestRestTemplate client;
    @Autowired
    private ObjectMapper objectMapper;
    private String TRANSFERENCIA_EXITO="Transferencia con exito";

    @LocalServerPort//obtiene el puerto donde se esta ejecutando el test
    private int puerto;
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    void transferir() throws JsonProcessingException {
        TransaccionDto transaccionDto = TransaccionDto.builder()
                .monto(new BigDecimal("100"))
                .idBanco(1L)
                .idCuentaOrigen(1L)
                .idCuentaDestino(2L)
                .build();
        ResponseEntity<String> responseEntity =
        client.postForEntity(getRoute("transferir"),transaccionDto,String.class);//se pude dejar la ruta relativa (sin el puerto)

        String json = responseEntity.getBody();
        assertNotNull(json);
        System.out.println(json);
        assertEquals(MediaType.APPLICATION_JSON,responseEntity.getHeaders().getContentType());
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertTrue(json.contains(TRANSFERENCIA_EXITO));

        //se utiliza JsonNode para poder utilizar el path y poder llamar tambien isText y asi
        JsonNode jsonNode = objectMapper.readTree(json);
        assertEquals(TRANSFERENCIA_EXITO,jsonNode.path("Mensaje").asText());
        assertEquals(LocalDate.now().toString(),jsonNode.path("Date").asText());
        assertEquals(1L,jsonNode.path("Transaccion").path("idCuentaOrigen").asLong());

        Map<String,Object> response = new HashMap<>();
        response.put("Date", LocalDate.now().toString());
        response.put("Status", "OK");
        response.put("Mensaje", "Transferencia con exito");
        response.put("Transaccion",transaccionDto);

        assertEquals(objectMapper.writeValueAsString(response),json);

    }

    @Test
    @Order(2)
    void getCuenta() {
        ResponseEntity<Cuenta> responseEntity =
        client.getForEntity(getRoute("1"), Cuenta.class);
        Cuenta cuenta = responseEntity.getBody();
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON,responseEntity.getHeaders().getContentType());
        assertNotNull(cuenta);
        assertEquals(1L,cuenta.getId());

    }

    @Test
    @Order(3)
    void listar() throws JsonProcessingException {
        ResponseEntity<Cuenta[]> cuentasArray =
        client.getForEntity(getRoute(""), Cuenta[].class);
        List<Cuenta> cuentas = Arrays.asList(cuentasArray.getBody());
        assertEquals(cuentasArray.getBody().length,cuentas.stream().filter(this::isIdNotNull).count());
        assertEquals(cuentasArray.getBody().length,cuentas.stream().filter(this::isSaldoPositive).count());
        //aqui tambien el del curso hace un JsonNode, esto es para que se utilize
        //la navegacion por json.path("").asText
        JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(cuentas));
        assertEquals(1L,jsonNode.get(0).path("id").asLong());
    }

    @Test
    @Order(4)
    void saveCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setSaldo(new BigDecimal("200"));
        cuenta.setPersona("Irving 2");
        ResponseEntity<Cuenta> responseEntity =
        client.postForEntity(getRoute(""),cuenta,Cuenta.class);
        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
        assertEquals("Irving 2",responseEntity.getBody().getPersona());
        assertEquals(4,responseEntity.getBody().getId());
    }

    @Test
    @Order(5)
    void deleteCuenta() {
        ResponseEntity<Cuenta[]> cuentasArray = client.getForEntity(getRoute(""), Cuenta[].class);
        assertEquals(4,cuentasArray.getBody().length);

        client.delete(getRoute("4"));

        ResponseEntity<Cuenta[]> cuentasArray2 = client.getForEntity(getRoute(""), Cuenta[].class);
        assertEquals(3,cuentasArray2.getBody().length);

    }

    private boolean isSaldoPositive(Cuenta cuenta) {
        if(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0)
            return true;
    return false;
    }

    public Boolean isIdNotNull(Cuenta cuenta){
        if(cuenta.getId()!=null)
            return true;
        return false;
    }

    String getRoute(String uri){
        return "http://localhost:"+puerto+ "/api-cuenta/"+uri;
    }
}
