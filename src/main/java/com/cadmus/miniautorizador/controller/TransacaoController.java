package com.cadmus.miniautorizador.controller;

import com.cadmus.miniautorizador.dto.TransacaoDTO;
import com.cadmus.miniautorizador.enums.TransacaoEnum;
import com.cadmus.miniautorizador.exception.CartaoInexistenteException;
import com.cadmus.miniautorizador.exception.SaldoInsuficienteException;
import com.cadmus.miniautorizador.exception.SenhaInvalidaException;
import com.cadmus.miniautorizador.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;
    @Autowired
    public TransacaoController(TransacaoService transacaoService){
        this.transacaoService = transacaoService;
    }

    @PostMapping
    public ResponseEntity<TransacaoEnum> efetuarTransacao(@RequestBody TransacaoDTO transacaoDTO){
        try {
            transacaoService.efetuarTransacao(transacaoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(TransacaoEnum.OK);
        } catch (SenhaInvalidaException ex) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(TransacaoEnum.SENHA_INVALIDA);
        } catch (SaldoInsuficienteException ex) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(TransacaoEnum.SALDO_INSUFICIENTE);
        } catch (CartaoInexistenteException ex) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(TransacaoEnum.CARTAO_INEXISTENTE);
        }
    }
}
