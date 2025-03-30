package br.com.homebudget.receitas;

import br.com.homebudget.auth.AuthService;
import br.com.homebudget.receitas.dto.FonteReceitaResponse;
import br.com.homebudget.receitas.dto.ReceitaDTO;
import br.com.homebudget.receitas.dto.ReceitaInputDTO;
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
public class ReceitaV1Controller {

    private final ReceitaService receitaService;
    private final ReceitaMapper receitaMapper;
    private final AuthService authService;

    @GetMapping("/receitas")
    public ResponseEntity<PagedResponse<ReceitaDTO>> index(
            @RequestParam(required = false) Long user_id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ReceitaDTO> receitaDTOPage = receitaService.getAll(user_id, pageable);

        PagedResponse<ReceitaDTO> response = new PagedResponse<>(
                receitaDTOPage.getContent(),
                new MetaResponse(
                        (int) receitaDTOPage.getTotalElements(),
                        receitaDTOPage.getTotalPages(),
                        receitaDTOPage.getNumber() + 1,
                        receitaDTOPage.getSize()
                )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/receitas/{id}")
    public ResponseEntity<ReceitaDTO> getReceitaById(@PathVariable Long id) {
        ReceitaEntity entity = receitaService.getById(id);
        ReceitaDTO receitaDTO = receitaMapper.toDto(entity);
        return ResponseEntity.ok(receitaDTO);
    }

    @PostMapping("/receitas")
    public ResponseEntity<ReceitaDTO> createReceita(@Valid @RequestBody ReceitaInputDTO receitaInputDTO) {
        Long userId = authService.getAuthenticatedUserId();
        ReceitaEntity entity = receitaService.create(receitaInputDTO, userId);
        ReceitaDTO receitaDTO = receitaMapper.toDto(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(receitaDTO);
    }

    @PutMapping("/receitas/{id}")
    public ResponseEntity<ReceitaDTO> update(@PathVariable Long id, @Valid @RequestBody ReceitaInputDTO receitaInputDTO) {
        ReceitaEntity entity = receitaService.update(id, receitaInputDTO);
        ReceitaDTO updatedReceita = receitaMapper.toDto(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(updatedReceita);
    }

    @DeleteMapping("/receitas/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        receitaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/receitas-mensais/{mes}/{ano}")
    public ResponseEntity<PagedResponse<ReceitaDTO>> buscarPorMesEAno(
            @PathVariable int mes,
            @PathVariable int ano,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        Long userId = authService.getAuthenticatedUserId();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ReceitaDTO> receitaDTOPage = receitaService.buscarPorMesEAno(userId, mes, ano, pageable);

        PagedResponse<ReceitaDTO> response = new PagedResponse<>(
                receitaDTOPage.getContent(),
                new MetaResponse(
                        (int) receitaDTOPage.getTotalElements(),
                        receitaDTOPage.getTotalPages(),
                        receitaDTOPage.getNumber() + 1,
                        receitaDTOPage.getSize()
                )
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/receitas-user-consolidada")
    public ResponseEntity<PagedResponse<ReceitaDTO>> getPastUntilCurrentMonth(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        Long userId = authService.getAuthenticatedUserId();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ReceitaDTO> receitaDTOPage = receitaService.getPastUntilCurrentMonth(userId, pageable);

        PagedResponse<ReceitaDTO> response = new PagedResponse<>(
                receitaDTOPage.getContent(),
                new MetaResponse(
                        (int) receitaDTOPage.getTotalElements(),
                        receitaDTOPage.getTotalPages(),
                        receitaDTOPage.getNumber() + 1,
                        receitaDTOPage.getSize()
                )
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("receitas/total-mes/{mes}/{ano}")
    public ResponseEntity<BigDecimal> getTotalDespesas(@PathVariable int mes, @PathVariable int ano) {
        Long userId = authService.getAuthenticatedUserId();
        BigDecimal total = receitaService.getTotalReceitasMensal(userId, ano, mes);
        return ResponseEntity.ok(total);
    }

    @GetMapping("receitas/total-mes-consolidadas/{mes}/{ano}")
    public ResponseEntity<BigDecimal> getTotalReceitassAteMesAtualByUser(@PathVariable int mes, @PathVariable int ano) {
        Long userId = authService.getAuthenticatedUserId();
        BigDecimal total = receitaService.getTotalReceitassAteMesAtualByUser(userId, ano, mes);
        return ResponseEntity.ok(total);
    }

    @GetMapping("receitas/fontes")
    public List<FonteReceitaResponse> listarFonteReceitas() {
        return Arrays.stream(FonteReceitaEnum.values())
                .map(tipo -> new FonteReceitaResponse(tipo.name(), tipo.getDescricao()))
                .collect(Collectors.toList());
    }
}
