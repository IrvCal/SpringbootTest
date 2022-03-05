package com.irv.examplestest.web.controller;

import com.irv.examplestest.domain.Cuenta;
import com.irv.examplestest.services.CuentaService;
import com.irv.examplestest.web.model.TransaccionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api-cuenta/")
public class CuentaController {
    @Autowired
    CuentaService service;
    @GetMapping("")
    String saluda(){
        return "Hello world";
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    Cuenta getCuenta(@PathVariable Long id){
        return service.findByIdCuenta(id);
    }
    @PostMapping("/transferir")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity setTransaccion(@RequestBody TransaccionDto transaccionDto){
        service.transferir(transaccionDto.getIdCuentaOrigen(),transaccionDto.getIdCuentaDestino(),transaccionDto.getMonto(),transaccionDto.getIdBanco());
        Map<String,Object> response = new HashMap<>();
        response.put("Date", LocalDate.now().toString());
        response.put("Status", "OK");
        response.put("Mensaje", "Transferencia con exito");
        response.put("Transaccion",transaccionDto);
        return ResponseEntity.ok(response);
    }



}
