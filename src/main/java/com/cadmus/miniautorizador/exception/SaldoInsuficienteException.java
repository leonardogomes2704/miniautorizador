package com.cadmus.miniautorizador.exception;

public class SaldoInsuficienteException extends RuntimeException{
    public SaldoInsuficienteException(String message) {
        super(message);
    }
}
