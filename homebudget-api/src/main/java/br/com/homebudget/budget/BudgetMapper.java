package br.com.homebudget.budget;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BudgetMapper {
    BudgetDTO toDto(Budget budget);
    Budget toEntity(BudgetDTO budgetDTO);
}