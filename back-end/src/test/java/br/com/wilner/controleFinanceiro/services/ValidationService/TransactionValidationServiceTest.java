package br.com.wilner.controleFinanceiro.services.ValidationService;

import br.com.wilner.controleFinanceiro.entities.Transaction.Transaction;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import br.com.wilner.controleFinanceiro.services.ValidationSerice.TransactionValidationService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static br.com.wilner.controleFinanceiro.builder.TransactionBuilder.umTransaction;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TransactionValidationServiceTest {
    @InjectMocks
    private TransactionValidationService transactionValidationService;

    static Stream<Arguments> dataProvider() {
        return Stream.of(
                Arguments.of("", BigDecimal.valueOf(100), LocalDateTime.now(), "CASH", "Tipo da transação inexistente"),
                Arguments.of("DEPOSIT", null, LocalDateTime.now(), "CASH", "Valor da transação inexistente"),
                Arguments.of("DEPOSIT", BigDecimal.valueOf(100), null, "CASH", "Data da transação inexistente"),
                Arguments.of("DEPOSIT", BigDecimal.valueOf(100), LocalDateTime.now(), "", "Forma de pagamento inexistente")
        );
    }

    @ParameterizedTest(name = "[{index}] - {4}")
    @MethodSource("dataProvider")
    void deveValidarCamposObrigatoriosAoSalvar(String transactionType, BigDecimal transactionValue, LocalDateTime transactionDate,
                                               String paymentMethod, String expectedMessage) {

        Transaction transaction = umTransaction().comTransactionType(transactionType).comTransactionValue(transactionValue)
                .comTransactionDate(transactionDate).comPaymentMethod(paymentMethod).agora();


        ValidationException exception = assertThrows(ValidationException.class, () ->
                transactionValidationService.checkValidFields(transaction));

        assertEquals(expectedMessage, exception.getMessage());
    }
}



