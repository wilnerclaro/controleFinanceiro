package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.entities.Category.Category;
import br.com.wilner.controleFinanceiro.entities.Transaction.Transaction;
import br.com.wilner.controleFinanceiro.entities.Transaction.TransactionDTO;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import br.com.wilner.controleFinanceiro.repositories.CategoryRepository;
import br.com.wilner.controleFinanceiro.repositories.TransactionRepository;
import br.com.wilner.controleFinanceiro.services.ValidationSerice.TransactionValidationService;
import br.com.wilner.controleFinanceiro.util.converter.TransactionConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionConverter transactionConverter;
    @Mock
    private TransactionValidationService transactionValidationService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private Category category;

    private Transaction transaction;
    private TransactionDTO transactionDTO;

    @BeforeEach
    void setUp() {
        transaction = new Transaction(1L, "Despesa", new BigDecimal("100.00"), LocalDateTime.now(), LocalDateTime.now(), null
                , "Pagamento", null, "Cartão de Crédito");
        transactionDTO = new TransactionDTO("Despesa", new BigDecimal("100.00"), "Pagamento", "Cartão de Crédito", null, "Pix");
    }

    @Test
    void deveSalvarTransacaoComSucesso() {
        // Criação de um mock para a categoria
        Category category = new Category(11L, "Lazer", "Categoria de Lazer", LocalDateTime.now(), LocalDateTime.now(),
                null, true, new BigDecimal("300"), new BigDecimal("300"));

        transaction.setCategory(category);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        when(transactionRepository.findByCategoryId(anyLong())).thenReturn(Collections.singletonList(transaction));

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionConverter.converterToDTO(any(Transaction.class))).thenReturn(transactionDTO);
        when(transactionConverter.converterToEntity(any(TransactionDTO.class))).thenReturn(transaction);

        TransactionDTO result = transactionService.saveTransaction(transactionDTO);

        assertNotNull(result);
        assertEquals(transactionDTO.transactionType(), result.transactionType());
        verify(transactionRepository).save(any(Transaction.class));
        verify(categoryRepository).findById(anyLong());
        verify(transactionRepository).findByCategoryId(anyLong());
    }


    @Test
    void deveLancarExceptionAoSalvarTransacaoInvalida() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            transactionService.saveTransaction(transactionDTO);
        });

        assertEquals("Erro ao salvar transação ", exception.getMessage());
    }

    @Test
    void deveBuscarTransacoesPorCategoriaEPeriodo() {
        List<Transaction> transactions = Collections.singletonList(transaction);
        when(transactionRepository.findByCategoryNameAndTransactionDateBetween(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(transactions);
        when(transactionConverter.converterToDTO(any(Transaction.class))).thenReturn(transactionDTO);

        List<TransactionDTO> result = transactionService.getTransactionsByCategoryAndDate("Lazer", LocalDate.now(), LocalDate.now());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(transactionRepository).findByCategoryNameAndTransactionDateBetween(anyString(), any(LocalDateTime.class), any(LocalDateTime.class));
    }


}

