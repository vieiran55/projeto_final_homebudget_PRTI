package br.com.homebudget.receitas.dto;

import br.com.homebudget.receitas.enums.FonteReceitaEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReceitaInputDTO(
        Long userId,
        @NotNull FonteReceitaEnum fonte,
        @NotNull @DecimalMin("0.01") BigDecimal valor,
        @NotBlank String descricao,
        @NotNull LocalDate data
) {
}
