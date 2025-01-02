package br.com.wilner.controleFinanceiro.util.converter;

import br.com.wilner.controleFinanceiro.entities.Category.Category;
import br.com.wilner.controleFinanceiro.entities.Transaction.Transaction;
import br.com.wilner.controleFinanceiro.entities.Transaction.TransactionDTO;
import br.com.wilner.controleFinanceiro.entities.User.User;
import br.com.wilner.controleFinanceiro.repositories.CategoryRepository;
import br.com.wilner.controleFinanceiro.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static br.com.wilner.controleFinanceiro.util.UserStatus.ACTIVE;

@Component
@Slf4j
public class TransactionConverter {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public TransactionConverter(UserRepository userRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public Transaction converterToEntity(TransactionDTO dto) {

        return Transaction.builder()
                .transactionType(dto.transactionType())
                .transactionValue(dto.transactionValue())
                .transactionDate(LocalDateTime.now())
                .description(dto.description())
                .user(findUser(dto))
                .category(findCategory(dto))
                .paymentMethod(dto.paymentMethod())
                .build();
    }


    public TransactionDTO converterToDTO(Transaction transaction) {
        return TransactionDTO.builder()

                .transactionType(transaction.getTransactionType())
                .transactionValue(transaction.getTransactionValue())
                .description(transaction.getDescription())
                .userName(transaction.getUser().getName())
                .categoryName(transaction.getCategory().getName())
                .paymentMethod(transaction.getPaymentMethod())
                .build();
    }

    public Transaction converterToEntityUpdate(Transaction transaction, TransactionDTO dto) {
        return Transaction.builder()
                .transactionType(dto.transactionType() != null ? dto.transactionType() : transaction.getTransactionType())
                .transactionValue(dto.transactionValue() != null ? dto.transactionValue() : transaction.getTransactionValue())
                .transactionDate(LocalDateTime.now())
                .description(transaction.getDescription())
                .updateDate(LocalDateTime.now())
                .user(findUser(dto))
                .category(findCategory(dto))
                .paymentMethod(dto.paymentMethod() != null ? dto.paymentMethod() : transaction.getPaymentMethod())
                .build();

    }

    private User findUser(TransactionDTO dto) {
        return userRepository.findByNameAndUserStatus(dto.userName(), ACTIVE)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado: {}", dto.userName());
                    return new RuntimeException("Usuário não encontrado: " + dto.userName());
                });
    }

    private Category findCategory(TransactionDTO dto) {
        return categoryRepository.findByNameAndIsActive(dto.categoryName(), true)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado: {}", dto.categoryName());
                    return new RuntimeException("Categoria não encontrada: " + dto.categoryName());
                });
    }
}
