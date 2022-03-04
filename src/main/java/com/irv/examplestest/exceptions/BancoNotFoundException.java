package com.irv.examplestest.exceptions;

public class BancoNotFoundException extends RuntimeException{
    public BancoNotFoundException(String message) {
        super(message);
    }
}
