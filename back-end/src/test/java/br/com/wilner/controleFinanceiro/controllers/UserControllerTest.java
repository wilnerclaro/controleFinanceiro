package br.com.wilner.controleFinanceiro.controllers;

import br.com.wilner.controleFinanceiro.entities.User.UserDTO;
import br.com.wilner.controleFinanceiro.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    private MockMvc mockMvc;
    private String json;
    private UserDTO userDTO;
    private String url;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).alwaysDo(print()).build();
        url = "/users";
        userDTO = UserDTO.builder().name("Novo Usuario").email("teste@teste.com").userStatus(true).build();
        json = objectMapper.writeValueAsString(userDTO);
    }

    @Test
    void deveListarTodosOsUsuariosComSucesso() throws Exception {

        when(userService.getAllUsers()).thenReturn(Collections.singletonList(userDTO));
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(userService).getAllUsers();
        verifyNoMoreInteractions(userService);
    }

    @Test
    void deveCriarUmNovoUsuarioComSucesso() throws Exception {

        when(userService.saveUser(any(UserDTO.class))).thenReturn(userDTO);
        mockMvc.perform(post(url + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verifyNoMoreInteractions(userService);

    }

    @Test
    void naoDeveEnviarReqiestCasoUserDTOSejaNull() throws Exception {
        mockMvc.perform(post(url + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verifyNoMoreInteractions(userService);

    }

    @Test
    void deveAtualizarUmNovoUsuarioComSucesso() throws Exception {
        String userName = "Teste";
        when(userService.updateUser(eq(userName), any(UserDTO.class))).thenReturn(new UserDTO());

        mockMvc.perform(patch(url + "/update")
                        .param("name", userName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(userService).updateUser(eq(userName), any(UserDTO.class));
        verifyNoMoreInteractions(userService);

    }

    @Test
    void naoDeveEnviarReqiestCasoIDSejaNull() throws Exception {
        mockMvc.perform(put(url + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verifyNoMoreInteractions(userService);

    }

    @Test
    void deveDeletarUsuarioComSucesso() throws Exception {
        String userName = "Teste";
        doNothing().when(userService).deactivationService(eq(userName));

        mockMvc.perform(delete(url + "/delete")
                        .param("name", userName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(userService).deactivationService(eq(userName));
        verifyNoMoreInteractions(userService);

    }

    @Test
    void naoDeveEnviarReqiestDeleteCasoIDSejaNull() throws Exception {
        mockMvc.perform(delete(url + "/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verifyNoMoreInteractions(userService);

    }

    @Test
    void deveBuscaUmUsuarioComIDComSucesso() throws Exception {
        String userName = "Teste";

        when(userService.getUserByName(eq(userName))).thenReturn(new UserDTO());

        mockMvc.perform(get(url + "/user")
                        .param("name", userName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(userService).getUserByName(eq(userName));
        verifyNoMoreInteractions(userService);

    }

    @Test
    void naoDeveBuscaUmUsuarioComIDNull() throws Exception {

        mockMvc.perform(get(url + "/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verifyNoMoreInteractions(userService);

    }

}
