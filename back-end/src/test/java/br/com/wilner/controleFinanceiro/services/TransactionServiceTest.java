package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.entities.Category.Category;
import br.com.wilner.controleFinanceiro.entities.Transaction.Transaction;
import br.com.wilner.controleFinanceiro.entities.Transaction.TransactionDTO;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import br.com.wilner.controleFinanceiro.repositories.CategoryRepository;
import br.com.wilner.controleFinanceiro.repositories.TransactionRepository;
import br.com.wilner.controleFinanceiro.services.ValidationSerice.TransactionValidationService;
import br.com.wilner.controleFinanceiro.util.converter.TransactionConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static br.com.wilner.controleFinanceiro.builder.TransactionBuilder.umTransaction;
import static br.com.wilner.controleFinanceiro.builder.TransactionDTOBuilder.umTransactionDTO;
import static org.assertj.core.api.Assertions.assertThat;
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


    @Test
    void deveCriarUmaTransacaoComSucesso() {
        TransactionDTO transactionDTO = umTransactionDTO().agora();
        Transaction transaction = umTransaction().agora();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionConverter.converterToDTO(transaction)).thenReturn(transactionDTO);
        when(transactionConverter.converterToEntity(transactionDTO)).thenReturn(transaction);

        // Mock para categoryRepository.findById
        Category category = new Category();
        category.setId(transaction.getCategory().getId());
        category.setValueRealized(BigDecimal.ZERO);
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        TransactionDTO savedTransaction = transactionService.saveTransaction(transactionDTO);

        assertEquals(transactionDTO, savedTransaction);
        assertThat(savedTransaction).isNotNull();
        verify(transactionConverter).converterToDTO(transaction);
        verify(transactionConverter).converterToEntity(transactionDTO);
        verify(transactionRepository).save(transaction);
        verify(categoryRepository).findById(transaction.getCategory().getId());
        verify(categoryRepository).save(category);
    }


    @Test
    void deveDarExcptionCasoOcorraAlgumErroDuranteACriacaoDeUmaTransacao() {
        TransactionDTO transactionDTO = umTransactionDTO().agora();
        Transaction transaction = umTransaction().agora();

        when(transactionConverter.converterToEntity(transactionDTO)).thenReturn(transaction);
        when(transactionRepository.save(any(Transaction.class))).thenThrow(new ValidationException("Erro ao salvar transação "));

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            transactionService.saveTransaction(transactionDTO);
        });

        assertEquals("Erro ao salvar transação ", ex.getMessage());
    }


    @Test
    void nãoDeveAtualizarCasoTransacaoIdNaoExista() {
        Transaction transaction = umTransaction().comId(2L).agora();
        TransactionDTO transactionDTO = umTransactionDTO().agora();

        when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ValidationException.class, () -> {
            transactionService.updateTransaction(transaction.getId(), transactionDTO);
        });

        String expectedMessage = "Transação não encontrada " + transaction.getId();
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deveLancarExceptionCasoOcorraAlgumErroDuranteAtualizacaoTransaction() {
        Transaction transaction = umTransaction().comId(2L).agora();
        TransactionDTO transactionDTO = umTransactionDTO().agora();

        when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(transaction));

        Exception exception = assertThrows(ValidationException.class, () -> {
            transactionService.updateTransaction(transaction.getId(), transactionDTO);
        });

        String expectedMessage = "Falha ao atualizar tranzacao: " + transaction.getId();
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void deveDeletarTransacaoComSucesso() {
        Transaction transaction = umTransaction().agora();

        doNothing().when(transactionRepository).deleteById(transaction.getId());

        assertDoesNotThrow(() -> transactionService.deleteTransaction(transaction.getId()));

        verify(transactionRepository, times(1)).deleteById(transaction.getId());

    }

    @Test
    void deveLancarValidationExceptionQuandoTransacaoNaoEncontrada() {
        Transaction transaction = umTransaction().comId(3L).agora();
        doThrow(new EmptyResultDataAccessException(1)).when(transactionRepository).deleteById(transaction.getId());

        ValidationException exception = assertThrows(ValidationException.class,
                () -> transactionService.deleteTransaction(transaction.getId()));

        assertEquals("Transação não encontrada: " + transaction.getId(), exception.getMessage());
    }

    @Test
    void deveLancarRuntimeExceptionParaOutrosErros() {
        Transaction transaction = umTransaction().comId(3L).agora();
        doThrow(new DataAccessException("Erro genérico") {
        }).when(transactionRepository).deleteById(transaction.getId());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> transactionService.deleteTransaction(transaction.getId()));

        assertEquals("Erro ao deletar transação: " + transaction.getId(), exception.getMessage());
    }

    @Test
    void deveBuscarTransacaoPorId() {
        Transaction transaction = umTransaction().comId(1L).agora();
        TransactionDTO transactionDTO = umTransactionDTO().agora();

        when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(transaction));
        when(transactionConverter.converterToDTO(transaction)).thenReturn(transactionDTO);

        TransactionDTO getTransactionById = transactionService.getTransactionById(transaction.getId());

        assertEquals(transactionDTO, getTransactionById);
        verify(transactionConverter).converterToDTO(transaction);
        verify(transactionRepository).findById(transaction.getId());
        verifyNoMoreInteractions(transactionConverter, transactionRepository);

    }

    @Test
    void deveLancarValidationExceptionQuandoTransacaoIdNaoEncontrado() {
        Transaction transaction = umTransaction().comId(2L).agora();
        when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.empty());

        ValidationException exception = assertThrows(ValidationException.class,
                () -> transactionService.getTransactionById(transaction.getId()));

        assertEquals("Transacao não encontrado " + transaction.getId(), exception.getMessage());
    }

    @Test
    void deveRetornarTransacoesDoUsuario() {
        Long userId = 1L;
        Transaction mockTransaction = umTransaction().agora();
        TransactionDTO mockTransactionDTO = umTransactionDTO().agora();

        List<Transaction> mockTransactions = Collections.singletonList(mockTransaction);

        when(transactionRepository.findByUserId(userId)).thenReturn(mockTransactions);
        when(transactionConverter.converterToDTO(mockTransaction)).thenReturn(mockTransactionDTO);

        List<TransactionDTO> result = transactionService.getTransactionsByUserId(userId);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(mockTransactionDTO, result.get(0));
        verify(transactionRepository).findByUserId(userId);
        verify(transactionConverter).converterToDTO(mockTransaction);
    }

    @Test
    void deveLancarValidationExceptionQuandoNaoExistemTransacoesDoUsuario() {
        Long userId = 1L;

        when(transactionRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        ValidationException exception = assertThrows(ValidationException.class,
                () -> transactionService.getTransactionsByUserId(userId));

        assertEquals("Não existem transações para este usuário! ", exception.getMessage());
        verify(transactionRepository).findByUserId(userId);
    }

    @Test
    void deveRetornarUmaTransacaoComDataECategoria() {
        String categoryName = "Despesas";
        LocalDate startDate = LocalDate.of(2022, Month.JANUARY, 1);
        LocalDate endDate = LocalDate.of(2022, Month.JANUARY, 31);
        List<Transaction> mockTransactions = List.of(new Transaction()); // Substitua pelo construtor real
        List<TransactionDTO> expectedDTOs = List.of(new TransactionDTO()); // Substitua pelo construtor real

        when(transactionRepository.findByCategoryNameAndTransactionDateBetween(eq(categoryName), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(mockTransactions);
        when(transactionConverter.converterToDTO(any(Transaction.class)))
                .thenReturn(expectedDTOs.get(0));

        List<TransactionDTO> result = transactionService.getTransactionsByCategoryAndDate(categoryName, startDate, endDate);

        assertEquals(expectedDTOs.size(), result.size());
        verify(transactionRepository, times(1)).findByCategoryNameAndTransactionDateBetween(eq(categoryName), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(transactionConverter, times(mockTransactions.size())).converterToDTO(any(Transaction.class));
    }

    @Test
    void deveBuscarUmaTransacaoComNomeComSucesso() {
        String userName = "UsuarioTeste";
        List<Transaction> mockTransactions = List.of(new Transaction()); // Substitua pelo construtor real
        List<TransactionDTO> expectedDTOs = List.of(new TransactionDTO()); // Substitua pelo construtor real

        when(transactionRepository.findByUserName(userName)).thenReturn(mockTransactions);
        when(transactionConverter.converterToDTO(any(Transaction.class))).thenReturn(expectedDTOs.get(0));

        List<TransactionDTO> result = transactionService.getTransactionsByUserName(userName);

        assertEquals(expectedDTOs.size(), result.size());
        verify(transactionRepository, times(1)).findByUserName(userName);
        verify(transactionConverter, times(mockTransactions.size())).converterToDTO(any(Transaction.class));
    }

    @Test
    void shouldThrowValidationExceptionWhenNoTransactionsFoundByCategoryAndDate() {
        String categoryName = "Despesas";
        LocalDate startDate = LocalDate.of(2022, Month.JANUARY, 1);
        LocalDate endDate = LocalDate.of(2022, Month.JANUARY, 31);

        when(transactionRepository.findByCategoryNameAndTransactionDateBetween(eq(categoryName), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList()); // Retorna uma lista vazia para simular "nenhuma transação encontrada"

        ValidationException exception = assertThrows(ValidationException.class, () ->
                        transactionService.getTransactionsByCategoryAndDate(categoryName, startDate, endDate),
                "Esperava que ValidationException fosse lançada"
        );

        assertEquals("Não existem transações para a categoria e período especificados!", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenNoTransactionsFoundByUserName() {
        String userName = "UsuarioTeste";

        when(transactionRepository.findByUserName(userName)).thenReturn(Collections.emptyList()); // Retorna uma lista vazia para simular "nenhuma transação encontrada"

        ValidationException exception = assertThrows(ValidationException.class, () ->
                        transactionService.getTransactionsByUserName(userName),
                "Esperava que ValidationException fosse lançada"
        );

        assertEquals("Não existem transações para este usuário! ", exception.getMessage());
    }


}

