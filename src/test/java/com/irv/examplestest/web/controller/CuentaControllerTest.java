package com.irv.examplestest.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.irv.examplestest.Data;
import com.irv.examplestest.domain.Cuenta;
import com.irv.examplestest.services.CuentaService;
import com.irv.examplestest.web.mappers.CuentaMapper;
import com.irv.examplestest.web.mappers.CuentaMapperImpl;
import com.irv.examplestest.web.model.TransaccionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaController.class)//se tiene que configurar el contexto MVC TEST
class CuentaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CuentaService cuentaService;
//    @Autowired //No jala el contexto
//    private CuentaMapper cuentaMapper;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void getCuenta() throws Exception {
        CuentaMapper cuentaMapper = new CuentaMapperImpl();
        //Given
        when(cuentaService.findByIdCuenta(1L)).thenReturn(
                Optional.ofNullable(cuentaMapper.cuentaDtoToCuenta(Data.CUENTA_DTO_1)));
        //Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api-cuenta/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                //el $ hace referencia a la raiz del JSON
                .andExpect(jsonPath("$.persona").value("Irving"));
    }

    @Test
    void transferir() throws Exception {
        TransaccionDto transaccion = TransaccionDto.builder()
                .idBanco(1L)
                .idCuentaOrigen(1L)
                .idCuentaDestino(2L)
                .build();

        mockMvc.perform(
                    MockMvcRequestBuilders.post("/api-cuenta/transferir")
                    //verifica que lo que se envia en el cuerpo es de tipo Json
                    .contentType(MediaType.APPLICATION_JSON)
                    //Aqui se tiene que mandar un string pero lo que nosotros queremos mandar
                    //es el DTO entonces se pasa a un ObjectMapper para mandarlo en JSON STRIng
                    .content(objectMapper.writeValueAsString(transaccion))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Transaccion.idCuentaOrigen").value(1L));
    }

    /**
     * Este es como el de arriba pero ahora se verificara como se esta devolviendo la respuesta
     * del map
     */
    @Test
    void transf() throws Exception {
        //Given
        TransaccionDto transaccion = TransaccionDto.builder()
                .idBanco(1L)
                .idCuentaOrigen(1L)
                .idCuentaDestino(2L)
                .build();

        //When
        Map<String,Object> response = new HashMap<>();response.put("Date", LocalDate.now().toString());
        response.put("Status", "OK");response.put("Mensaje", "Transferencia con exito");
        response.put("Transaccion",transaccion);
        //Then
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api-cuenta/transferir")
                        .contentType(MediaType.APPLICATION_JSON)//este tiene que estar para indicar que
                        //manda como json en el cuerpo
                        .content(objectMapper.writeValueAsString(transaccion))
                )
                .andExpect(jsonPath("$.Transaccion.idCuentaOrigen").value(1L))
                //Ahora validamos aqui la respuesta verdadera con la esperada
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void testAllCuentas() throws Exception {
        CuentaMapper cuentaMapper = new CuentaMapperImpl();
        //Given una lista
        when(cuentaService.findAll()).thenReturn(
                cuentaMapper.cuentasDtoToCuentas(Data.CUENTA_DTOS));
        //When se consume (llama la lista)
        mockMvc.perform(
                    MockMvcRequestBuilders.get("/api-cuenta/")
                        .contentType(MediaType.APPLICATION_JSON)
                )//Then se espera esto
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].persona").value("Irving"))
                .andExpect(jsonPath("$[1].persona").value("Juan"))
                .andExpect(jsonPath("$[1].saldo").value("2000"))
                .andExpect(jsonPath("$",hasSize(3)))
        ;
        //hasta este punto se tenia que en el controllador arrojaba un null porque no se
        //habia colocado algun comportamiento, pero si se tiene qu agregar return service.findAll();
        //porque si no sale java.lang.AssertionError: Content type not set
    }
//----------------------Checar el content type el test no me salio pero
    //si funciona cunado esta arriba
    @Test
    void testSave() throws Exception {
        //Given
        CuentaMapper cuentaMapper = new CuentaMapperImpl();
        Cuenta cuenta = cuentaMapper.cuentaDtoToCuenta(Data.CUENTA_DTO_1);
        cuenta.setId(null);
        when(cuentaService.save(cuenta)).thenReturn(cuenta);//se tiene que modificar este then
        //porque tambien se va a probar que id se mande nulo y en db se llene
        //este comportamiento ya se vio en Junit pero se hace asi
//        when(cuentaService.save(cuenta)).then(invocation -> {
//            Cuenta cuenta1 = invocation.getArgument(0);
//            cuenta1.setId(3L);
//            return cuenta1;
//        });
        //When se consume
        mockMvc.perform(
                MockMvcRequestBuilders.post("/api-cuenta/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cuenta))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.persona",is("Irving")))
        ;
    }
}