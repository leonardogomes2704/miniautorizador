package com.cadmus.miniautorizador.service;

import com.cadmus.miniautorizador.dto.TransacaoDTO;
import com.cadmus.miniautorizador.enums.TransacaoEnum;
import com.cadmus.miniautorizador.exception.CartaoInexistenteException;
import com.cadmus.miniautorizador.exception.SaldoInsuficienteException;
import com.cadmus.miniautorizador.exception.SenhaInvalidaException;
import com.cadmus.miniautorizador.model.Cartao;
import com.cadmus.miniautorizador.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class TransacaoService {
    private final CartaoRepository cartaoRepository;
    private final CartaoService cartaoService;

    @Autowired
    public TransacaoService(CartaoRepository cartaoRepository, CartaoService cartaoService) {
        this.cartaoRepository = cartaoRepository;
        this.cartaoService = cartaoService;
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void efetuarTransacao(TransacaoDTO transacaoDTO) {
        validacaoTransacao(transacaoDTO);
        cartaoService.debitarSaldoCartao(transacaoDTO);
    }

    private void validacaoTransacao(TransacaoDTO transacaoDTO) {
        Optional<Cartao> cartaoOptional =  cartaoRepository.findById(transacaoDTO.getNumeroCartao());

        cartaoOptional.orElseThrow(() -> new CartaoInexistenteException(TransacaoEnum.CARTAO_INEXISTENTE.toString()));

        cartaoOptional.ifPresent(c -> {
            validarSaldo(transacaoDTO.getValor(), c.getSaldo());
            validarSenha(transacaoDTO.getSenhaCartao(), c.getSenha());
        });
    }

    private static void validarSenha(String transacaoSenha, String cartaoSenha) {
        if(!cartaoSenha.equals(transacaoSenha)){
            throw new SenhaInvalidaException(TransacaoEnum.SENHA_INVALIDA.toString());
        }
    }

    private static void validarSaldo(BigDecimal valorTransacao, BigDecimal saldoCartao) {
        if(saldoCartao.compareTo(valorTransacao) < 0){
            throw new SaldoInsuficienteException(TransacaoEnum.SALDO_INSUFICIENTE.toString());
        }
    }
}
