package br.com.homebudget.relatorios.dto;

import java.math.BigDecimal;

public record SaldoMensalDTO(String mes,
                             BigDecimal saldo
) {
}
