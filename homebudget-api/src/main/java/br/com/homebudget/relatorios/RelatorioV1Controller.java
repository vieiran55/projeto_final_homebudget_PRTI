package br.com.homebudget.relatorios;

import br.com.homebudget.auth.AuthService;
import br.com.homebudget.relatorios.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class RelatorioV1Controller {
    private final AuthService authService;

    private final RelatorioService relatorioService;

    @GetMapping("/relatorios/ultimos-12-meses/")
    public ResponseEntity<List<ResumoFinanceiroDTO>> getReceitasDespesas() {
        Long userId = authService.getAuthenticatedUserId();
        return ResponseEntity.ok(relatorioService.getReceitasDespesasUltimos12Meses(userId));
    }

    @GetMapping("/relatorios/despesas-por-categoria")
    public ResponseEntity<List<DistribuicaoDespesasDTO>> getDespesasPorCategoria(
            @RequestParam int anoInicio,
            @RequestParam int mesInicio,
            @RequestParam int anoFim,
            @RequestParam int mesFim
    ) {
        Long userId = authService.getAuthenticatedUserId();
        return ResponseEntity.ok(relatorioService.getDistribuicaoDespesas(userId, anoInicio, mesInicio, anoFim, mesFim));
    }

    @GetMapping("/relatorios/receitas-por-fonte")
    public ResponseEntity<List<DistribuicaoReceitasDTO>> getReceitasPorFonte(
            @RequestParam int anoInicio,
            @RequestParam int mesInicio,
            @RequestParam int anoFim,
            @RequestParam int mesFim
    ) {
        Long userId = authService.getAuthenticatedUserId();
        return ResponseEntity.ok(relatorioService.getDistribuicaoReceitas(userId, anoInicio, mesInicio, anoFim, mesFim));
    }

    @GetMapping("/relatorios/investimentos-por-tipo")
    public ResponseEntity<List<DistribuicaoInvestimentoDTO>> getInvestimentosPorTipo(
            @RequestParam int anoInicio,
            @RequestParam int mesInicio,
            @RequestParam int anoFim,
            @RequestParam int mesFim
    ) {
        Long userId = authService.getAuthenticatedUserId();
        return ResponseEntity.ok(relatorioService.getDistribuicaoInvestimentos(userId, anoInicio, mesInicio, anoFim, mesFim));
    }

    @GetMapping("/relatorios/saldo-mensal")
    public ResponseEntity<List<SaldoMensalDTO>> getSaldoMensal(
            @RequestParam int anoInicio,
            @RequestParam int mesInicio,
            @RequestParam int anoFim,
            @RequestParam int mesFim
    ) {
        Long userId = authService.getAuthenticatedUserId();
        return ResponseEntity.ok(relatorioService.getEvolucaoSaldoMensal(userId, anoInicio, mesInicio, anoFim, mesFim));
    }

    @GetMapping("/relatorios/saldo-mensal-total")
    public ResponseEntity<List<SaldoMensalTotalDTO>> getSaldoMensalTotal(
            @RequestParam int anoInicio,
            @RequestParam int mesInicio,
            @RequestParam int anoFim,
            @RequestParam int mesFim
    ) {
        Long userId = authService.getAuthenticatedUserId();
        return ResponseEntity.ok(relatorioService.getEvolucaoSaldoMensalTotal(userId, anoInicio, mesInicio, anoFim, mesFim));
    }

    @GetMapping("/relatorios/comparacao-gastos")
    public ResponseEntity<List<ComparacaoGastosDTO>> getComparacaoMeses(
            @RequestParam int ano1,
            @RequestParam int mes1,
            @RequestParam int ano2,
            @RequestParam int mes2
    ) {
        Long userId = authService.getAuthenticatedUserId();
        return ResponseEntity.ok(relatorioService.getComparacaoGastos(userId, ano1, mes1, ano2, mes2));
    }

    @GetMapping("/relatorios/projecao-financeira")
    public ResponseEntity<List<ProjecaoFinanceiraDTO>> getProjecaoFinanceira(
            @RequestParam int mesesProjecao
    ) {
        Long userId = authService.getAuthenticatedUserId();
        List<ProjecaoFinanceiraDTO> projecao = relatorioService.getProjecaoFinanceira(userId, mesesProjecao);
        return ResponseEntity.ok(projecao);
    }
}
