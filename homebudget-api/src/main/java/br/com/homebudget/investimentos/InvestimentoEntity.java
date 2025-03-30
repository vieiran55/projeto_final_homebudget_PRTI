package br.com.homebudget.investimentos;

import br.com.homebudget.investimentos.enums.TipoInvestimentoEnum;
import br.com.homebudget.investimentos.enums.TipoInvestimentoEnumConverter;
import br.com.homebudget.shared.entity.TransacaoEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "investimentos")
@Getter
@Setter
@NoArgsConstructor
public class InvestimentoEntity extends TransacaoEntity {

    @Convert(converter = TipoInvestimentoEnumConverter.class)
    @Column(name = "tipo", length = 255, nullable = false)
    private TipoInvestimentoEnum tipo;

    @Column(name = "valor_inicial", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorInicial;

    @Column(name = "valor_atual", precision = 10, scale = 2, nullable = false)
    private BigDecimal valorAtual;
}
