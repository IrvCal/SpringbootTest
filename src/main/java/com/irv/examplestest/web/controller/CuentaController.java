package com.irv.examplestest.web.controller;

import static org.springframework.http.HttpStatus.*;

import com.irv.examplestest.domain.Cuenta;
import com.irv.examplestest.services.CuentaService;
import com.irv.examplestest.web.model.TransaccionDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api-cuenta/")
public class CuentaController {
    @Autowired
    CuentaService service;

    @GetMapping("/saludo")
    String saluda(){
        return "Hello world";
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    Cuenta getCuenta(@PathVariable Long id){
        return service.findByIdCuenta(id);
    }

    @PostMapping("/transferir")
    @ResponseStatus(OK)
    ResponseEntity setTransaccion(@RequestBody TransaccionDto transaccionDto){
        service.transferir(transaccionDto.getIdCuentaOrigen(),transaccionDto.getIdCuentaDestino(),transaccionDto.getMonto(),transaccionDto.getIdBanco());
        Map<String,Object> response = new HashMap<>();
        response.put("Date", LocalDate.now().toString());
        response.put("Status", "OK");
        response.put("Mensaje", "Transferencia con exito");
        response.put("Transaccion",transaccionDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    @ResponseStatus(OK)
    List<Cuenta> getAllCuentas(){
        return service.findAll();
    }

    @PostMapping("")
    @ResponseStatus(CREATED)
    Cuenta save(@RequestBody Cuenta cuenta){
        return service.save(cuenta);
    }

}
