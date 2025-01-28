package br.com.wilner.controleFinanceiro.entities.Transaction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransactionDTO(
        @NotBlank(message = "Tipo da transação é obrigatório") String transactionType,
        @NotNull(message = "Valor da transação é obrigatório") BigDecimal transactionValue,
        @NotBlank(message = "Descrição é obrigatória") String description,
        @NotBlank(message = "Forma de pagamento é obrigatória") String paymentMethod,
        @NotNull(message = "ID da categoria é obrigatório") String categoryName,
        @NotNull(message = "ID do usuário é obrigatório") String userName) {

}
