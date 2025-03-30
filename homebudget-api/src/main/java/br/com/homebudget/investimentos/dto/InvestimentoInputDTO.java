package br.com.homebudget.investimentos.dto;

import br.com.homebudget.investimentos.enums.TipoInvestimentoEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InvestimentoInputDTO(
        @NotNull Long userId,
        @NotNull TipoInvestimentoEnum tipo,
        @NotNull @DecimalMin("0.01") BigDecimal valorInicial,
        @NotNull @DecimalMin("0.01") BigDecimal valorAtual,
        @NotBlank String descricao,
        @NotNull LocalDate data
) {
}
