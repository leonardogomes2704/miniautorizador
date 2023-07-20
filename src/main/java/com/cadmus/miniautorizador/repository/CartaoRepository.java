package com.cadmus.miniautorizador.repository;

import com.cadmus.miniautorizador.model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {

}
