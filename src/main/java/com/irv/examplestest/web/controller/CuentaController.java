package com.irv.examplestest.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CuentaController {
    @GetMapping("/")
    String saluda(){
        return "Hello world";
    }
}
