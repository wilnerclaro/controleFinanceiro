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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static br.com.wilner.controleFinanceiro.builder.TransactionBuilder.umTransaction;
import static br.com.wilner.controleFinanceiro.builder.TransactionDTOBuilder.umTransactionDTO;
import static br.com.wilner.controleFinanceiro.builder.UserBuilder.umUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionConverter transactionConverter;

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

}

