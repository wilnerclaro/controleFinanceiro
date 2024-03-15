package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.DTO.UserDTO;
import br.com.wilner.controleFinanceiro.entities.User;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import br.com.wilner.controleFinanceiro.repositories.UserRepository;
import br.com.wilner.controleFinanceiro.util.converter.UserConverter;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static br.com.wilner.controleFinanceiro.builder.UserBuilder.umUser;
import static br.com.wilner.controleFinanceiro.builder.UserDTOBuilder.umUserDTO;
import static br.com.wilner.controleFinanceiro.util.UserStatus.ACTIVE;
import static br.com.wilner.controleFinanceiro.util.UserStatus.INACTIVE;
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


    @Test
    void deveListarUsuariosComSucesso() {
        User user = umUser().agora();
        List<User> listUsers = Collections.singletonList(user);

        UserDTO userDTO = umUserDTO().agora();
        List<UserDTO> listUsersDTO = Collections.singletonList(userDTO);


        when(userConverter.converterToDTO(user)).thenReturn(userDTO);
        when(userRepository.findByUserStatus(ACTIVE)).thenReturn(listUsers);

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(listUsersDTO, result);
        verify(userConverter).converterToDTO(user);
        verify(userRepository).findByUserStatus(ACTIVE);
        verifyNoMoreInteractions(userConverter, userRepository);


    }

    @Test
    void deveBuscarUmUsuarioComId() {
        final String USER_NAME = "Teste";
        User userByName = umUser().comName(USER_NAME).agora();
        UserDTO userDTOByName = umUserDTO().comName(USER_NAME).agora();

        when(userConverter.converterToDTO(userByName)).thenReturn(userDTOByName);
        when(userRepository.findByNameAndUserStatus(USER_NAME, ACTIVE)).thenReturn(Optional.of(userByName));

        UserDTO getUserById = userService.getUserByName(USER_NAME);

        assertEquals(userDTOByName, getUserById);
        verify(userConverter).converterToDTO(userByName);
        verify(userRepository).findByNameAndUserStatus(USER_NAME, ACTIVE);
        verifyNoMoreInteractions(userConverter, userRepository);

    }

    @Test
    void deveSalvarUmUsuarioComSucesso() {
        UserDTO userDTO = umUserDTO().agora();
        User user = umUser().agora();

        when(userConverter.converterToEntity(userDTO)).thenReturn(user);
        when(userConverter.converterToDTO(user)).thenReturn(userDTO);
        when(userRepository.save(user)).thenReturn(user);

        UserDTO savedUser = userService.saveUser(userDTO);

        assertEquals(userDTO, savedUser);
        verify(userConverter).converterToDTO(user);
        verify(userConverter).converterToEntity(userDTO);
        verify(userRepository).save(user);
        verifyNoMoreInteractions(userConverter, userRepository);
    }

    @Test
    void deveDeletarUmUsuarioComSucesso() {
        final String USER_NAME = "Teste";
        User user = umUser().comUserStatus(ACTIVE).agora();

        when(userRepository.findByNameAndUserStatus(USER_NAME, ACTIVE)).thenReturn(Optional.of(user));

        doAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            assertEquals(INACTIVE, savedUser.getUserStatus());
            assertNotNull(savedUser.getDataAtualizacao());
            return null;
        }).when(userRepository).save(any(User.class));

        userService.deactivationService(USER_NAME);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        final String USER_NAME = "Teste";
        User existingUser = umUser().comName("Novo Usuário").agora();
        UserDTO updateUserDTO = umUserDTO().comName("Novo Nome").agora();

        when(userRepository.findByNameAndUserStatus(USER_NAME, ACTIVE)).thenReturn(Optional.ofNullable(existingUser));
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(userConverter.converterToEntityUpdate(any(User.class), any(UserDTO.class)))
                .thenAnswer(invocation -> {
                    User userToUpdate = invocation.getArgument(0, User.class);
                    UserDTO updatedData = invocation.getArgument(1, UserDTO.class);
                    userToUpdate.setName(updatedData.getName());
                    return userToUpdate;
                });

        when(userConverter.converterToDTO(existingUser)).thenReturn(updateUserDTO);

        UserDTO result = userService.updateUser(USER_NAME, updateUserDTO);

        assertNotNull(existingUser);
        assertNotNull(result);
        assertEquals(updateUserDTO.getName(), result.getName());

        verify(userRepository, times(1)).findByNameAndUserStatus(USER_NAME, ACTIVE);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void naoDeveRetornarUsarioCasoErroNoRepositorio() {

        when(userRepository.findByUserStatus(ACTIVE)).thenThrow(ValidationException.class);

        String message = assertThrows(ValidationException.class, () -> {
            userService.getAllUsers();
        }).getMessage();

        assertEquals("Erro ao listar usuarios ", message);
    }

    @Test
    void naoDeveRetornarUsarioCasoIDNaoExista() {
        final String USER_NAME = "Teste";

        when(userRepository.findByNameAndUserStatus(USER_NAME, ACTIVE)).thenReturn(Optional.empty());

        String message = assertThrows(ValidationException.class, () -> {
            userService.getUserByName(USER_NAME);
        }).getMessage();

        assertEquals("Usuario não encontrado " + USER_NAME, message);
    }

    @Test
    void deveLancarExceptionAoSalvarUsuario() {
        UserDTO userDTO = umUserDTO().agora();

        when(userRepository.save(any(User.class))).thenThrow(ValidationException.class);

        String message = assertThrows(ValidationException.class, () -> {
            userService.saveUser(userDTO);
        }).getMessage();

        assertEquals("Erro ao salvar usuário ", message);
    }


    @Test
    void deveLancarExceptionQuandoUsuarioNaoEncontrado() {
        User username = umUser().comName("Teste").agora();
        when(userRepository.findByNameAndUserStatus(username.getName(), ACTIVE)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.deactivationService(username.getName());
        });

        String expectedMessage = "Usuário não encontrado com ID: " + username.getName();
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deveLancarRuntimeExceptionParaErroInesperadoNaAtualizacao() {
        String USER_NAME = "Teste";
        UserDTO userDTOMock = umUserDTO().agora();
        User userMock = umUser().agora();

        when(userRepository.findByNameAndUserStatus(USER_NAME, ACTIVE)).thenReturn(Optional.of(userMock));
        when(userConverter.converterToEntityUpdate(userMock, userDTOMock)).thenReturn(userMock);
        when(userRepository.save(userMock)).thenThrow(new RuntimeException("Erro no banco de dados"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(USER_NAME, userDTOMock);
        });

        String expectedMessage = "Falha ao atualizar usaurio: " + USER_NAME;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deveLancarExceptionQuandoUsuarioNaoEncontradoParaAtualizacao() {
        String USER_NAME = "Teste";
        UserDTO userDTOMock = umUserDTO().agora();

        when(userRepository.findByNameAndUserStatus(USER_NAME, ACTIVE)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ValidationException.class, () -> {
            userService.updateUser(USER_NAME, userDTOMock);
        });

        String expectedMessage = "Usuario não encontrado " + USER_NAME;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }


}
