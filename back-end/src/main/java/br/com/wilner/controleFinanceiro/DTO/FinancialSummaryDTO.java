package br.com.wilner.controleFinanceiro.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Builder
@Setter

public class FinancialSummaryDTO {
    private final BigDecimal totalIncome;
    private final BigDecimal totalExpense;

    public FinancialSummaryDTO(BigDecimal totalIncome, BigDecimal totalExpense) {
        this.totalIncome = totalIncome == null ? BigDecimal.ZERO : totalIncome;
        this.totalExpense = totalExpense == null ? BigDecimal.ZERO : totalExpense;
    }
}
