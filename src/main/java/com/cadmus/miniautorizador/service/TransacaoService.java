package com.cadmus.miniautorizador.service;

import com.cadmus.miniautorizador.dto.TransacaoDTO;
import com.cadmus.miniautorizador.enums.TransacaoEnum;
import com.cadmus.miniautorizador.exception.CartaoInexistenteException;
import com.cadmus.miniautorizador.exception.SaldoInsuficienteException;
import com.cadmus.miniautorizador.exception.SenhaInvalidaException;
import com.cadmus.miniautorizador.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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
        cartaoRepository.findById(transacaoDTO.getNumeroCartao())
                .ifPresent(c -> {
                    if(c.getSaldo().compareTo(transacaoDTO.getValor()) < 0){
                        throw new SaldoInsuficienteException(TransacaoEnum.SALDO_INSUFICIENTE.toString());
                    }
                    if(!c.getNumeroCartao().equals(transacaoDTO.getNumeroCartao())){
                        throw new CartaoInexistenteException(TransacaoEnum.CARTAO_INEXISTENTE.toString());
                    }
                    if(!c.getSenha().equals(transacaoDTO.getSenhaCartao())){
                        throw new SenhaInvalidaException(TransacaoEnum.SENHA_INVALIDA.toString());
                    }
                });
    }
}
