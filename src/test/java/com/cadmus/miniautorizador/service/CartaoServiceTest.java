package com.cadmus.miniautorizador.service;

import com.cadmus.miniautorizador.dto.CartaoDTO;
import com.cadmus.miniautorizador.dto.TransacaoDTO;
import com.cadmus.miniautorizador.exception.CartaoExistenteException;
import com.cadmus.miniautorizador.exception.CartaoInexistenteException;
import com.cadmus.miniautorizador.exception.CartaoNaoEncontradoException;
import com.cadmus.miniautorizador.model.Cartao;
import com.cadmus.miniautorizador.repository.CartaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class CartaoServiceTest {

    @Mock
    private CartaoRepository cartaoRepository;

    @InjectMocks
    private CartaoService cartaoService;

    @Test
    public void testCriarCartao() {
        String numeroCartao = "1234567890";
        String senha = "senha";
        CartaoDTO cartaoDTO = new CartaoDTO(senha, numeroCartao);

        when(cartaoRepository.findById(numeroCartao)).thenReturn(Optional.empty());

        Cartao cartaoSalvo = new Cartao(numeroCartao, senha, BigDecimal.TEN);
        when(cartaoRepository.save(any(Cartao.class))).thenReturn(cartaoSalvo);

        CartaoDTO novoCartao = cartaoService.criar(cartaoDTO);

        assertNotNull(novoCartao);
        assertEquals(numeroCartao, novoCartao.getNumeroCartao());
        assertEquals(senha, novoCartao.getSenha());

        verify(cartaoRepository, times(1)).findById(numeroCartao);
        verify(cartaoRepository, times(1)).save(any(Cartao.class));
    }

    @Test
    public void testCriarCartaoJaExistente() {
        String numeroCartao = "1234567890";
        String senha = "senha";
        CartaoDTO cartaoDTO = new CartaoDTO(senha, numeroCartao);

        when(cartaoRepository.findById(numeroCartao)).thenReturn(Optional.of(new Cartao()));

        assertThrows(CartaoExistenteException.class, () -> cartaoService.criar(cartaoDTO));

        verify(cartaoRepository, times(1)).findById(numeroCartao);
        verify(cartaoRepository, times(0)).save(any(Cartao.class));
    }

    @Test
    public void testObter() {
        String numeroCartao = "1234567890";
        String senha = "senha";

        Cartao cartaoSalvo = new Cartao(numeroCartao, senha, BigDecimal.TEN);
        when(cartaoRepository.findById(numeroCartao)).thenReturn(Optional.of(cartaoSalvo));

        BigDecimal saldo = cartaoService.obter(numeroCartao);

        assertNotNull(saldo);
        assertEquals(BigDecimal.TEN, saldo);

        verify(cartaoRepository, times(1)).findById(numeroCartao);

    }

    @Test
    public void testObterCartaoNaoEncontrado() {
        String numeroCartao = "1234567890";

        when(cartaoRepository.findById(numeroCartao)).thenReturn(Optional.empty());
        assertThrows(CartaoNaoEncontradoException.class, () -> cartaoService.obter(numeroCartao));

        verify(cartaoRepository, times(1)).findById(numeroCartao);
    }

    @Test
    public void testDebitarSaldoCartao() {
        String numeroCartao = "1234567890";
        String senha = "senha";

        TransacaoDTO transacaoDTO = new TransacaoDTO(numeroCartao,senha, BigDecimal.TEN);
        Cartao cartaoSalvo = new Cartao(numeroCartao, senha, BigDecimal.TEN);
        Cartao cartaoSaldoNovo = new Cartao(numeroCartao, senha, BigDecimal.ZERO);

        when(cartaoRepository.findById(numeroCartao)).thenReturn(Optional.of(cartaoSalvo));
        when(cartaoRepository.save(cartaoSalvo)).thenReturn(cartaoSaldoNovo);
        cartaoService.debitarSaldoCartao(transacaoDTO);

        verify(cartaoRepository, times(1)).findById(numeroCartao);
        verify(cartaoRepository, times(1)).save(cartaoSalvo);
    }

    @Test
    public void testDebitarSaldoCartaoInexistente() {
        String numeroCartao = "1234567890";
        String senha = "senha";

        TransacaoDTO transacaoDTO = new TransacaoDTO(numeroCartao,senha, BigDecimal.TEN);
        Cartao cartaoSalvo = new Cartao(numeroCartao, senha, BigDecimal.TEN);

        when(cartaoRepository.findById(numeroCartao)).thenReturn(Optional.empty());

        assertThrows(CartaoInexistenteException.class, () -> cartaoService.debitarSaldoCartao(transacaoDTO));

        verify(cartaoRepository, times(1)).findById(numeroCartao);
        verify(cartaoRepository, times(0)).save(cartaoSalvo);
    }

}
