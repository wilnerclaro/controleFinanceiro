package br.com.wilner.controleFinanceiro.controllers;

import br.com.wilner.controleFinanceiro.entities.Transaction.TransactionDTO;
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

import java.math.BigDecimal;
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
    private String jsonRequest;
    private TransactionDTO transactionDTO;
    private String url;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).alwaysDo(print()).build();
        url = "/transactions";
        transactionDTO = new TransactionDTO("Despesa", new BigDecimal("100.00"), "Lazer", "Pagamento mensal", "Teste", "Pix");
        jsonRequest = objectMapper.writeValueAsString(transactionDTO);
    }

    @Test
    void deveCriarUmaNovaTransacaoComSucesso() throws Exception {
        when(transactionService.saveTransaction(any(TransactionDTO.class))).thenReturn(transactionDTO);

        mockMvc.perform(post(url + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(transactionService).saveTransaction(any(TransactionDTO.class));
        verifyNoMoreInteractions(transactionService);
    }

    @Test
    void deveRetornarTransacoesPorCategoriaEPeriodo() throws Exception {
        when(transactionService.getTransactionsByCategoryAndDate(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(Collections.singletonList(transactionDTO));

        mockMvc.perform(get(url + "/category")
                        .param("categoryName", "Lazer")
                        .param("startDate", "2022-01-01")
                        .param("endDate", "2022-01-31")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(transactionService).getTransactionsByCategoryAndDate(anyString(), any(LocalDate.class), any(LocalDate.class));
        verifyNoMoreInteractions(transactionService);
    }

    @Test
    void deveBuscarTransacaoPorUserNameComSucesso() throws Exception {
        when(transactionService.getTransactionsByUserName(anyString())).thenReturn(Collections.singletonList(transactionDTO));

        mockMvc.perform(get(url + "/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("name", "Teste"))
                .andExpect(status().is2xxSuccessful());

        verify(transactionService).getTransactionsByUserName("Teste");
    }

    @Test
    void deveAtualizarTransacaoComSucesso() throws Exception {
        when(transactionService.updateTransaction(anyLong(), any(TransactionDTO.class))).thenReturn(transactionDTO);

        mockMvc.perform(patch(url + "/update-transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(transactionDTO))
                        .param("id", String.valueOf(1L)))
                .andExpect(status().is2xxSuccessful());

        verify(transactionService).updateTransaction(anyLong(), any(TransactionDTO.class));
    }

    @Test
    void deveExcluirTransacaoComSucesso() throws Exception {
        doNothing().when(transactionService).deleteTransaction(1L);

        mockMvc.perform(delete(url + "/delete")
                        .param("id", String.valueOf(1L)))
                .andExpect(status().is2xxSuccessful());

        verify(transactionService).deleteTransaction(1L);
    }


}
