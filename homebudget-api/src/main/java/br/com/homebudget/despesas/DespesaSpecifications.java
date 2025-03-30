package br.com.homebudget.despesas;

import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class DespesaSpecifications {
    public static Specification<DespesaEntity> byUser(Long user_id){
        return (root, query, criteriaBuilder) -> {
         if (user_id == null){
             return null;
         }
         return criteriaBuilder.like(criteriaBuilder.lower(root.get("user").get("name")), "%" + user_id + "%");
        };
    }

    public static Specification<DespesaEntity> byUserId(Long userId) {
        return (root, query, criteriaBuilder) ->
                userId == null ? null : criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    public static Specification<DespesaEntity> byDateBetween(LocalDate dataInicio, LocalDate dataFim){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("data"), dataInicio, dataFim);
    }

}
