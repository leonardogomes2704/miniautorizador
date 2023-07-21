package com.cadmus.miniautorizador.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TransacaoDTO {

    private String numeroCartao;
    private String senhaCartao;
    private BigDecimal valor;
}
