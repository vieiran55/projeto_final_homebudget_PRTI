package br.com.homebudget.despesas;

import br.com.homebudget.despesas.enums.CategoriaDespesaEnum;
import br.com.homebudget.despesas.enums.CategoriaDespesaEnumConverter;
import br.com.homebudget.shared.entity.TransacaoEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "despesas")
@Getter
@Setter
@NoArgsConstructor
public class DespesaEntity extends TransacaoEntity {

    @Convert(converter = CategoriaDespesaEnumConverter.class)
    @Column(name = "categoria", length = 255, nullable = false)
    private CategoriaDespesaEnum categoria;

    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    protected BigDecimal valor;
}
