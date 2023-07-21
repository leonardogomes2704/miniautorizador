package com.cadmus.miniautorizador.exception;

public class CartaoInexistenteException extends RuntimeException{
    public CartaoInexistenteException(String message) {
        super(message);
    }
}
