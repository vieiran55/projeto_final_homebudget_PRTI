package br.com.homebudget.receitas.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum FonteReceitaEnum {
    SALARIO("Salário"),
    FREELANCE("Freelance"),
    BENEFICIOS("Benefícios"),
    INVESTIMENTOS("Investimentos"),
    EMPRESTIMOS("Empréstimo"),
    OUTROS("Outros"),
    ALUGUEL("Aluguel"),
    BONUS("Bônus"),
    COMISSOES("Comissões"),
    DIVIDENDOS("Dividendos"),
    ROYALTIES("Royalties"),
    PENSÃO("Pensão"),
    APOSENTADORIA("Aposentadoria"),
    BOLSAS_ESTUDO("Bolsa de Estudo"),
    DOACOES("Doações"),
    PREMIO("Prêmio"),
    VENDA_BENS("Venda de Bens"),
    RENDIMENTOS_POUPANCA("Rendimentos de Poupança"),
    RENDIMENTOS_CDB("Rendimentos de CDB"),
    RENDIMENTOS_ACAO("Rendimentos de Ações"),
    RENDIMENTOS_FII("Rendimentos de Fundos Imobiliários"),
    RENDIMENTOS_CRIPTO("Rendimentos de Criptomoedas"),
    RENDIMENTOS_FOREIGN("Rendimentos de Investimentos no Exterior"),
    RESTITUICAO_IMPOSTO("Restituição de Imposto"),
    INDENIZACOES("Indenizações"),
    HERANCA("Herança"),
    PATROCINIOS("Patrocínios"),
    VENDAS_ONLINE("Vendas Online"),
    CASHBACK("Cashback"),
    GANHOS_APOSTAS("Ganhos com Apostas");

    private final String descricao;

    FonteReceitaEnum(String descricao) {
        this.descricao = descricao;
    }

    @Override
    @JsonValue
    public String toString() {
        return descricao;
    }
}
