package br.com.homebudget.receitas;

import br.com.homebudget.receitas.enums.FonteReceitaEnum;
import br.com.homebudget.receitas.enums.FonteReceitaEnumConverter;
import br.com.homebudget.shared.entity.TransacaoEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "receitas")
@Getter
@Setter
@NoArgsConstructor
public class ReceitaEntity extends TransacaoEntity {

    @Convert(converter = FonteReceitaEnumConverter.class)
    @Column(name = "fonte", length = 255, nullable = false)
    private FonteReceitaEnum fonte;

    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    protected BigDecimal valor;
}
