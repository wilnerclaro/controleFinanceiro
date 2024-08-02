package br.com.wilner.controleFinanceiro.util.converter;

import br.com.wilner.controleFinanceiro.entities.Transaction.TransactionDTO;
import br.com.wilner.controleFinanceiro.entities.Category.Category;
import br.com.wilner.controleFinanceiro.entities.Transaction.Transaction;
import br.com.wilner.controleFinanceiro.entities.User.User;
import br.com.wilner.controleFinanceiro.repositories.CategoryRepository;
import br.com.wilner.controleFinanceiro.repositories.UserRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static br.com.wilner.controleFinanceiro.util.UserStatus.ACTIVE;

@Component
public class TransactionConverter {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public TransactionConverter(UserRepository userRepository, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public Transaction converterToEntity(TransactionDTO dto) {

        return Transaction.builder()
                .transactionType(dto.getTransactionType())
                .transactionValue(dto.getTransactionValue())
                .transactionDate(LocalDateTime.now())
                .description(dto.getDescription())
                .user(findUser(dto))
                .category(findCategory(dto))
                .paymentMethod(dto.getPaymentMethod())
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
                .transactionType(dto.getTransactionType() != null ? dto.getTransactionType() : transaction.getTransactionType())
                .transactionValue(dto.getTransactionValue() != null ? dto.getTransactionValue() : transaction.getTransactionValue())
                .transactionDate(LocalDateTime.now())
                .description(transaction.getDescription())
                .updateDate(LocalDateTime.now())
                .user(findUser(dto))
                .category(findCategory(dto))
                .paymentMethod(dto.getPaymentMethod() != null ? dto.getPaymentMethod() : transaction.getPaymentMethod())
                .build();

    }

    private User findUser(TransactionDTO dto) {
        return userRepository.findByNameAndUserStatus(dto.getUserName(), ACTIVE)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + dto.getUserName()));
    }

    private Category findCategory(TransactionDTO dto) {
        return categoryRepository.findByNameAndIsActive(dto.getCategoryName(), true)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada: " + dto.getCategoryName()));
    }
}
