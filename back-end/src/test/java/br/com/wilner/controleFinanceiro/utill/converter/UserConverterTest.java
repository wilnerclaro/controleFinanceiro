package br.com.wilner.controleFinanceiro.utill.converter;

import br.com.wilner.controleFinanceiro.entities.User.User;
import br.com.wilner.controleFinanceiro.entities.User.UserDTO;
import br.com.wilner.controleFinanceiro.util.UserStatus;
import br.com.wilner.controleFinanceiro.util.converter.UserConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class UserConverterTest {

    @InjectMocks
    private UserConverter userConverter;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Teste", "teste@teste.com", UserStatus.ACTIVE, LocalDateTime.now(), null, null);
        userDTO = new UserDTO("Teste", "teste@teste.com");
    }

    @Test
    void deveConverterDeDTOParaEntidade() {
        User result = userConverter.converterToEntity(userDTO);

        assertNotNull(result);
        assertEquals(userDTO.name(), result.getName());
        assertEquals(userDTO.email(), result.getEmail());
    }

    @Test
    void deveConverterDeEntidadeParaDTO() {
        UserDTO result = userConverter.converterToDTO(user);

        assertNotNull(result);
        assertEquals(user.getName(), result.name());
        assertEquals(user.getEmail(), result.email());
    }

}
