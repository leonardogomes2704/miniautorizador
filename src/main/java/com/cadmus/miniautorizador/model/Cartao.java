package com.cadmus.miniautorizador.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cartao {

    @Id
    private String numeroCartao;

    private String senha;

    private BigDecimal saldo;

}
