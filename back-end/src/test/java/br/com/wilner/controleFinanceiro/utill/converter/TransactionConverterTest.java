package br.com.wilner.controleFinanceiro.utill.converter;

import br.com.wilner.controleFinanceiro.entities.Category.Category;
import br.com.wilner.controleFinanceiro.entities.Transaction.Transaction;
import br.com.wilner.controleFinanceiro.entities.Transaction.TransactionDTO;
import br.com.wilner.controleFinanceiro.entities.User.User;
import br.com.wilner.controleFinanceiro.repositories.CategoryRepository;
import br.com.wilner.controleFinanceiro.repositories.UserRepository;
import br.com.wilner.controleFinanceiro.util.UserStatus;
import br.com.wilner.controleFinanceiro.util.converter.TransactionConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionConverterTest {

    @InjectMocks
    private TransactionConverter transactionConverter;

    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private User user;
    @Mock
    private Category category;

    private Transaction transaction;
    private TransactionDTO transactionDTO;

    @BeforeEach
    void setUp() {
        transaction = new Transaction(1L, "Despesa", new BigDecimal("100.00"), LocalDateTime.now(), LocalDateTime.now(), null, "Pagamento", null, "Cartão de Crédito");
        transactionDTO = new TransactionDTO("Despesa", new BigDecimal("100.00"), "Pagamento", "Cartão de Crédito", "Teste", "Pix");
    }

    @Test
    void deveConverterDeDTOParaEntidade() {
        when(userRepository.findByNameAndUserStatus(anyString(), any(UserStatus.class))).thenReturn(Optional.of(new User()));
        when(categoryRepository.findByNameAndIsActive(anyString(), anyBoolean())).thenReturn(Optional.of(new Category()));

        Transaction result = transactionConverter.converterToEntity(transactionDTO);

        assertNotNull(result);
        assertEquals(transactionDTO.transactionType(), result.getTransactionType());
        assertEquals(transactionDTO.transactionValue(), result.getTransactionValue());
    }

    @Test
    void deveConverterDeEntidadeParaDTO() {
        User user = new User(1L, "Teste", "teste@teste.com", UserStatus.ACTIVE, LocalDateTime.now(), null, null);
        Category category = new Category(1L, "Lazer", "Categoria de Lazer", LocalDateTime.now(), LocalDateTime.now(),
                null, true, new BigDecimal("300"), new BigDecimal("300"));
        transaction.setUser(user);
        transaction.setCategory(category);
        TransactionDTO result = transactionConverter.converterToDTO(transaction);

        assertNotNull(result);
        assertEquals(transaction.getTransactionType(), result.transactionType());
        assertEquals(transaction.getTransactionValue(), result.transactionValue());
        assertEquals(transaction.getDescription(), result.description());
    }

}
