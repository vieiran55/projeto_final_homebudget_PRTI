package br.com.homebudget.relatorios;

import br.com.homebudget.despesas.DespesaRepository;
import br.com.homebudget.despesas.enums.CategoriaDespesaEnum;
import br.com.homebudget.investimentos.InvestimentoRepository;
import br.com.homebudget.investimentos.enums.TipoInvestimentoEnum;
import br.com.homebudget.receitas.ReceitaRepository;
import br.com.homebudget.receitas.enums.FonteReceitaEnum;
import br.com.homebudget.relatorios.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final DespesaRepository despesaRepository;
    private final ReceitaRepository receitaRepository;
    private final InvestimentoRepository investimentoRepository;

    public List<ResumoFinanceiroDTO> getReceitasDespesasUltimos12Meses(Long userId) {
        LocalDate dataInicio = LocalDate.now().minusMonths(11).withDayOfMonth(1);

        List<Object[]> despesas = despesaRepository.getDespesasUltimos12Meses(userId, dataInicio);
        List<Object[]> receitas = receitaRepository.getReceitasUltimos12Meses(userId, dataInicio);
        List<Object[]> investimentos = investimentoRepository.getInvestimentosUltimos12Meses(userId, dataInicio);

        Map<String, BigDecimal> mapDespesas = mapearValores(despesas);
        Map<String, BigDecimal> mapReceitas = mapearValores(receitas);
        Map<String, BigDecimal> mapInvestimentos = mapearValores(investimentos);

        List<ResumoFinanceiroDTO> resultado = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            LocalDate mesAno = dataInicio.plusMonths(i);
            String chave = mesAno.getYear() + "-" + mesAno.getMonthValue();

            resultado.add(new ResumoFinanceiroDTO(
                    mesAno.getYear(),
                    mesAno.getMonthValue(),
                    mapReceitas.getOrDefault(chave, BigDecimal.ZERO),
                    mapDespesas.getOrDefault(chave, BigDecimal.ZERO),
                    mapInvestimentos.getOrDefault(chave, BigDecimal.ZERO)
            ));
        }

        return resultado;
    }

    public List<DistribuicaoDespesasDTO> getDistribuicaoDespesas(Long userId, int anoInicio, int mesInicio, int anoFim, int mesFim) {
        List<Object[]> resultados = despesaRepository.getDistribuicaoDespesasPorCategoria(userId, anoInicio, mesInicio, anoFim, mesFim);

        return resultados.stream()
                .map(obj -> new DistribuicaoDespesasDTO(((CategoriaDespesaEnum) obj[0]).toString(), (BigDecimal) obj[1]))
                .collect(Collectors.toList());
    }

    public List<DistribuicaoInvestimentoDTO> getDistribuicaoInvestimentos(Long userId, int anoInicio, int mesInicio, int anoFim, int mesFim) {
        List<Object[]> resultados = investimentoRepository.getDistribuicaoInvestimentoPorTipo(userId, anoInicio, mesInicio, anoFim, mesFim);

        return resultados.stream()
                .map(obj -> new DistribuicaoInvestimentoDTO(((TipoInvestimentoEnum) obj[0]).toString(), (BigDecimal) obj[1]))
                .collect(Collectors.toList());
    }

    public List<DistribuicaoReceitasDTO> getDistribuicaoReceitas(Long userId, int anoInicio, int mesInicio, int anoFim, int mesFim) {
        List<Object[]> resultados = receitaRepository.getDistribuicaoReceitasPorFonte(userId, anoInicio, mesInicio, anoFim, mesFim);

        return resultados.stream()
                .map(obj -> new DistribuicaoReceitasDTO(((FonteReceitaEnum) obj[0]).toString(), (BigDecimal) obj[1]))
                .collect(Collectors.toList());
    }

    public List<SaldoMensalDTO> getEvolucaoSaldoMensal(Long userId, int anoInicio, int mesInicio, int anoFim, int mesFim) {
        List<Object[]> despesas = despesaRepository.getDespesasMensaisGrafico(userId, anoInicio, mesInicio, anoFim, mesFim);
        List<Object[]> receitas = receitaRepository.getReceitasMensaisGrafico(userId, anoInicio, mesInicio, anoFim, mesFim);

        Map<String, BigDecimal> saldoMensal = new TreeMap<>();

        for (Object[] row : receitas) {
            String key = row[0] + "-" + String.format("%02d", row[1]); // Ano-Mês (ex: "2024-03")
            saldoMensal.put(key, (BigDecimal) row[2]);
        }

        for (Object[] row : despesas) {
            String key = row[0] + "-" + String.format("%02d", row[1]);
            saldoMensal.put(key, saldoMensal.getOrDefault(key, BigDecimal.ZERO).subtract((BigDecimal) row[2]));
        }

        return saldoMensal.entrySet().stream()
                .map(entry -> new SaldoMensalDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public List<SaldoMensalTotalDTO> getEvolucaoSaldoMensalTotal(Long userId, int anoInicio, int mesInicio, int anoFim, int mesFim) {
        List<Object[]> despesas = despesaRepository.getDespesasMensaisGrafico(userId, anoInicio, mesInicio, anoFim, mesFim);
        List<Object[]> receitas = receitaRepository.getReceitasMensaisGrafico(userId, anoInicio, mesInicio, anoFim, mesFim);
        List<Object[]> investimentos = investimentoRepository.getInvestimentosMensaisGrafico(userId, anoInicio, mesInicio, anoFim, mesFim);

        Map<String, BigDecimal> despesasMensais = new TreeMap<>();
        Map<String, BigDecimal> receitasMensais = new TreeMap<>();
        Map<String, BigDecimal> investimentosMensais = new TreeMap<>();

        for (Object[] row : despesas) {
            String key = row[0] + "-" + String.format("%02d", row[1]);
            despesasMensais.put(key, (BigDecimal) row[2]);
        }
        for (Object[] row : receitas) {
            String key = row[0] + "-" + String.format("%02d", row[1]);
            receitasMensais.put(key, (BigDecimal) row[2]);
        }
        for (Object[] row : investimentos) {
            String key = row[0] + "-" + String.format("%02d", row[1]);
            investimentosMensais.put(key, (BigDecimal) row[2]);
        }

        LocalDate dataAtual = LocalDate.of(anoInicio, mesInicio, 1);
        LocalDate dataFinal = LocalDate.of(anoFim, mesFim, 1);

        List<SaldoMensalTotalDTO> resultado = new ArrayList<>();
        BigDecimal saldoTotal = BigDecimal.ZERO; // Inicializa o saldo total acumulado
        BigDecimal totalReceitasAcumuladas = BigDecimal.ZERO;
        BigDecimal totalDespesasAcumuladas = BigDecimal.ZERO;
        BigDecimal totalInvestimentosAcumulados = BigDecimal.ZERO;

        while (!dataAtual.isAfter(dataFinal)) {
            String key = dataAtual.getYear() + "-" + String.format("%02d", dataAtual.getMonthValue());

            BigDecimal totalReceitas = receitasMensais.getOrDefault(key, BigDecimal.ZERO);
            BigDecimal totalDespesas = despesasMensais.getOrDefault(key, BigDecimal.ZERO);
            BigDecimal totalInvestimentos = investimentosMensais.getOrDefault(key, BigDecimal.ZERO);

            // Atualiza os acumulados
            totalReceitasAcumuladas = totalReceitasAcumuladas.add(totalReceitas);
            totalDespesasAcumuladas = totalDespesasAcumuladas.add(totalDespesas);
            totalInvestimentosAcumulados = totalInvestimentosAcumulados.add(totalInvestimentos);

            // Calcula os valores finais
            BigDecimal saldoMensal = totalReceitas.subtract(totalInvestimentos).subtract(totalDespesas); // Receitas - Aporte - Despesas
            BigDecimal aporteMensal = totalInvestimentos; // Investimentos do mês atual
            saldoTotal = totalReceitasAcumuladas.add(totalInvestimentosAcumulados).subtract(totalDespesasAcumuladas);

            // Adiciona ao resultado
            resultado.add(new SaldoMensalTotalDTO(key, saldoMensal, saldoTotal, aporteMensal, totalInvestimentosAcumulados));

            dataAtual = dataAtual.plusMonths(1);
        }

        return resultado;
    }







    public List<ComparacaoGastosDTO> getComparacaoGastos(Long userId, int ano1, int mes1, int ano2, int mes2) {
        List<Object[]> resultados = despesaRepository.getComparacaoDespesasPorCategoria(userId, ano1, mes1, ano2, mes2);

        return resultados.stream()
                .map(row -> new ComparacaoGastosDTO(
                        ((CategoriaDespesaEnum) row[0]).getDescricao(),
                        (BigDecimal) row[1], // Total do primeiro período
                        (BigDecimal) row[2]  // Total do segundo período
                ))
                .collect(Collectors.toList());
    }

    public List<ProjecaoFinanceiraDTO> getProjecaoFinanceira(Long userId, int mesesProjecao) {
        // Obtém histórico de despesas e receitas
        List<Object[]> despesas = despesaRepository.getHistoricoDespesas(userId);
        List<Object[]> receitas = receitaRepository.getHistoricoReceitas(userId);

        // Calcula média mensal dos últimos 6 meses
        BigDecimal mediaDespesas = calcularMedia(despesas, 6);
        BigDecimal mediaReceitas = calcularMedia(receitas, 6);

        // Projeta os próximos meses
        List<ProjecaoFinanceiraDTO> projecoes = new ArrayList<>();
        LocalDate hoje = LocalDate.now();

        BigDecimal saldoAcumulado = mediaReceitas.subtract(mediaDespesas); // Primeiro saldo projetado

        for (int i = 1; i <= mesesProjecao; i++) {
            LocalDate dataFutura = hoje.plusMonths(i);
            saldoAcumulado = saldoAcumulado.add(mediaReceitas).subtract(mediaDespesas);

            projecoes.add(new ProjecaoFinanceiraDTO(
                    dataFutura.getYear(),
                    dataFutura.getMonthValue(),
                    saldoAcumulado
            ));
        }

        return projecoes;
    }

    // Método auxiliar para calcular a média
    private BigDecimal calcularMedia(List<Object[]> valores, int meses) {
        if (valores.isEmpty()) return BigDecimal.ZERO;

        BigDecimal soma = BigDecimal.ZERO;
        int count = Math.min(valores.size(), meses);

        for (int i = 0; i < count; i++) {
            soma = soma.add((BigDecimal) valores.get(i)[2]);
        }

        return soma.divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP);
    }


    private Map<String, BigDecimal> mapearValores(List<Object[]> lista) {
        return lista.stream()
                .collect(Collectors.toMap(
                        item -> item[0] + "-" + item[1],
                        item -> (BigDecimal) item[2]
                ));
    }
}