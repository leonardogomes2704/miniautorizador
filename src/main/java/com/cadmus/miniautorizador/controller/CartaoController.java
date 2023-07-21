package com.cadmus.miniautorizador.controller;

import com.cadmus.miniautorizador.dto.CartaoDTO;
import com.cadmus.miniautorizador.exception.CartaoExistenteException;
import com.cadmus.miniautorizador.service.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cartoes")
public class CartaoController {

    @Autowired
    private CartaoService cartaoService;

    @PostMapping
    public ResponseEntity<CartaoDTO> criar(@RequestBody CartaoDTO cartaoDTO){
        try {
            CartaoDTO novoCartao = cartaoService.criar(cartaoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoCartao);
        } catch (CartaoExistenteException ex) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(cartaoDTO);
        }
    }
}
