package br.com.homebudget.receitas;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ReceitaSpecifications {
    public static Specification<ReceitaEntity> byUser(Long user_id){
        return (root, query, criteriaBuilder) -> {
            if (user_id == null){
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + user_id + "%");
        };
    }

    public static Specification<ReceitaEntity> byUserId(Long userId) {
        return (root, query, criteriaBuilder) ->
                userId == null ? null : criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<ReceitaEntity> byDateBetween(LocalDate dataInicio, LocalDate dataFim){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("data"), dataInicio, dataFim);
    }
}
