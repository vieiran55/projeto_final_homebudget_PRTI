package br.com.homebudget.relatorios.dto;

import java.math.BigDecimal;

public record DistribuicaoDespesasDTO(
        String categoria,
        BigDecimal total
) {}
