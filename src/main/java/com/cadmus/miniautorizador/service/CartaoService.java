package com.cadmus.miniautorizador.service;

import com.cadmus.miniautorizador.dto.CartaoDTO;
import com.cadmus.miniautorizador.exception.CartaoExistenteException;
import com.cadmus.miniautorizador.model.Cartao;
import com.cadmus.miniautorizador.repository.CartaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartaoService {
    private final BigDecimal VALOR_INICIAL = BigDecimal.valueOf(500);

    @Autowired
    private CartaoRepository cartaoRepository;

    public CartaoDTO criar(CartaoDTO cartaoDTO) {

        if(existeCartao(cartaoDTO.getNumeroCartao())){
            throw new CartaoExistenteException("Já existe um cartão com esse número");
        }

        Cartao cartao = new Cartao(cartaoDTO.getNumeroCartao(), cartaoDTO.getSenha(), VALOR_INICIAL);
        cartao = cartaoRepository.save(cartao);

        return new CartaoDTO(cartao.getSenha(), cartao.getNumeroCartao());
    }

    private boolean existeCartao(String numeroCartao) {
        Optional<Cartao> cartaoExistente = cartaoRepository.findById(numeroCartao);
        return cartaoExistente.isPresent();
    }

}
