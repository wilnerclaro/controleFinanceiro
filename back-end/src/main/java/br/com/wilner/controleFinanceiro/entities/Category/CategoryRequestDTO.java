package br.com.wilner.controleFinanceiro.entities.Category;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CategoryRequestDTO(String name, String description, BigDecimal valueExpected) {
}
