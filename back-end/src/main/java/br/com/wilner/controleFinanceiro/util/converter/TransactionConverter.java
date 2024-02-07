package br.com.wilner.controleFinanceiro.util.converter;

import br.com.wilner.controleFinanceiro.DTO.TransactionDTO;
import br.com.wilner.controleFinanceiro.entities.Transaction;
import br.com.wilner.controleFinanceiro.entities.User;
import br.com.wilner.controleFinanceiro.repositories.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TransactionConverter {

    private final UserRepository userRepository;

    public TransactionConverter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Transaction converterToEntity(TransactionDTO dto) {

        return Transaction.builder()
                .id(dto.getId())
                .transactionType(dto.getTransactionType())
                .transactionValue(dto.getTransactionValue())
                .transactionDate(LocalDateTime.now())
                .description(dto.getDescription())
                .user(findUser(dto))
                .paymentMethod(dto.getPaymentMethod())
                .build();
    }


    public TransactionDTO converterToDTO(Transaction transaction) {
        return TransactionDTO.builder()
                .id(transaction.getId())
                .transactionType(transaction.getTransactionType())
                .transactionValue(transaction.getTransactionValue())
                .description(transaction.getDescription())
                .userId(transaction.getUser().getId())
                .paymentMethod(transaction.getPaymentMethod())
                .build();
    }

    public Transaction converterToEntityUpdate(Transaction transaction, TransactionDTO dto) {
        return Transaction.builder()
                .id(dto.getId() != null ? dto.getId() : transaction.getId())
                .transactionType(dto.getTransactionType() != null ? dto.getTransactionType() : transaction.getTransactionType())
                .transactionValue(dto.getTransactionValue() != null ? dto.getTransactionValue() : transaction.getTransactionValue())
                .transactionDate(LocalDateTime.now())
                .description(transaction.getDescription())
                .updateDate(LocalDateTime.now())
                .user(findUser(dto))
                .paymentMethod(dto.getPaymentMethod() != null ? dto.getPaymentMethod() : transaction.getPaymentMethod())
                .build();

    }

    private User findUser(TransactionDTO dto) {
        return userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + dto.getUserId()));
    }
}
