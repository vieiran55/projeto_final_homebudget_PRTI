package br.com.homebudget.despesas.dto;

import br.com.homebudget.despesas.enums.CategoriaDespesaEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DespesaDTO(
        Long id,
        Long userId,
        CategoriaDespesaEnum categoria,
        BigDecimal valor,
        String descricao,
        LocalDate data
) {
}
