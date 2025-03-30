package br.com.homebudget.investimentos.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum TipoInvestimentoEnum {
    TESOURO_DIRETO("Tesouro Direto"),
    CDB("CDB (Certificado de Depósito Bancário)"),
    LCI("LCI (Letra de Crédito Imobiliário)"),
    LCA("LCA (Letra de Crédito do Agronegócio)"),
    DEBENTURES("Debêntures"),
    POUPANCA("Poupança"),
    RDB("RDB (Recibo de Depósito Bancário)"),
    ACAO("Ações"),
    ETF("ETF (Exchange Traded Funds)"),
    FUNDO_IMOBILIARIO("Fundo Imobiliário (FII)"),
    OPCOES("Opções"),
    CONTRATOS_FUTUROS("Contratos Futuros"),
    BDR("BDR (Brazilian Depositary Receipts)"),
    FUNDO_RENDA_FIXA("Fundo de Renda Fixa"),
    FUNDO_MULTIMERCADO("Fundo Multimercado"),
    FUNDO_ACOES("Fundo de Ações"),
    FUNDO_CAMBIO("Fundo Cambial"),
    FUNDO_PREVIDENCIA("Fundo de Previdência"),
    CRIPTOMOEDA("Criptomoedas"),
    COMMODITIES("Commodities"),
    IMOVEIS("Imóveis"),
    PRIVATE_EQUITY("Private Equity"),
    VENTURE_CAPITAL("Venture Capital"),
    CROWDFUNDING("Crowdfunding"),
    OURO("Ouro e Metais Preciosos"),
    ARTE_COLECIONAVEIS("Arte e Colecionáveis"),
    SEGURO_VIDA_RESGATE("Seguro de Vida com Resgate");

    private final String descricao;

    TipoInvestimentoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    @JsonValue
    public String toString() {
        return descricao;
    }
}
