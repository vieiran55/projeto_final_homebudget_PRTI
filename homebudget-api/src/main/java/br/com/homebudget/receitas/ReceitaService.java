package br.com.homebudget.receitas;


import br.com.homebudget.despesas.DespesaEntity;
import br.com.homebudget.despesas.DespesaSpecifications;
import br.com.homebudget.despesas.dto.DespesaDTO;
import br.com.homebudget.receitas.dto.ReceitaDTO;
import br.com.homebudget.receitas.dto.ReceitaInputDTO;
import br.com.homebudget.shared.exceptions.NotFoundException;
import br.com.homebudget.users.UserEntity;
import br.com.homebudget.users.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class ReceitaService {

    private final ReceitaRepository receitaRepository;
    private final ReceitaMapper receitaMapper;
    private final UserRepository userRepository;

    public Page<ReceitaDTO> getAll(Long user_id, Pageable pageable) {
        Specification<ReceitaEntity> spec = Specification.where(ReceitaSpecifications.byUser(user_id));

        return receitaRepository.findAll(spec, pageable)
                .map(receitaMapper::toDto);
    }

    public ReceitaEntity getById(Long id) {
        return receitaRepository.findById(id).orElseThrow(() -> new NotFoundException("Despesa não encontrada"));
    }

    @Transactional
    public ReceitaEntity create(@Valid ReceitaInputDTO receitaInputDTO, Long userId){

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário com ID " + userId + " não encontrado."));

        ReceitaEntity receitaEntity = ReceitaMapper.INSTANCE.toEntity(receitaInputDTO);
        receitaEntity.setUser(user);
        return receitaRepository.save(receitaEntity);
    }

    @Transactional
    public ReceitaEntity update (Long id, ReceitaInputDTO receitaInputDTO){
        ReceitaEntity entity = receitaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Receita não encontrada"));

        receitaMapper.updateEntityFromDto(receitaInputDTO, entity);
        return receitaRepository.save(entity);
    }

    @Transactional
    public void delete(Long id){
        ReceitaEntity entity = receitaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Despesa não econtrada"));
        receitaRepository.deleteById(entity.getId());
    }

    public Page<ReceitaDTO> buscarPorMesEAno(Long userId, int mes, int ano, Pageable pageable){
        YearMonth yearMonth = YearMonth.of(ano, mes);
        LocalDate dataInicio = yearMonth.atDay(1);
        LocalDate dataFim = yearMonth.atEndOfMonth();

        Specification<ReceitaEntity> spec = Specification
                .where(ReceitaSpecifications.byUserId(userId))
                .and(ReceitaSpecifications.byDateBetween(dataInicio, dataFim));

        return receitaRepository.findAll(spec, pageable).map(receitaMapper::toDto);
    }

    public Page<ReceitaDTO> getPastUntilCurrentMonth(Long userId, Pageable pageable){
        LocalDate currentDate = LocalDate.now();

        return receitaRepository
                .findPastUntilCurrentMonth(userId, currentDate.getYear(), currentDate.getMonthValue(), pageable)
                .map(receitaMapper::toDto);
    }

    public BigDecimal getTotalReceitasMensal(Long userId, int ano, int mes) {
        return receitaRepository.somarReceitasMesAtualByUser(userId, ano, mes);
    }

    public BigDecimal getTotalReceitassAteMesAtualByUser(Long userId, int ano, int mes) {
        return receitaRepository.somarReceitasAteMesAtualByUser(userId, ano, mes);
    }
}
