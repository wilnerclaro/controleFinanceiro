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

import java.util.ArrayList;
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
        List<User> listUser = new ArrayList<>();
        User user = umUser().agora();
        listUser.add(user);

        List<UserDTO> listUserDTO = new ArrayList<>();
        UserDTO userDTO = umUserDTO().agora();
        listUserDTO.add(userDTO);


        when(userConverter.converterToDTO(user)).thenReturn(userDTO);
        when(userRepository.findAll()).thenReturn(listUser);

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(listUserDTO, result);
        verify(userConverter).converterToDTO(user);
        verify(userRepository).findAll();
        verifyNoMoreInteractions(userConverter, userRepository);


    }

    @Test
    void deveBuscarUmUsuarioComId() {
        final long USER_ID = 1L;
        User userById = umUser().comId(USER_ID).agora();
        UserDTO userDTOById = umUserDTO().comId(USER_ID).agora();

        when(userConverter.converterToDTO(userById)).thenReturn(userDTOById);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(userById));

        UserDTO getUserById = userService.getUserById(USER_ID);

        assertEquals(userDTOById, getUserById);
        verify(userConverter).converterToDTO(userById);
        verify(userRepository).findById(USER_ID);
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
        final Long USER_ID = 1L;
        User user = umUser().comUserStatus(ACTIVE).agora();

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

        doAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            assertEquals(INACTIVE, savedUser.getUserStatus()); // Assumindo que INACTIVE é o status para usuários desativados
            assertNotNull(savedUser.getDataAtualizacao()); // Verifica se a data de atualização foi definida
            return null;
        }).when(userRepository).save(any(User.class));

        userService.deactivationService(USER_ID);

        verify(userRepository, times(1)).save(user); // Verifica se o usuário é salvo com o status atualizado
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        final Long USER_ID = 1L;
        User existingUser = umUser().comId(1L).comName("Novo Usuário").agora();
        UserDTO updateUserDTO = umUserDTO().comName("Novo Nome").comId(1L).agora();

        when(userRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(existingUser));
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(userConverter.converterToEntityUpdate(any(User.class), any(UserDTO.class)))
                .thenAnswer(invocation -> {
                    User userToUpdate = invocation.getArgument(0, User.class);
                    UserDTO updatedData = invocation.getArgument(1, UserDTO.class);
                    userToUpdate.setName(updatedData.getName());
                    return userToUpdate;
                });

        when(userConverter.converterToDTO(existingUser)).thenReturn(updateUserDTO);

        UserDTO result = userService.updateUser(USER_ID, updateUserDTO);

        assertNotNull(existingUser);
        assertNotNull(result);
        assertEquals(updateUserDTO.getName(), result.getName());

        verify(userRepository, times(1)).findById(USER_ID);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void naoDeveRetornarUsarioCasoErroNoRepositorio() {

        when(userRepository.findAll()).thenThrow(ValidationException.class);

        String message = assertThrows(ValidationException.class, () -> {
            userService.getAllUsers();
        }).getMessage();

        assertEquals("Erro ao listar usuarios ", message);
    }

    @Test
    void naoDeveRetornarUsarioCasoIDNaoExista() {
        final Long USUARIO_ID = 2L;

        when(userRepository.findById(USUARIO_ID)).thenReturn(Optional.empty());

        String message = assertThrows(ValidationException.class, () -> {
            userService.getUserById(USUARIO_ID);
        }).getMessage();

        assertEquals("Usuario não encontrado " + USUARIO_ID, message);
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
        User userId = umUser().comId(1L).agora();
        when(userRepository.findById(userId.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            userService.deactivationService(userId.getId());
        });

        String expectedMessage = "Usuário não encontrado com ID: " + userId.getId();
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void deveLancarRuntimeExceptionParaErroInesperadoNaAtualizacao() {
        Long userId = 1L;
        UserDTO userDTOMock = umUserDTO().agora();
        User userMock = umUser().agora();

        when(userRepository.findById(userId)).thenReturn(Optional.of(userMock));
        when(userConverter.converterToEntityUpdate(userMock, userDTOMock)).thenReturn(userMock);
        when(userRepository.save(userMock)).thenThrow(new RuntimeException("Erro no banco de dados"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(userId, userDTOMock);
        });

        String expectedMessage = "Falha ao atualizar usaurio: " + userId;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deveLancarExceptionQuandoUsuarioNaoEncontradoParaAtualizacao() {
        Long userId = 1L;
        UserDTO userDTOMock = umUserDTO().agora();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ValidationException.class, () -> {
            userService.updateUser(userId, userDTOMock);
        });

        String expectedMessage = "Usuario não encontrado " + userId;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }


}
