package br.com.wilner.controleFinanceiro.entities.Category;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CategoryTotals(String categoryName, BigDecimal totalExpected, BigDecimal totalRealized) {
}


