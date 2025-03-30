package br.com.homebudget.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public record UserDTO(
        Long id,
        String name,
        String email
) {
}
