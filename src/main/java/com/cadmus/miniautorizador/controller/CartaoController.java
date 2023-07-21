package com.cadmus.miniautorizador.controller;

import com.cadmus.miniautorizador.dto.CartaoDTO;
import com.cadmus.miniautorizador.exception.CartaoExistenteException;
import com.cadmus.miniautorizador.exception.CartaoNaoEncontradoException;
import com.cadmus.miniautorizador.service.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

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

    @GetMapping("/{numeroCartao}")
    public ResponseEntity<BigDecimal> obter(@PathVariable String numeroCartao){
        try {
            BigDecimal saldo = cartaoService.obter(numeroCartao);
            return ResponseEntity.status(HttpStatus.OK).body(saldo);
        } catch (CartaoNaoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
