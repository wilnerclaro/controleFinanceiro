package br.com.wilner.controleFinanceiro.utill.converter;

import br.com.wilner.controleFinanceiro.DTO.UserDTO;
import br.com.wilner.controleFinanceiro.entities.User;
import br.com.wilner.controleFinanceiro.util.converter.UserConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static br.com.wilner.controleFinanceiro.builder.UserBuilder.umUser;
import static br.com.wilner.controleFinanceiro.builder.UserDTOBuilder.umUserDTO;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)

public class UserConverterTest {

    @InjectMocks
    UserConverter userConverter;

    private UserDTO userDTO;

    private User user;

    @Test
    void deveConverterParaUserEntityComSucesso() {
        userDTO = umUserDTO().agora();
        user = umUser().agora();

        User userToEntity = userConverter.converterToEntity(userDTO);

        assertAll("UsuarioEntity",
                () -> assertEquals(user.getId(), userToEntity.getId()),
                () -> assertEquals(user.getName(), userToEntity.getName()),
                () -> assertEquals(user.getEmail(), userToEntity.getEmail()),
                () -> assertEquals(user.getUserStatus(), userToEntity.getUserStatus())

        );

        assertNotNull(userToEntity.getId());
        assertNotNull(userToEntity.getDataCriacao());
    }

    @Test
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
    }

}
