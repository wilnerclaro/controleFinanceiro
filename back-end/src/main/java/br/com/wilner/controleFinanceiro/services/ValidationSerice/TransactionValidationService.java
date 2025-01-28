package br.com.wilner.controleFinanceiro.services.ValidationSerice;

import br.com.wilner.controleFinanceiro.entities.Transaction.Transaction;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionValidationService implements ValidateMandatoryFields<Transaction> {


    @Override
    public void checkValidFields(Transaction transaction) {
        if (transaction.getTransactionType().isEmpty() || transaction.getTransactionType() == null)
            throw new ValidationException("Tipo da transação inexistente");
        if (transaction.getTransactionDate() == null)
            throw new ValidationException("Data da transação inexistente");
        if (transaction.getPaymentMethod().isEmpty() || transaction.getPaymentMethod() == null)
            throw new ValidationException("Forma de pagamento inexistente");
        if (transaction.getTransactionValue() == null || transaction.getTransactionValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("O valor da transação deve ser maior ou igual a zero");
        }
    }
}
