package com.cadmus.miniautorizador.service;

import com.cadmus.miniautorizador.dto.TransacaoDTO;
import com.cadmus.miniautorizador.enums.TransacaoEnum;
import com.cadmus.miniautorizador.exception.CartaoInexistenteException;
import com.cadmus.miniautorizador.exception.SaldoInsuficienteException;
import com.cadmus.miniautorizador.exception.SenhaInvalidaException;
import com.cadmus.miniautorizador.model.Cartao;
import com.cadmus.miniautorizador.repository.CartaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {

    @Mock
    private CartaoRepository cartaoRepository;

    @InjectMocks
    private TransacaoService transacaoService;

    @Mock
    private CartaoService cartaoService;

    @Test
    public void testEfetuarTransacao() {
        String numeroCartao = "1234567890";
        String senha = "senha";
        TransacaoDTO transacaoDTO = new TransacaoDTO(numeroCartao,senha, BigDecimal.TEN);
        Cartao cartaoSalvo = new Cartao(numeroCartao, senha, BigDecimal.TEN);

        when(cartaoRepository.findById(numeroCartao)).thenReturn(Optional.of(cartaoSalvo));
        transacaoService.efetuarTransacao(transacaoDTO);

        verify(cartaoRepository, times(1)).findById(numeroCartao);
    }

    @Test
    public void testEfetuarTransacaoCartaoInexistente() {
        String numeroCartao = "1234567890";
        String senha = "senha";
        TransacaoDTO transacaoDTO = new TransacaoDTO(numeroCartao,senha, BigDecimal.TEN);
        when(cartaoRepository.findById(numeroCartao)).thenReturn(Optional.empty());

        CartaoInexistenteException exception = assertThrows(CartaoInexistenteException.class,
                () -> transacaoService.efetuarTransacao(transacaoDTO));

        assertEquals(TransacaoEnum.CARTAO_INEXISTENTE.toString(), exception.getMessage());

        verify(cartaoRepository, times(1)).findById(numeroCartao);
    }

    @Test
    public void testEfetuarTransacaoSaldo() {
        String numeroCartao = "1234567890";
        String senha = "senha";

        Cartao cartaoSalvo = new Cartao(numeroCartao, senha, BigDecimal.ZERO);
        TransacaoDTO transacaoDTO = new TransacaoDTO(numeroCartao,senha, BigDecimal.TEN);
        when(cartaoRepository.findById(numeroCartao)).thenReturn(Optional.of(cartaoSalvo));

        SaldoInsuficienteException exception = assertThrows(SaldoInsuficienteException.class,
                () -> transacaoService.efetuarTransacao(transacaoDTO));

        assertEquals(TransacaoEnum.SALDO_INSUFICIENTE.toString(), exception.getMessage());

        verify(cartaoRepository, times(1)).findById(numeroCartao);
    }

    @Test
    public void testEfetuarTransacaoSenha() {
        String numeroCartao = "1234567890";
        String senha = "senha";
        String senhaErrada = "senhaErrada";

        Cartao cartaoSalvo = new Cartao(numeroCartao, senha, BigDecimal.TEN);
        TransacaoDTO transacaoDTO = new TransacaoDTO(numeroCartao,senhaErrada, BigDecimal.TEN);
        when(cartaoRepository.findById(numeroCartao)).thenReturn(Optional.of(cartaoSalvo));

        SenhaInvalidaException exception = assertThrows(SenhaInvalidaException.class,
                () -> transacaoService.efetuarTransacao(transacaoDTO));

        assertEquals(TransacaoEnum.SENHA_INVALIDA.toString(), exception.getMessage());

        verify(cartaoRepository, times(1)).findById(numeroCartao);
    }


}
