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
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + dto.getUserId()));

        return Transaction.builder()
                .id(dto.getId())
                .transactionType(dto.getTransactionType())
                .transactionValue(dto.getTransactionValue())
                .transactionDate(LocalDateTime.now())
                .description(dto.getDescription())
                .user(user)
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
}
