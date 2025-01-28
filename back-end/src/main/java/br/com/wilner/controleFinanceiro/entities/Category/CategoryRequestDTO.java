package br.com.wilner.controleFinanceiro.entities.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CategoryRequestDTO(@NotBlank(message = "O nome não pode ser vazio") String name,
                                 @NotBlank(message = "A descrição não pode ser vazia") String description,
                                 @NotNull(message = "O valor deve ser informado") BigDecimal valueExpected) {
}
