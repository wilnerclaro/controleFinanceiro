package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.DTO.TransactionDTO;
import br.com.wilner.controleFinanceiro.entities.Transaction;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import br.com.wilner.controleFinanceiro.repositories.TransactionRepository;
import br.com.wilner.controleFinanceiro.util.converter.TransactionConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionConverter transactionConverter;

    public TransactionDTO saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = transactionConverter.converterToEntity(transactionDTO);
        isValidTransaction(transaction);
        try {
            Transaction savedTransaction = transactionRepository.save(transaction);
            return transactionConverter.converterToDTO(savedTransaction);
        } catch (Exception e) {
            throw new ValidationException("Erro ao salvar transação ");
        }
    }

    private void isValidTransaction(Transaction transaction) {
        if (transaction.getTransactionType() == null) throw new ValidationException("Tipo da transação inexistente");
        if (transaction.getTransactionValue() == null) throw new ValidationException("Valor da transação inexistente");
        if (transaction.getTransactionDate() == null) throw new ValidationException("Data da transação inexistente");
        if (transaction.getPaymentMethod() == null) throw new ValidationException("Forma de pagamento inexistente");


    }
}
