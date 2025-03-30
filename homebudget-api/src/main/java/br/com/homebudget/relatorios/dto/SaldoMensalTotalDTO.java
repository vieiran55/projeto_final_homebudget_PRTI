package br.com.homebudget.relatorios.dto;

import java.math.BigDecimal;

public record SaldoMensalTotalDTO(String anoMes,
                                  BigDecimal saldoMensal,
                                  BigDecimal saldoTotal,
                                  BigDecimal aporteMensal,
                                  BigDecimal totalInvestimentos
) {
}
