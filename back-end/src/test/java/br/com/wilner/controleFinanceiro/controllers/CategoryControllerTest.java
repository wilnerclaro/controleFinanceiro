package br.com.wilner.controleFinanceiro.controllers;

import br.com.wilner.controleFinanceiro.DTO.CategoryDTO;
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

import java.util.Collections;

import static br.com.wilner.controleFinanceiro.builder.CategoryDTOBuilder.umCategoryDTO;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @InjectMocks
    private CategoryController categoryController;
    @Mock
    private CategoryService categoryService;
    private MockMvc mockMvc;
    private String json;
    private CategoryDTO categoryDTO;
    private String url;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).alwaysDo(print()).build();
        url = "/categories";
        categoryDTO = umCategoryDTO().agora();
        json = objectMapper.writeValueAsString(categoryDTO);
    }

    @Test
    void deveCriarUmNovaCategoriaComSucesso() throws Exception {

        when(categoryService.saveCategory(any(CategoryDTO.class))).thenReturn(categoryDTO);
        mockMvc.perform(post(url + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verifyNoMoreInteractions(categoryService);

    }

    @Test
    void deveListarTodosOsUsuariosComSucesso() throws Exception {

        when(categoryService.getAllCategories()).thenReturn(Collections.singletonList(categoryDTO));
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(categoryService).getAllCategories();
        verifyNoMoreInteractions(categoryService);
    }

    @Test
    void deveBuscarUmaCategoriaPorNome() throws Exception {
        CategoryDTO categoryByname = umCategoryDTO().comCategoryName("teste").agora();
        when(categoryService.getCategoryByName(categoryByname.getCategoryName())).thenReturn(categoryDTO);
        mockMvc.perform(get(url + "/category")
                        .param("name", categoryByname.getCategoryName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(categoryService).getCategoryByName(categoryByname.getCategoryName());
        verifyNoMoreInteractions(categoryService);

    }

    @Test
    void naoDeveBuscarUmaCategoriaPorNomeNull() throws Exception {
        mockMvc.perform(get(url + "/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verifyNoMoreInteractions(categoryService);
    }

    @Test
    void naoDeveEnviarReqiestDeleteCasoNameSejaNull() throws Exception {
        mockMvc.perform(delete(url + "/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verifyNoMoreInteractions(categoryService);

    }

    @Test
    void deveDeletarUsuarioComSucesso() throws Exception {
        String categoryName = "Teste";
        doNothing().when(categoryService).deactivationService(categoryName);

        mockMvc.perform(delete(url + "/delete")
                        .param("name", categoryName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(categoryService).deactivationService(categoryName);
        verifyNoMoreInteractions(categoryService);

    }
}
