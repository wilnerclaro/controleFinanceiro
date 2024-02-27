package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.entities.Transaction;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class TransactionValidationService {
    public void isValidTransaction(Transaction transaction) {
        if (transaction.getTransactionType().isEmpty())
            throw new ValidationException("Tipo da transação inexistente");
        if (transaction.getTransactionValue() == null)
            throw new ValidationException("Valor da transação inexistente");
        if (transaction.getTransactionDate() == null)
            throw new ValidationException("Data da transação inexistente");
        if (transaction.getPaymentMethod().isEmpty())
            throw new ValidationException("Forma de pagamento inexistente");
    }
}
