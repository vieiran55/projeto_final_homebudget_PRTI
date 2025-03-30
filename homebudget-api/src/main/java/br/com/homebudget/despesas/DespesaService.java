package br.com.homebudget.despesas;

import br.com.homebudget.despesas.dto.DespesaDTO;
import br.com.homebudget.despesas.dto.DespesaInputDTO;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class DespesaService {

    private final DespesaRepository despesaRepository;
    private final DespesaMapper despesaMapper;
    private final UserRepository userRepository;

    public Page<DespesaDTO> getAll(Long user_id, Pageable pageable) {
        Specification<DespesaEntity> spec = Specification.where(DespesaSpecifications.byUser(user_id));

        return despesaRepository.findAll(spec, pageable)
                .map(despesaMapper::toDto);
    }

    public DespesaEntity getById(Long id) {
        return despesaRepository.findById(id).orElseThrow(() -> new NotFoundException("Despesa não encontrada"));
    }

    @Transactional
    public DespesaEntity create(@Valid DespesaInputDTO despesaInputDTO, Long userId) {


        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário com ID " + userId + " não encontrado."));


        DespesaEntity despesaEntity = DespesaMapper.INSTANCE.toEntity(despesaInputDTO);


        despesaEntity.setUser(user);

        return despesaRepository.save(despesaEntity);
    }

    @Transactional
    public DespesaEntity update(Long id, DespesaInputDTO despesaInputDTO){
        DespesaEntity entity = despesaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Despesa não encontrada"));

        despesaMapper.updateEntityFromDto(despesaInputDTO, entity);
        return despesaRepository.save(entity);
    }

    @Transactional
    public void delete(Long id){
        DespesaEntity entity = despesaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Despesa não encontrada"));
        despesaRepository.deleteById(entity.getId());
    }

    public Page<DespesaDTO> buscarPorMesEAno(Long userId, int mes, int ano, Pageable pageable){
        YearMonth yearMonth = YearMonth.of(ano, mes);
        LocalDate dataInicio = yearMonth.atDay(1);
        LocalDate dataFim = yearMonth.atEndOfMonth();

        Specification<DespesaEntity> spec = Specification
                .where(DespesaSpecifications.byUserId(userId))
                .and(DespesaSpecifications.byDateBetween(dataInicio, dataFim));

        return despesaRepository.findAll(spec, pageable).map(despesaMapper::toDto);
    }

    public Page<DespesaDTO> getPastUntilCurrentMonth(Long userId, Pageable pageable){
        LocalDate currentDate = LocalDate.now();

        return despesaRepository
                .findPastUntilCurrentMonth(userId, currentDate.getYear(), currentDate.getMonthValue(), pageable)
                .map(despesaMapper::toDto);
    }

    public BigDecimal getTotalDespesasMensal(Long userId, int ano, int mes) {
        return despesaRepository.somarDespesasMesAtualByUser(userId, ano, mes);
    }

    public BigDecimal getTotalDespesasAteMesAtualByUser(Long userId, int ano, int mes) {
        return despesaRepository.somarDespesasAteMesAtualByUser(userId, ano, mes);
    }
}
