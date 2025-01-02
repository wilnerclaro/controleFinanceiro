package br.com.wilner.controleFinanceiro.entities.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserDTO(
        @NotBlank(message = "O nome não pode estar vazio")
        String name,
        @Email(message = "E-mail inválido")
        String email) {

}
