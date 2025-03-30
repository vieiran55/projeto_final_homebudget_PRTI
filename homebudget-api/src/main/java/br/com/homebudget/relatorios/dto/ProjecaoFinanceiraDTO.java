package br.com.homebudget.relatorios.dto;

import java.math.BigDecimal;

public record ProjecaoFinanceiraDTO(int ano,
                                    int mes,
                                    BigDecimal saldoProjetado) {
}
