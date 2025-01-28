package br.com.wilner.controleFinanceiro.controllers;

import br.com.wilner.controleFinanceiro.entities.Category.CategoryRequestDTO;
import br.com.wilner.controleFinanceiro.entities.Category.CategoryResponseDTO;
import br.com.wilner.controleFinanceiro.entities.Category.CategoryTotals;
import br.com.wilner.controleFinanceiro.services.CategoryService;
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
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @InjectMocks
    private CategoryController categoryController;
    @Mock
    private CategoryService categoryService;
    private MockMvc mockMvc;
    private String jsonRequest;
    private CategoryRequestDTO categoryRequestDTO;
    private CategoryResponseDTO categoryResponseDTO;
    private String url;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).alwaysDo(print()).build();
        url = "/categories";
        categoryRequestDTO = new CategoryRequestDTO("Teste", "Categoria para testes", BigDecimal.valueOf(200));
        categoryResponseDTO = new CategoryResponseDTO("Casa", "Teste", true, BigDecimal.ZERO, BigDecimal.ZERO);
        jsonRequest = objectMapper.writeValueAsString(categoryRequestDTO);
    }

    @Test
    void deveCriarUmaNovaCategoriaComSucesso() throws Exception {
        when(categoryService.saveCategory(any(CategoryRequestDTO.class))).thenReturn(categoryResponseDTO);

        mockMvc.perform(post(url + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(categoryService).saveCategory(any(CategoryRequestDTO.class));
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    void deveListarTodasAsCategoriasComSucesso() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(Collections.singletonList(categoryResponseDTO));

        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(categoryService).getAllCategories();
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    void deveRetornarOTotalPorCategoria() throws Exception {
        when(categoryService.calculateTotalsForCategory(anyString()))
                .thenReturn(new CategoryTotals("Teste", new BigDecimal("200"), BigDecimal.ZERO));

        mockMvc.perform(get(url + "/totals/Teste") // Use o nome da categoria diretamente na URL
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(jsonRequest)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());


        verify(categoryService).calculateTotalsForCategory("Teste");
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    void deveLancarErroAoCriarCategoriaComDadosInvalidos() throws Exception {
        categoryRequestDTO = new CategoryRequestDTO("", "", null);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoryRequestDTO)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void deveRetornarCategoriaPorNome() throws Exception {
        when(categoryService.getCategoryByName(anyString())).thenReturn(categoryResponseDTO);

        mockMvc.perform(get(url + "/category")
                        .param("name", "Casa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value("Casa"));

        verify(categoryService).getCategoryByName("Casa");
    }

    @Test
    void deveAtualizarCategoriaComSucesso() throws Exception {
        when(categoryService.updateCategory(anyString(), any(CategoryRequestDTO.class))).thenReturn(categoryResponseDTO);

        mockMvc.perform(patch(url + "/category-update")
                        .param("name", "Casa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(categoryRequestDTO)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value("Casa"));

        verify(categoryService).updateCategory(anyString(), any(CategoryRequestDTO.class));
    }

    @Test
    void deveExcluirCategoriaComSucesso() throws Exception {
        doNothing().when(categoryService).deactivationService(anyString());

        mockMvc.perform(delete(url + "/delete")
                        .param("name", "Casa"))
                .andExpect(status().isAccepted());

        verify(categoryService).deactivationService(anyString());
    }


}

