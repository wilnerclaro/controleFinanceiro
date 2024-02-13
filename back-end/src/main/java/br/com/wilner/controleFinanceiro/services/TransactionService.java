package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.DTO.TransactionDTO;
import br.com.wilner.controleFinanceiro.entities.Transaction;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import br.com.wilner.controleFinanceiro.repositories.TransactionRepository;
import br.com.wilner.controleFinanceiro.util.converter.TransactionConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public TransactionDTO updateTransaction(Long id, TransactionDTO updateTransactionDTO) {
        try {
            Transaction transactionById = transactionRepository.findById(id).orElseThrow(() -> new ValidationException("Transação não encontrada " + id));
            transactionById = transactionConverter.converterToEntityUpdate(transactionById, updateTransactionDTO);
            return transactionConverter.converterToDTO(transactionRepository.save(transactionById));
        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao atualizar tranzacao: " + id, e);
        }
    }

    public void deleteTransaction(Long id) {
        try {
            transactionRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ValidationException("Transação não encontrada: " + id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar transação: " + id, e);
        }
    }

    public TransactionDTO getTransactionById(Long id) {
        Transaction transactionId = transactionRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Transacao não encontrado " + id));
        return transactionConverter.converterToDTO(transactionId);
    }

    public List<TransactionDTO> getTransactionsByUserId(Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);
        if (transactions.isEmpty()) {
            throw new ValidationException("Não existem transações para este usuário! ");
        }
        return transactions.stream()
                .map(transactionConverter::converterToDTO)
                .toList();
    }


}
