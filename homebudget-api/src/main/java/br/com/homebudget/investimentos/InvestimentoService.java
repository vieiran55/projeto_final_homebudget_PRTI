package br.com.homebudget.investimentos;


import br.com.homebudget.despesas.DespesaEntity;
import br.com.homebudget.despesas.DespesaSpecifications;
import br.com.homebudget.despesas.dto.DespesaDTO;
import br.com.homebudget.investimentos.dto.InvestimentoDTO;
import br.com.homebudget.investimentos.dto.InvestimentoInputDTO;
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
public class InvestimentoService {

    private final InvestimentoRepository investimentoRepository;
    private final InvestimentoMapper investimentoMapper;
    private final UserRepository userRepository;

    public Page<InvestimentoDTO> getAll(Long user_id, Pageable pageable) {
        Specification<InvestimentoEntity> spec = Specification.where(InvestimentoSpecifications.byUser(user_id));

        return investimentoRepository.findAll(spec, pageable)
                .map(investimentoMapper::toDto);
    }

    public InvestimentoEntity getById(Long id) {
        return investimentoRepository.findById(id).orElseThrow(() -> new NotFoundException("Despesa não encontrada"));
    }

    @Transactional
    public InvestimentoEntity create(@Valid InvestimentoInputDTO investimentoInputDTO, Long userId) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário com ID " + userId + " não encontrado."));

        InvestimentoEntity receitaEntity = InvestimentoMapper.INSTANCE.toEntity(investimentoInputDTO);

        receitaEntity.setUser(user);
        return investimentoRepository.save(receitaEntity);
    }

    @Transactional
    public InvestimentoEntity update(Long id, InvestimentoInputDTO investimentoInputDTO) {
        InvestimentoEntity entity = investimentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Receita não encontrada"));

        investimentoMapper.updateEntityFromDto(investimentoInputDTO, entity);
        return investimentoRepository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        InvestimentoEntity entity = investimentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Despesa não econtrada"));
        investimentoRepository.deleteById(entity.getId());
    }

    public Page<InvestimentoDTO> buscarPorMesEAno(Long userId, int mes, int ano, Pageable pageable) {
        YearMonth yearMonth = YearMonth.of(ano, mes);
        LocalDate dataInicio = yearMonth.atDay(1);
        LocalDate dataFim = yearMonth.atEndOfMonth();

        Specification<InvestimentoEntity> spec = Specification
                .where(InvestimentoSpecifications.byUserId(userId))
                .and(InvestimentoSpecifications.byDateBetween(dataInicio, dataFim));

        return investimentoRepository.findAll(spec, pageable).map(investimentoMapper::toDto);
    }

    public Page<InvestimentoDTO> getPastUntilCurrentMonth(Long userId, Pageable pageable) {
        LocalDate currentDate = LocalDate.now();

        return investimentoRepository
                .findPastUntilCurrentMonth(userId, currentDate.getYear(), currentDate.getMonthValue(), pageable)
                .map(investimentoMapper::toDto);
    }

    public BigDecimal getTotalInvestimentosMensal(Long userId, int ano, int mes) {
        return investimentoRepository.somarInvestimentosMesAtualByUser(userId, ano, mes);
    }

    public BigDecimal getTotalInvestimentosAteMesAtualByUser(Long userId, int ano, int mes) {
        return investimentoRepository.somarInvestimentosAteMesAtualByUser(userId, ano, mes);
    }
}
