package br.com.homebudget.relatorios.dto;

import java.math.BigDecimal;

public record ComparacaoGastosDTO(String categoria,
                                  BigDecimal totalAtual,
                                  BigDecimal totalAnterior) {
}
