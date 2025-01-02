package br.com.wilner.controleFinanceiro.entities.Category;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CategoryResponseDTO(String name, String description, Boolean isActive, BigDecimal valueRealized,
                                  BigDecimal valueExpected) {

}
