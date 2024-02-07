package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.DTO.TransactionDTO;
import br.com.wilner.controleFinanceiro.entities.Transaction;
import br.com.wilner.controleFinanceiro.entities.User;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import br.com.wilner.controleFinanceiro.repositories.TransactionRepository;
import br.com.wilner.controleFinanceiro.util.converter.TransactionConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static br.com.wilner.controleFinanceiro.builder.TransactionBuilder.umTransaction;
import static br.com.wilner.controleFinanceiro.builder.TransactionDTOBuilder.umTransactionDTO;
import static br.com.wilner.controleFinanceiro.builder.UserBuilder.umUser;
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


    //TODO Listar Transações por Categoria (Retorna transações filtradas por uma categoria específica.)
    @Test
    void deveCriarUmaTransacaoComSucesso() {
        TransactionDTO transactionDTO = umTransactionDTO().agora();
        Transaction transaction = umTransaction().agora();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionConverter.converterToDTO(transaction)).thenReturn(transactionDTO);
        when(transactionConverter.converterToEntity(transactionDTO)).thenReturn(transaction);

        TransactionDTO savedTransaction = transactionService.saveTransaction(transactionDTO);

        assertEquals(transactionDTO, savedTransaction);
        assertThat(savedTransaction).isNotNull();
        verify(transactionConverter).converterToDTO(transaction);
        verify(transactionConverter).converterToEntity(transactionDTO);
        verify(transactionRepository).save(transaction);
        verifyNoMoreInteractions(transactionConverter, transactionRepository);

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


    @ParameterizedTest(name = "[{index}] - {7}")
    @MethodSource(value = "dataProvider")
    void deveValidarCamposObrigatoriosAoSalvar(Long id, String transactionType, BigDecimal transactionValue, LocalDateTime transactionDate,
                                               String description, User user, String paymentMethod, String message) {

        String exMessage = assertThrows(ValidationException.class, () -> {
            TransactionDTO transactionDTO = umTransactionDTO().agora();
            Transaction transaction = umTransaction().comId(id)
                    .comTransactionType(transactionType)
                    .comTransactionValue(transactionValue)
                    .comTransactionDate(transactionDate)
                    .comDescription(description)
                    .comUser(user)
                    .comPaymentMethod(paymentMethod).agora();
            when(transactionConverter.converterToEntity(transactionDTO)).thenReturn(transaction);

            transactionService.saveTransaction(transactionDTO);
        }).getMessage();

        assertEquals(message, exMessage);
    }

    static Stream<Arguments> dataProvider() {
        return Stream.of(
                Arguments.of(1L, null, BigDecimal.valueOf(100), LocalDateTime.now(), "Pizza", umUser().agora(), "Pix", "Tipo da transação inexistente"),
                Arguments.of(1L, "Despesa", null, LocalDateTime.now(), "Pizza", umUser().agora(), "Pix", "Valor da transação inexistente"),
                Arguments.of(1L, "Despesa", BigDecimal.valueOf(100), null, "Pizza", umUser().agora(), "Pix", "Data da transação inexistente"),
                Arguments.of(1L, "Despesa", BigDecimal.valueOf(100), LocalDateTime.now(), "Pizza", umUser().agora(), null, "Forma de pagamento inexistente")
        );
    }

    @Test
    void deveAtualizarUmaTransacaoComsucesso() {
        Transaction existTransaction = umTransaction().comId(1L).comTransactionType("Despesa").comDescription("Cerveja").agora();
        TransactionDTO updateTransactionDTO = umTransactionDTO().comId(1L).comTransactionType("Receita").comDescription("Aluguel").agora();

        when(transactionRepository.findById(existTransaction.getId())).thenReturn(Optional.ofNullable(existTransaction));
        when(transactionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionConverter.converterToEntityUpdate(any(Transaction.class), any(TransactionDTO.class)))
                .thenAnswer(invocation -> {
                    Transaction transactionToUpdate = invocation.getArgument(0, Transaction.class);
                    TransactionDTO transactionUpdatedData = invocation.getArgument(1, TransactionDTO.class);
                    transactionToUpdate.setTransactionType(transactionUpdatedData.getTransactionType());
                    transactionToUpdate.setDescription(transactionUpdatedData.getDescription());
                    return transactionToUpdate;
                });

        when(transactionConverter.converterToDTO(existTransaction)).thenReturn(updateTransactionDTO);

        TransactionDTO result = transactionService.updateTransaction(existTransaction.getId(), updateTransactionDTO);

        assertNotNull(existTransaction);
        assertNotNull(result);
        assertEquals(updateTransactionDTO.getTransactionType(), result.getTransactionType());
        assertEquals(updateTransactionDTO.getDescription(), result.getDescription());


        verify(transactionRepository, times(1)).findById(existTransaction.getId());
        verify(transactionRepository, times(1)).save(existTransaction);

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
        when(transactionConverter.converterToEntityUpdate(transaction, transactionDTO)).thenReturn(transaction);
        when(transactionRepository.save(transaction)).thenThrow(new RuntimeException("Falha ao atualizar tranzacao: "));

        Exception exception = assertThrows(RuntimeException.class, () -> {
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


}

