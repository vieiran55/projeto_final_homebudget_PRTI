package br.com.homebudget.relatorios.dto;

import java.math.BigDecimal;

public record DistribuicaoInvestimentoDTO(
        String tipo,
        BigDecimal total
) {}