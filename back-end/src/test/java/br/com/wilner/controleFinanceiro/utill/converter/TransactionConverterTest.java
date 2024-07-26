package br.com.wilner.controleFinanceiro.utill.converter;

import br.com.wilner.controleFinanceiro.DTO.TransactionDTO;
import br.com.wilner.controleFinanceiro.entities.Category.Category;
import br.com.wilner.controleFinanceiro.entities.Transaction.Transaction;
import br.com.wilner.controleFinanceiro.entities.User.User;
import br.com.wilner.controleFinanceiro.repositories.CategoryRepository;
import br.com.wilner.controleFinanceiro.repositories.UserRepository;
import br.com.wilner.controleFinanceiro.util.converter.TransactionConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static br.com.wilner.controleFinanceiro.builder.CategoryBuilder.umCategory;
import static br.com.wilner.controleFinanceiro.builder.TransactionBuilder.umTransaction;
import static br.com.wilner.controleFinanceiro.builder.TransactionDTOBuilder.umTransactionDTO;
import static br.com.wilner.controleFinanceiro.builder.UserBuilder.umUser;
import static br.com.wilner.controleFinanceiro.util.UserStatus.ACTIVE;
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

    @Mock
    private CategoryRepository categoryRepository;


    @Test
    void deveConverterParaTransactionEntityComSucesso() {

        User userBuilder = umUser().comName("Teste").agora();
        Category categoryBuilder = umCategory().comName("Moradia").agora();

        TransactionDTO transactionDTO = umTransactionDTO().comUserName(userBuilder.getName()).comCategoryName(categoryBuilder.getName()).agora();
        transaction = umTransaction().agora();

        when(userRepository.findByNameAndUserStatus(userBuilder.getName(), ACTIVE)).thenReturn(Optional.of(userBuilder));
        when(categoryRepository.findByNameAndIsActive(categoryBuilder.getName(), true)).thenReturn(Optional.of(categoryBuilder));


        Transaction transactionToEntity = transactionConverter.converterToEntity(transactionDTO);

        assertAll("TransactionEntity",
                () -> assertEquals(transaction.getTransactionType(), transactionToEntity.getTransactionType()),
                () -> assertEquals(transaction.getTransactionValue(), transactionToEntity.getTransactionValue()),
                () -> assertEquals(transaction.getTransactionDate().truncatedTo(ChronoUnit.MINUTES), transactionToEntity.getTransactionDate().truncatedTo(ChronoUnit.MINUTES)),
                () -> assertEquals(transaction.getDescription(), transactionToEntity.getDescription()),
                () -> assertEquals(transaction.getUser().getId(), transactionToEntity.getUser().getId()),
                () -> assertEquals(transaction.getPaymentMethod(), transactionToEntity.getPaymentMethod())

        );

        assertNotNull(transactionToEntity.getTransactionType());
        assertNotNull(transactionToEntity.getTransactionType());
    }

    @Test
    void deveConverterParaUserEntityUpdateComSucesso() {

        transactionDTO = umTransactionDTO().comTransactionType("TesteNovo").agora();
        Category categoryBuilder = umCategory().comName("Moradia").agora();

        Transaction transactionEsperado = umTransaction().comTransactionType("TesteNovo").agora();
        transaction = umTransaction().comTransactionType("outro nome").agora();
        User userBuilder = umUser().agora();

        when(userRepository.findByNameAndUserStatus(userBuilder.getName(), ACTIVE)).thenReturn(Optional.of(userBuilder));
        when(categoryRepository.findByNameAndIsActive(categoryBuilder.getName(), true)).thenReturn(Optional.of(categoryBuilder));


        Transaction transactionToEntityUpdate = transactionConverter.converterToEntityUpdate(transaction, transactionDTO);

        assertAll("TransactionEntityUpdate",
                () -> assertEquals(transactionEsperado.getTransactionType(), transactionToEntityUpdate.getTransactionType()),
                () -> assertEquals(transactionEsperado.getTransactionValue(), transactionToEntityUpdate.getTransactionValue()),
                () -> assertEquals(transactionEsperado.getDescription(), transactionToEntityUpdate.getDescription()),
                () -> assertEquals(transactionEsperado.getUser().getId(), transactionToEntityUpdate.getUser().getId()),
                () -> assertEquals(transactionEsperado.getPaymentMethod(), transactionToEntityUpdate.getPaymentMethod())


        );

        assertNotNull(transactionToEntityUpdate.getUpdateDate());

    }

    @Test
    void deveConverterParatransactionDTOComSucesso() {

        User userBuilder = umUser().agora();
        Category categoryBuilder = umCategory().agora();

        TransactionDTO transactionDTO = umTransactionDTO().agora();
        transaction = umTransaction().agora();

        TransactionDTO transactionToDTO = transactionConverter.converterToDTO(transaction);

        assertAll("TransactionDTO",
                () -> assertEquals(transactionDTO.getTransactionType(), transactionToDTO.getTransactionType()),
                () -> assertEquals(transactionDTO.getTransactionValue(), transactionToDTO.getTransactionValue()),
                () -> assertEquals(transactionDTO.getDescription(), transactionToDTO.getDescription()),
                () -> assertEquals(transactionDTO.getUserName(), transactionToDTO.getUserName()),
                () -> assertEquals(transactionDTO.getCategoryName(), transactionToDTO.getCategoryName()),
                () -> assertEquals(transactionDTO.getPaymentMethod(), transactionToDTO.getPaymentMethod())

        );

        assertNotNull(transactionDTO.getUserName());
        assertNotNull(transactionDTO.getTransactionType());
    }

}
