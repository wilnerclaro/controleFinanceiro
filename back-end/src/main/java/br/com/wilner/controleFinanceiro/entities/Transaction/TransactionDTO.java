package br.com.wilner.controleFinanceiro.entities.Transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransactionDTO(
        String transactionType,
        @NotNull(message = "O valor da transação é obrigatório")
        @DecimalMin(value = "0.0", inclusive = true, message = "O valor da transação deve ser maior ou igual a zero")
        BigDecimal transactionValue,
        String categoryName,
        String description,
        String userName,
        String paymentMethod) {

}
