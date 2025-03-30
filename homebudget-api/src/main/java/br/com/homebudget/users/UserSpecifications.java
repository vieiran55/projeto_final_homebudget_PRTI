package br.com.homebudget.users;

import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {
    public static Specification<UserEntity> byName(String name)
    {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()){
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
}
