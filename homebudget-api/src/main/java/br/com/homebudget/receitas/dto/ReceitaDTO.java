package br.com.homebudget.receitas.dto;

import br.com.homebudget.receitas.enums.FonteReceitaEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReceitaDTO(
        Long id,
        Long userId,
        FonteReceitaEnum fonte,
        BigDecimal valor,
        String descricao,
        LocalDate data
) {
}
