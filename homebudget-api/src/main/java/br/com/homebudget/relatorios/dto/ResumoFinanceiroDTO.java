package br.com.homebudget.relatorios.dto;

import java.math.BigDecimal;

public record ResumoFinanceiroDTO(
        int ano,
        int mes,
        BigDecimal totalReceitas,
        BigDecimal totalDespesas,
        BigDecimal totalInvestimentos
) {}