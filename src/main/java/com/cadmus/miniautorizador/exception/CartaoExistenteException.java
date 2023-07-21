package com.cadmus.miniautorizador.exception;

public class CartaoExistenteException extends RuntimeException{
    public CartaoExistenteException(String message) {
        super(message);
    }
}
