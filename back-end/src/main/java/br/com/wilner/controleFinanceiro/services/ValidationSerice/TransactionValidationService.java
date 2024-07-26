package br.com.wilner.controleFinanceiro.services.ValidationSerice;

import br.com.wilner.controleFinanceiro.entities.Transaction.Transaction;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class TransactionValidationService implements ValidateMandatoryFields<Transaction> {


    @Override
    public void checkValidFields(Transaction transaction) {
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
