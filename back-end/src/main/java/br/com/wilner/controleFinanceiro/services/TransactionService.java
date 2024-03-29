package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.DTO.TransactionDTO;
import br.com.wilner.controleFinanceiro.entities.Transaction;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import br.com.wilner.controleFinanceiro.repositories.TransactionRepository;
import br.com.wilner.controleFinanceiro.services.ValidationSerice.TransactionValidationService;
import br.com.wilner.controleFinanceiro.util.converter.TransactionConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionConverter transactionConverter;
    private final TransactionValidationService transactionValidationService;

    public TransactionDTO saveTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = transactionConverter.converterToEntity(transactionDTO);
        transactionValidationService.checkValidFields(transaction);

        try {
            Transaction savedTransaction = transactionRepository.save(transaction);
            return transactionConverter.converterToDTO(savedTransaction);
        } catch (Exception e) {
            throw new ValidationException("Erro ao salvar transação ");
        }
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

    public List<TransactionDTO> getTransactionsByCategoryAndDate(String categoryName, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startOfDay = startDate.atStartOfDay(); // Início do dia para a data inicial
        LocalDateTime endOfDay = endDate.atTime(23, 59, 59); // Fim do dia para a data final

        List<Transaction> transactions = transactionRepository.findByCategoryNameAndTransactionDateBetween(categoryName, startOfDay, endOfDay);
        if (transactions.isEmpty()) {
            throw new ValidationException("Não existem transações para a categoria e período especificados!");
        }
        return transactions.stream()
                .map(transactionConverter::converterToDTO)
                .collect(Collectors.toList());
    }


    public List<TransactionDTO> getTransactionsByUserName(String userName) {
        List<Transaction> transactions = transactionRepository.findByUserName(userName);
        if (transactions.isEmpty()) {
            throw new ValidationException("Não existem transações para este usuário! ");
        }
        return transactions.stream()
                .map(transactionConverter::converterToDTO)
                .toList();
    }


}
