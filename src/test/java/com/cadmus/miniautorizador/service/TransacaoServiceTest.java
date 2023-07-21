package com.cadmus.miniautorizador.service;

import com.cadmus.miniautorizador.dto.TransacaoDTO;
import com.cadmus.miniautorizador.exception.CartaoExistenteException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TransacaoServiceTest {

    @Autowired
    private TransacaoService transacaoService;

    @Test
    public void testEfetuarTransacaoThread1e2() throws InterruptedException {
            Thread thread1 = new Thread(() -> {
                transacaoService.efetuarTransacao(new TransacaoDTO("6549873025634501","1234", BigDecimal.TEN));
            });
            Thread thread2 = new Thread(() -> {
                transacaoService.efetuarTransacao(new TransacaoDTO("6549873025634501","1234", BigDecimal.TEN));
            });

            thread1.start();
            thread2.start();

            thread1.join();
            thread2.join();
    }
    @Test
    public void testEfetuarTransacaoThread1() throws InterruptedException {

        Thread thread1 = new Thread(() -> {
            transacaoService.efetuarTransacao(new TransacaoDTO("6549873025634501","1234", BigDecimal.TEN));
        });


        thread1.start();


        thread1.join();

    }

    @Test
    public void testEfetuarTransacaoThread2() throws InterruptedException {

        Thread thread2 = new Thread(() -> {
            transacaoService.efetuarTransacao(new TransacaoDTO("6549873025634501","1234", BigDecimal.TEN));
        });

        thread2.start();

        thread2.join();
    }


}
