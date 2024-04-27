package br.com.wilner.controleFinanceiro.util.converter;

import br.com.wilner.controleFinanceiro.DTO.TransactionDTO;
import br.com.wilner.controleFinanceiro.entities.Category;
import br.com.wilner.controleFinanceiro.entities.Transaction;
import br.com.wilner.controleFinanceiro.entities.User;
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
                .transactionValueExpected(dto.getTransactionValueExpected())
                .transactionValueRealized(dto.getTransactionValue())
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
                .transactionValueRealized(transaction.getTransactionValueRealized())
                .transactionValueExpected(transaction.getTransactionValueExpected())
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
                .transactionValueRealized(dto.getTransactionValueRealized() != null ? dto.getTransactionValueRealized() : transaction.getTransactionValueRealized())
                .transactionValueExpected(dto.getTransactionValueExpected() != null ? dto.getTransactionValueExpected() : transaction.getTransactionValueExpected())
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
