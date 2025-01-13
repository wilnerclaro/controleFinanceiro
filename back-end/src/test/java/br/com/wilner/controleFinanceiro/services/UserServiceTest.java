package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.entities.User.User;
import br.com.wilner.controleFinanceiro.entities.User.UserDTO;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import br.com.wilner.controleFinanceiro.repositories.UserRepository;
import br.com.wilner.controleFinanceiro.util.UserStatus;
import br.com.wilner.controleFinanceiro.util.converter.UserConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserConverter userConverter;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Teste", "teste@teste.com", UserStatus.ACTIVE, LocalDateTime.now(), null, null);
        userDTO = new UserDTO("Teste", "teste@teste.com");
    }

    @Test
    void deveSalvarUsuarioComSucesso() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userConverter.converterToDTO(any(User.class))).thenReturn(userDTO);
        when(userConverter.converterToEntity(any(UserDTO.class))).thenReturn(user);

        UserDTO result = userService.saveUser(userDTO);

        assertNotNull(result);
        assertEquals(userDTO.name(), result.name());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void deveLancarExceptionAoBuscarUsuarioNaoEncontrado() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userService.getUserByName("Teste");
        });

        assertEquals("Usuário não encontrado " + userDTO.name(), exception.getMessage());
    }

    @Test
    void deveBuscarUsuarioPorNome() {
        when(userRepository.findByNameAndUserStatus(anyString(), any(UserStatus.class)))
                .thenReturn(Optional.of(user));
        when(userConverter.converterToDTO(any(User.class))).thenReturn(userDTO);

        UserDTO result = userService.getUserByName("Teste");

        assertNotNull(result);
        assertEquals(userDTO.name(), result.name());
    }


}
