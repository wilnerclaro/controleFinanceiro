package br.com.wilner.controleFinanceiro.utill.converter;

import br.com.wilner.controleFinanceiro.DTO.UserDTO;
import br.com.wilner.controleFinanceiro.entities.User;
import br.com.wilner.controleFinanceiro.util.converter.UserConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.temporal.ChronoUnit;

import static br.com.wilner.controleFinanceiro.builder.UserBuilder.umUser;
import static br.com.wilner.controleFinanceiro.builder.UserDTOBuilder.umUserDTO;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserConverterTest {

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
                () -> assertEquals(user.getName(), userToEntity.getName()),
                () -> assertEquals(user.getEmail(), userToEntity.getEmail()),
                () -> assertEquals(user.getUserStatus(), userToEntity.getUserStatus())

        );

        assertNotNull(userToEntity.getName());
        assertNotNull(userToEntity.getDataCriacao());
    }

    @Test
    void deveConverterParaUserEntityUpdateComSucesso() {

        userDTO = umUserDTO().comName("TesteNovo").agora();
        User userEsperado = umUser().comName("TesteNovo").agora();
        user = umUser().comName("outro nome").agora();

        User userToEntityUpdate = userConverter.converterToEntityUpdate(user, userDTO);

        assertAll("UsuarioEntityUpdate",
                () -> assertEquals(userEsperado.getName(), userToEntityUpdate.getName()),
                () -> assertEquals(userEsperado.getEmail(), userToEntityUpdate.getEmail()),
                () -> assertEquals(userEsperado.getUserStatus(), userToEntityUpdate.getUserStatus()),
                () -> assertEquals(userEsperado.getDataCriacao().truncatedTo(ChronoUnit.MINUTES), userToEntityUpdate.getDataCriacao().truncatedTo(ChronoUnit.MINUTES))

        );

        assertNotNull(userToEntityUpdate.getDataAtualizacao());

    }

    @Test
    void deveConverterParaUserDTOComSucesso() {
        userDTO = umUserDTO().agora();
        user = umUser().agora();

        UserDTO userToDTO = userConverter.converterToDTO(user);

        assertAll("UsuarioDTO",
                () -> assertEquals(userDTO.getName(), userToDTO.getName()),
                () -> assertEquals(userDTO.getEmail(), userToDTO.getEmail()),
                () -> assertEquals(userDTO.getUserStatus(), userToDTO.getUserStatus())

        );

        assertNotNull(userToDTO.getName());
    }

}
