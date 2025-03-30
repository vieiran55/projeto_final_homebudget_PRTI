package br.com.homebudget.despesas.dto;

import br.com.homebudget.despesas.enums.CategoriaDespesaEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DespesaInputDTO(
        Long userId,
        @NotNull CategoriaDespesaEnum categoria,
        @NotNull @DecimalMin("0.01") BigDecimal valor,
        @NotBlank String descricao,
        @NotNull LocalDate data
) {
}
