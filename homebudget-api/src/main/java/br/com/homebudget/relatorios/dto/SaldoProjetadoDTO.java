package br.com.homebudget.relatorios.dto;

import java.math.BigDecimal;
import java.time.YearMonth;

public record SaldoProjetadoDTO(YearMonth periodo,
                                BigDecimal saldo) {
    public YearMonth getPeriodo() {
        return periodo;
    }
}
