package br.com.homebudget.relatorios.dto;

import java.math.BigDecimal;

public record DistribuicaoReceitasDTO(
        String categoria,
        BigDecimal total
) {}