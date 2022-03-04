package com.irv.examplestest.exceptions;

import java.util.NoSuchElementException;

public class CuentaPersonaNotFoundException extends NoSuchElementException {
    public CuentaPersonaNotFoundException(String s) {
        super(s);
    }
}
