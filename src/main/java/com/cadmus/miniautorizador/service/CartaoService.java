package com.cadmus.miniautorizador.service;

import com.cadmus.miniautorizador.dto.CartaoDTO;
import com.cadmus.miniautorizador.exception.CartaoExistenteException;
import com.cadmus.miniautorizador.exception.CartaoNaoEncontradoException;
import com.cadmus.miniautorizador.model.Cartao;
import com.cadmus.miniautorizador.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

@Service
public class CartaoService {
    private final BigDecimal VALOR_INICIAL = BigDecimal.valueOf(500);
    private final CartaoRepository cartaoRepository;

    @Autowired
    public CartaoService(CartaoRepository cartaoRepository) {
        this.cartaoRepository = cartaoRepository;
    }

    public CartaoDTO criar(CartaoDTO cartaoDTO) {
        cartaoRepository.findById(cartaoDTO.getNumeroCartao())
                .ifPresent(c -> {
                    throw new CartaoExistenteException("Já existe um cartão com esse número");
                });

        Cartao cartao = cartaoRepository.save(new Cartao(cartaoDTO.getNumeroCartao(), cartaoDTO.getSenha(), VALOR_INICIAL));
        return new CartaoDTO(cartao.getSenha(), cartao.getNumeroCartao());
    }

    public BigDecimal obter(String numeroCartao){
        return cartaoRepository.findById(numeroCartao)
                .map(Cartao::getSaldo)
                .orElseThrow(() -> new CartaoNaoEncontradoException("Cartão não encontrado para o número: " + numeroCartao));
    }

}
