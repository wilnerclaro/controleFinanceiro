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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @InjectMocks
    private UserController userController;
    @Mock
    private UserService userService;
    private MockMvc mockMvc;
    private String jsonRequest;
    private UserDTO userDTO;
    private String url;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).alwaysDo(print()).build();
        url = "/users";
        userDTO = new UserDTO("Novo Usuario", "teste@teste.com");
        jsonRequest = objectMapper.writeValueAsString(userDTO);
    }

    @Test
    void deveCriarUmNovoUsuarioComSucesso() throws Exception {
        when(userService.saveUser(any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(post(url + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value("Novo Usuario"))
                .andExpect(jsonPath("$.email").value("teste@teste.com"));

        verify(userService).saveUser(any(UserDTO.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    void deveBuscarUsuarioPorNome() throws Exception {
        when(userService.getUserByName(anyString())).thenReturn(userDTO);

        mockMvc.perform(get(url + "/user")
                        .param("name", "Novo Usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value("Novo Usuario"))
                .andExpect(jsonPath("$.email").value("teste@teste.com"));

        verify(userService).getUserByName(anyString());
        verifyNoMoreInteractions(userService);
    }

    @Test
    void deveAtualizarUsuarioComSucesso() throws Exception {
        when(userService.updateUser(anyString(), any(UserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(patch(url + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userDTO))
                        .param("name", "Novo Usuario"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value("Novo Usuario"));

        verify(userService).updateUser(anyString(), any(UserDTO.class));
    }

    @Test
    void deveExcluirUsuarioComSucesso() throws Exception {
        doNothing().when(userService).deactivationService(anyString());

        mockMvc.perform(delete(url + "/delete")
                        .param("name", "Novo Usuario"))
                .andExpect(status().is2xxSuccessful());

        verify(userService).deactivationService("Novo Usuario");
    }


}
