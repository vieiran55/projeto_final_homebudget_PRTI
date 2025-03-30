package br.com.homebudget.despesas;

import br.com.homebudget.auth.AuthService;
import br.com.homebudget.despesas.dto.CategoriaDespesaResponse;
import br.com.homebudget.despesas.dto.DespesaDTO;
import br.com.homebudget.despesas.dto.DespesaInputDTO;
import br.com.homebudget.despesas.enums.CategoriaDespesaEnum;
import br.com.homebudget.receitas.dto.FonteReceitaResponse;
import br.com.homebudget.receitas.enums.FonteReceitaEnum;
import br.com.homebudget.shared.dto.MetaResponse;
import br.com.homebudget.shared.response.PagedResponse;
import br.com.homebudget.users.UserEntity;
import br.com.homebudget.users.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class DespesaV1Controller {

    private final DespesaService despesaService;
    private final DespesaMapper despesaMapper;
    private final AuthService authService;

    @Autowired
    private DespesaRepository despesaRepository;

    @GetMapping("/despesas")
    public ResponseEntity<PagedResponse<DespesaDTO>> index (
            @RequestParam(required = false) Long user_id,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<DespesaDTO> despesaDTOPage = despesaService.getAll(user_id, pageable);

        PagedResponse<DespesaDTO> response = new PagedResponse<>(
                despesaDTOPage.getContent(),
                new MetaResponse(
                        (int) despesaDTOPage.getTotalElements(),
                        despesaDTOPage.getTotalPages(),
                        despesaDTOPage.getNumber() + 1,
                        despesaDTOPage.getSize()
                )
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/despesas/{id}")
    public ResponseEntity<DespesaDTO> getDespesaById(@PathVariable Long id){
        DespesaEntity entity = despesaService.getById(id);
        DespesaDTO despesaDTO = despesaMapper.toDto(entity);
        return ResponseEntity.ok(despesaDTO);
    }

    @PostMapping("/despesas")
    public ResponseEntity<DespesaDTO> createDespesa(@Valid @RequestBody DespesaInputDTO despesaInputDTO){
        Long userId = authService.getAuthenticatedUserId();
        DespesaEntity entity = despesaService.create(despesaInputDTO, userId);
        DespesaDTO despesaDTO = despesaMapper.toDto(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(despesaDTO);
    }

    @PutMapping("/despesas/{id}")
    public ResponseEntity<DespesaDTO> update (@PathVariable Long id,@Valid @RequestBody DespesaInputDTO despesaInputDto){
        DespesaEntity entity = despesaService.update(id, despesaInputDto);
        DespesaDTO updatedDespesa = despesaMapper.toDto(entity);
        return ResponseEntity.ok(updatedDespesa);
    }

    @DeleteMapping("/despesas/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        despesaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/despesas-mensais/{mes}/{ano}")
    public ResponseEntity<PagedResponse<DespesaDTO>> buscarPorMesEAno(
            @PathVariable int mes,
            @PathVariable int ano,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @AuthenticationPrincipal UserDetails userDetails
    ){

        Long userId = authService.getAuthenticatedUserId();

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<DespesaDTO> despesaDTOPage = despesaService.buscarPorMesEAno(userId, mes, ano, pageable);

        PagedResponse<DespesaDTO> response = new PagedResponse<>(
                despesaDTOPage.getContent(),
                new MetaResponse(
                        (int) despesaDTOPage.getTotalElements(),
                        despesaDTOPage.getTotalPages(),
                        despesaDTOPage.getNumber() + 1,
                        despesaDTOPage.getSize()
                )
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/despesas-user-consolidada")
    public ResponseEntity<PagedResponse<DespesaDTO>> getPastUntilCurrentMonth(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        Long userId = authService.getAuthenticatedUserId();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<DespesaDTO> despesaDTOPage = despesaService.getPastUntilCurrentMonth(userId, pageable);


        PagedResponse<DespesaDTO> response = new PagedResponse<>(
                despesaDTOPage.getContent(),
                new MetaResponse(
                        (int) despesaDTOPage.getTotalElements(),
                        despesaDTOPage.getTotalPages(),
                        despesaDTOPage.getNumber() + 1,
                        despesaDTOPage.getSize()
                )
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("despesas/total-mes/{mes}/{ano}")
    public ResponseEntity<BigDecimal> getTotalDespesas(@PathVariable int mes, @PathVariable int ano) {
        Long userId = authService.getAuthenticatedUserId();
        BigDecimal total = despesaService.getTotalDespesasMensal(userId, ano, mes);
        return ResponseEntity.ok(total);
    }

    @GetMapping("despesas/total-mes-consolidadas/{mes}/{ano}")
    public ResponseEntity<BigDecimal> getTotalDespesasAteMesAtualByUser(@PathVariable int mes, @PathVariable int ano) {
        Long userId = authService.getAuthenticatedUserId();
        BigDecimal total = despesaService.getTotalDespesasAteMesAtualByUser(userId, ano, mes);
        return ResponseEntity.ok(total);
    }

    @GetMapping("despesas/categorias")
    public List<CategoriaDespesaResponse> listarCategoriaDespesas() {
        return Arrays.stream(CategoriaDespesaEnum.values())
                .map(tipo -> new CategoriaDespesaResponse(tipo.name(), tipo.getDescricao()))
                .collect(Collectors.toList());
    }
}
