package br.com.homebudget.investimentos;

import br.com.homebudget.auth.AuthService;
import br.com.homebudget.despesas.dto.DespesaDTO;
import br.com.homebudget.investimentos.dto.InvestimentoDTO;
import br.com.homebudget.investimentos.dto.InvestimentoInputDTO;
import br.com.homebudget.investimentos.dto.TipoInvestimentoResponse;
import br.com.homebudget.investimentos.enums.TipoInvestimentoEnum;
import br.com.homebudget.receitas.dto.FonteReceitaResponse;
import br.com.homebudget.receitas.enums.FonteReceitaEnum;
import br.com.homebudget.shared.dto.MetaResponse;
import br.com.homebudget.shared.response.PagedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class InvestimentoV1Controller {

    private final InvestimentoService investimentoService;
    private final InvestimentoMapper investimentoMapper;
    private final AuthService authService;

    @GetMapping("/investimentos")
    public ResponseEntity<PagedResponse<InvestimentoDTO>> index(
            @RequestParam(required = false) Long user_id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<InvestimentoDTO> investimentoDTOPage = investimentoService.getAll(user_id, pageable);

        PagedResponse<InvestimentoDTO> response = new PagedResponse<>(
                investimentoDTOPage.getContent(),
                new MetaResponse(
                        (int) investimentoDTOPage.getTotalElements(),
                        investimentoDTOPage.getTotalPages(),
                        investimentoDTOPage.getNumber() + 1,
                        investimentoDTOPage.getSize()
                )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/investimentos/{id}")
    public ResponseEntity<InvestimentoDTO> getInvestimentoById(@PathVariable Long id) {
        InvestimentoEntity entity = investimentoService.getById(id);
        InvestimentoDTO investimentoDTO = investimentoMapper.toDto(entity);
        return ResponseEntity.ok(investimentoDTO);
    }

    @PostMapping("/investimentos")
    public ResponseEntity<InvestimentoDTO> createInvestimento(@Valid @RequestBody InvestimentoInputDTO investimentoInputDTO) {
        Long userId = authService.getAuthenticatedUserId();
        InvestimentoEntity entity = investimentoService.create(investimentoInputDTO, userId);
        InvestimentoDTO investimentoDTO = investimentoMapper.toDto(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(investimentoDTO);
    }

    @PutMapping("/investimentos/{id}")
    public ResponseEntity<InvestimentoDTO> update(@PathVariable Long id, @Valid @RequestBody InvestimentoInputDTO investimentoInputDTO) {
        InvestimentoEntity entity = investimentoService.update(id, investimentoInputDTO);
        InvestimentoDTO updatedInvestimento = investimentoMapper.toDto(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedInvestimento);
    }

    @DeleteMapping("/investimentos/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        investimentoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/investimentos-mensais/{mes}/{ano}")
    public ResponseEntity<PagedResponse<InvestimentoDTO>> buscarPorMesEAno(
            @PathVariable int mes,
            @PathVariable int ano,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ){

        Long userId = authService.getAuthenticatedUserId();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<InvestimentoDTO> investimentoDTOPage = investimentoService.buscarPorMesEAno(userId, mes, ano, pageable);

        PagedResponse<InvestimentoDTO> response = new PagedResponse<>(
                investimentoDTOPage.getContent(),
                new MetaResponse(
                        (int) investimentoDTOPage.getTotalElements(),
                        investimentoDTOPage.getTotalPages(),
                        investimentoDTOPage.getNumber() + 1,
                        investimentoDTOPage.getSize()
                )
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/investimentos-user-consolidada")
    public ResponseEntity<PagedResponse<InvestimentoDTO>> getPastUntilCurrentMonth(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        Long userId = authService.getAuthenticatedUserId();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<InvestimentoDTO> investimentoDTOPage = investimentoService.getPastUntilCurrentMonth(userId, pageable);

        PagedResponse<InvestimentoDTO> response = new PagedResponse<>(
                investimentoDTOPage.getContent(),
                new MetaResponse(
                        (int) investimentoDTOPage.getTotalElements(),
                        investimentoDTOPage.getTotalPages(),
                        investimentoDTOPage.getNumber() + 1,
                        investimentoDTOPage.getSize()
                )
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("investimentos/total-mes/{mes}/{ano}")
    public ResponseEntity<BigDecimal> getTotalInvestimentos(@PathVariable int mes, @PathVariable int ano) {
        Long userId = authService.getAuthenticatedUserId();
        BigDecimal total = investimentoService.getTotalInvestimentosMensal(userId, ano, mes);
        return ResponseEntity.ok(total);
    }

    @GetMapping("investimentos/total-mes-consolidadas/{mes}/{ano}")
    public ResponseEntity<BigDecimal> getTotalInvestimentosAteMesAtualByUser(@PathVariable int mes, @PathVariable int ano) {
        Long userId = authService.getAuthenticatedUserId();
        BigDecimal total = investimentoService.getTotalInvestimentosAteMesAtualByUser(userId, ano, mes);
        return ResponseEntity.ok(total);
    }

    @GetMapping("investimentos/tipos")
    public List<TipoInvestimentoResponse> listarTipoInvestimentos() {
        return Arrays.stream(TipoInvestimentoEnum.values())
                .map(tipo -> new TipoInvestimentoResponse(tipo.name(), tipo.getDescricao()))
                .collect(Collectors.toList());
    }

}
