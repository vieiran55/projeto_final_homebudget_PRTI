package br.com.homebudget.despesas.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum CategoriaDespesaEnum {
    ALIMENTACAO("Alimentação"),
    CARTAO("Cartão de Crédito"),
    TRANSPORTE("Transporte"),
    MORADIA("Moradia"),
    SAUDE("Saúde"),
    LAZER("Lazer"),
    EDUCACAO("Educação"),
    VESTUARIO("Vestuário"),
    TELEFONIA("Telefonia"),
    INTERNET("Internet"),
    ENERGIA("Energia"),
    AGUA("Água"),
    GAS("Gás"),
    MANUTENCAO_CASA("Manutenção da Casa"),
    SEGUROS("Seguros"),
    IMPOSTOS("Impostos"),
    PETS("Pets"),
    CUIDADOS_PESSOAIS("Cuidados Pessoais"),
    VIAGENS("Viagens"),
    CARRO("Carro"),
    TRANSPORTE_PUBLICO("Transporte Público"),
    PRESENTES("Presentes"),
    DOACOES("Doações"),
    SUPERMERCADO("Supermercado"),
    RESTAURANTES("Restaurantes"),
    HOBBIES("Hobbies"),
    ASSINATURAS("Assinaturas"),
    FINANCIAMENTOS("Financiamentos"),
    CONSORCIO("Consórcio"),
    APOSTAS("Apostas"),
    JOGOS("Jogos"),
    OUTROS("Outros");

    private final String descricao;

    CategoriaDespesaEnum(String descricao) {
        this.descricao = descricao;
    }

    @Override
    @JsonValue
    public String toString() {
        return descricao;
    }
}
