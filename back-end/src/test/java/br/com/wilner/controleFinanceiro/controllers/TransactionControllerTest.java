package br.com.wilner.controleFinanceiro.controllers;

import br.com.wilner.controleFinanceiro.DTO.TransactionDTO;
import br.com.wilner.controleFinanceiro.builder.TransactionDTOBuilder;
import br.com.wilner.controleFinanceiro.services.TransactionService;
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

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @InjectMocks
    private TransactionController transactionController;
    @Mock
    private TransactionService transactionService;
    private MockMvc mockMvc;
    private String json;
    private TransactionDTO transactionDTO;
    private String url;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).alwaysDo(print()).build();
        url = "/transactions";
        transactionDTO = TransactionDTOBuilder.umTransactionDTO().agora();
        json = objectMapper.writeValueAsString(transactionDTO);
    }


    @Test
    void deveCriarUmNovaTransacaoComSucesso() throws Exception {

        when(transactionService.saveTransaction(any(TransactionDTO.class))).thenReturn(transactionDTO);
        mockMvc.perform(post(url + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verifyNoMoreInteractions(transactionService);

    }

    @Test
    void naoDeveCriarCasoCamposObrigatorioNull() throws Exception {
        mockMvc.perform(post(url + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verifyNoMoreInteractions(transactionService);

    }

    @Test
    void deveAtualizarUmaTransacaoComSucesso() throws Exception {
        Long transactionID = 1L;
        when(transactionService.updateTransaction(eq(transactionID), any(TransactionDTO.class))).thenReturn(new TransactionDTO());

        mockMvc.perform(put(url + "/update")
                        .param("id", transactionID.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(transactionService).updateTransaction(eq(transactionID), any(TransactionDTO.class));
        verifyNoMoreInteractions(transactionService);

    }

    @Test
    void naoDeveEnviarReqiestCasoIDSejaNull() throws Exception {
        mockMvc.perform(put(url + "/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verifyNoMoreInteractions(transactionService);

    }

    @Test
    void deveDeletarUsuarioComSucesso() throws Exception {
        Long transactionID = 1L;
        doNothing().when(transactionService).deleteTransaction(transactionID);

        mockMvc.perform(delete(url + "/delete")
                        .param("id", transactionID.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(transactionService).deleteTransaction(transactionID);
        verifyNoMoreInteractions(transactionService);

    }

    @Test
    void naoDeveEnviarReqiestDeleteCasoIDSejaNull() throws Exception {
        mockMvc.perform(delete(url + "/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verifyNoMoreInteractions(transactionService);

    }

    @Test
    void deveBuscaUmaTransacaoComNomeComSucesso() throws Exception {
        String transactionName = "Teste";
        when(transactionService.getTransactionsByUserName(transactionName)).thenReturn(Collections.singletonList(transactionDTO));

        mockMvc.perform(get(url + "/users")
                        .param("name", transactionName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(transactionService).getTransactionsByUserName(transactionName);
        verifyNoMoreInteractions(transactionService);

    }

    @Test
    void naoDeveBuscaUmUsuarioComIDNull() throws Exception {

        mockMvc.perform(get(url + "/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verifyNoMoreInteractions(transactionService);

    }

    @Test
    void deveBuscarTransacoesPorCategoriaEData() throws Exception {
        String categoryName = "Lazer";
        String startDate = "2022-01-01";
        String endDate = "2022-01-31";

        when(transactionService.getTransactionsByCategoryAndDate(categoryName, LocalDate.parse(startDate), LocalDate.parse(endDate)))
                .thenReturn(Collections.singletonList(transactionDTO));

        mockMvc.perform(get(url + "/category")
                        .param("categoryName", categoryName)
                        .param("startDate", startDate)
                        .param("endDate", endDate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());


        verify(transactionService, times(1)).getTransactionsByCategoryAndDate(categoryName, LocalDate.parse(startDate), LocalDate.parse(endDate));
        verifyNoMoreInteractions(transactionService);
    }


}
