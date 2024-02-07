package br.com.wilner.controleFinanceiro.utill.converter;

import br.com.wilner.controleFinanceiro.DTO.TransactionDTO;
import br.com.wilner.controleFinanceiro.entities.Transaction;
import br.com.wilner.controleFinanceiro.entities.User;
import br.com.wilner.controleFinanceiro.repositories.UserRepository;
import br.com.wilner.controleFinanceiro.util.converter.TransactionConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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


    @Test
    void deveConverterParaTransactionEntityComSucesso() {
        transactionDTO = umTransactionDTO().agora();
        transaction = umTransaction().agora();
        User userBuilder = umUser().agora();
        when(userRepository.findById(userBuilder.getId())).thenReturn(Optional.of(userBuilder));

        Transaction transactionToEntity = transactionConverter.converterToEntity(transactionDTO);

        assertAll("TransactionEntity",
                () -> assertEquals(transaction.getId(), transactionToEntity.getId()),
                () -> assertEquals(transaction.getTransactionType(), transactionToEntity.getTransactionType()),
                () -> assertEquals(transaction.getTransactionValue(), transactionToEntity.getTransactionValue()),
                () -> assertEquals(transaction.getTransactionDate(), transactionToEntity.getTransactionDate()),
                () -> assertEquals(transaction.getDescription(), transactionToEntity.getDescription()),
                () -> assertEquals(transaction.getUser(), transactionToEntity.getUser()),
                () -> assertEquals(transaction.getPaymentMethod(), transactionToEntity.getPaymentMethod())

        );

        assertNotNull(transactionToEntity.getId());
        assertNotNull(transactionToEntity.getTransactionType());
    }

  /*  @Test
    void deveConverterParaUserEntityUpdateComSucesso() {

        userDTO = umUserDTO().comName("TesteNovo").agora();
        User userEsperado = umUser().comName("TesteNovo").agora();
        user = umUser().comName("outro nome").agora();

        User userToEntityUpdate = userConverter.converterToEntityUpdate(user, userDTO);

        assertAll("UsuarioEntityUpdate",
                () -> assertEquals(userEsperado.getId(), userToEntityUpdate.getId()),
                () -> assertEquals(userEsperado.getName(), userToEntityUpdate.getName()),
                () -> assertEquals(userEsperado.getEmail(), userToEntityUpdate.getEmail()),
                () -> assertEquals(userEsperado.getUserStatus(), userToEntityUpdate.getUserStatus()),
                () -> assertEquals(userEsperado.getDataCriacao(), userToEntityUpdate.getDataCriacao())

        );

        assertNotNull(userToEntityUpdate.getDataAtualizacao());

    }

    @Test
    void deveConverterParaUserDTOComSucesso() {
        userDTO = umUserDTO().agora();
        user = umUser().agora();

        UserDTO userToDTO = userConverter.converterToDTO(user);

        assertAll("UsuarioEntity",
                () -> assertEquals(userDTO.getId(), userToDTO.getId()),
                () -> assertEquals(userDTO.getName(), userToDTO.getName()),
                () -> assertEquals(userDTO.getEmail(), userToDTO.getEmail()),
                () -> assertEquals(userDTO.getUserStatus(), userToDTO.getUserStatus())

        );

        assertNotNull(userToDTO.getId());
    }*/

}
