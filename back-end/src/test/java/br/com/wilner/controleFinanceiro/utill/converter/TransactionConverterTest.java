package br.com.wilner.controleFinanceiro.utill.converter;

import br.com.wilner.controleFinanceiro.DTO.TransactionDTO;
import br.com.wilner.controleFinanceiro.entities.Transaction;
import br.com.wilner.controleFinanceiro.entities.User;
import br.com.wilner.controleFinanceiro.repositories.UserRepository;
import br.com.wilner.controleFinanceiro.util.converter.TransactionConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static br.com.wilner.controleFinanceiro.builder.TransactionBuilder.umTransaction;
import static br.com.wilner.controleFinanceiro.builder.TransactionDTOBuilder.umTransactionDTO;
import static br.com.wilner.controleFinanceiro.builder.UserBuilder.umUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionConverterTest {

    @InjectMocks
    TransactionConverter transactionConverter;
    @Mock
    private TransactionDTO transactionDTO;
    @Mock
    private Transaction transaction;
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        transactionDTO = umTransactionDTO().agora();
        transaction = umTransaction().agora();
    }


    @Test
    void deveConverterParaTransactionEntityComSucesso() {

        User userBuilder = umUser().agora();
        when(userRepository.findById(userBuilder.getId())).thenReturn(Optional.of(userBuilder));

        Transaction transactionToEntity = transactionConverter.converterToEntity(transactionDTO);

        assertAll("TransactionEntity",
                () -> assertEquals(transaction.getId(), transactionToEntity.getId()),
                () -> assertEquals(transaction.getTransactionType(), transactionToEntity.getTransactionType()),
                () -> assertEquals(transaction.getTransactionValue(), transactionToEntity.getTransactionValue()),
                () -> assertEquals(transaction.getTransactionDate().truncatedTo(ChronoUnit.MINUTES), transactionToEntity.getTransactionDate().truncatedTo(ChronoUnit.MINUTES)),
                () -> assertEquals(transaction.getDescription(), transactionToEntity.getDescription()),
                () -> assertEquals(transaction.getUser(), transactionToEntity.getUser()),
                () -> assertEquals(transaction.getPaymentMethod(), transactionToEntity.getPaymentMethod())

        );

        assertNotNull(transactionToEntity.getId());
        assertNotNull(transactionToEntity.getTransactionType());
    }

    @Test
    void deveConverterParaUserEntityUpdateComSucesso() {

        transactionDTO = umTransactionDTO().comTransactionType("TesteNovo").agora();
        Transaction transactionEsperado = umTransaction().comTransactionType("TesteNovo").agora();
        transaction = umTransaction().comTransactionType("outro nome").agora();
        User userBuilder = umUser().agora();

        when(userRepository.findById(userBuilder.getId())).thenReturn(Optional.of(userBuilder));

        Transaction transactionToEntityUpdate = transactionConverter.converterToEntityUpdate(transaction, transactionDTO);

        assertAll("TransactionEntityUpdate",
                () -> assertEquals(transactionEsperado.getId(), transactionToEntityUpdate.getId()),
                () -> assertEquals(transactionEsperado.getTransactionType(), transactionToEntityUpdate.getTransactionType()),
                () -> assertEquals(transactionEsperado.getTransactionValue(), transactionToEntityUpdate.getTransactionValue()),
                () -> assertEquals(transactionEsperado.getDescription(), transactionToEntityUpdate.getDescription()),
                () -> assertEquals(transactionEsperado.getUser(), transactionToEntityUpdate.getUser()),
                () -> assertEquals(transactionEsperado.getPaymentMethod(), transactionToEntityUpdate.getPaymentMethod())


        );

        assertNotNull(transactionToEntityUpdate.getUpdateDate());

    }

    @Test
    void deveConverterParatransactionDTOComSucesso() {

        User userBuilder = umUser().agora();

        TransactionDTO transactionToDTO = transactionConverter.converterToDTO(transaction);

        assertAll("TransactionDTO",
                () -> assertEquals(transactionDTO.getId(), transactionToDTO.getId()),
                () -> assertEquals(transactionDTO.getTransactionType(), transactionToDTO.getTransactionType()),
                () -> assertEquals(transactionDTO.getTransactionValue(), transactionToDTO.getTransactionValue()),
                () -> assertEquals(transactionDTO.getDescription(), transactionToDTO.getDescription()),
                () -> assertEquals(transactionDTO.getUserId(), transactionToDTO.getUserId()),
                () -> assertEquals(transactionDTO.getPaymentMethod(), transactionToDTO.getPaymentMethod())

        );

        assertNotNull(transactionDTO.getId());
        assertNotNull(transactionDTO.getTransactionType());
    }

}
