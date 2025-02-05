package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.entities.Category.Category;
import br.com.wilner.controleFinanceiro.entities.Category.CategoryRequestDTO;
import br.com.wilner.controleFinanceiro.entities.Category.CategoryResponseDTO;
import br.com.wilner.controleFinanceiro.entities.Category.CategoryTotals;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import br.com.wilner.controleFinanceiro.repositories.CategoryRepository;
import br.com.wilner.controleFinanceiro.services.ValidationSerice.CategoryValidationService;
import br.com.wilner.controleFinanceiro.util.converter.CategoryConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryConverter categoryConverter;
    @Mock
    private CategoryValidationService categoryValidationService;

    private Category category;
    private CategoryRequestDTO categoryRequestDTO;
    private CategoryResponseDTO categoryResponseDTO;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "Lazer", "Categoria de Lazer", LocalDateTime.now(), LocalDateTime.now(),
                null, true, new BigDecimal("300"), new BigDecimal("300"));
        categoryRequestDTO = new CategoryRequestDTO("Lazer", "Categoria de Lazer", new BigDecimal("300"));
        categoryResponseDTO = new CategoryResponseDTO("Lazer", "Categoria de Lazer", true, new BigDecimal("300"), new BigDecimal("300"));
    }

    @Test
    void deveSalvarCategoriaComSucesso() {
        when(categoryConverter.converterToEntity(any(CategoryRequestDTO.class))).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryConverter.converterToDTO(any(Category.class))).thenReturn(categoryResponseDTO);

        CategoryResponseDTO result = categoryService.saveCategory(categoryRequestDTO);

        assertNotNull(result);
        assertEquals(categoryResponseDTO.name(), result.name());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void deveLancarExceptionAoBuscarCategoriaNaoEncontrada() {
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            categoryService.getCategoryByName("Lazer");
        });

        assertEquals("Categoria não encontrada " + categoryRequestDTO.name(), exception.getMessage());
    }

    @Test
    void deveCalcularTotaisPorCategoria() {
        when(categoryRepository.findTotalsByCategoryName(anyString())).thenReturn(Optional.of(new CategoryTotals("Lazer", new BigDecimal("300"), new BigDecimal("300"))));

        CategoryTotals result = categoryService.calculateTotalsForCategory("Lazer");

        assertNotNull(result);
        assertEquals(categoryResponseDTO.valueRealized(), result.totalRealized());
        assertEquals(categoryResponseDTO.valueExpected(), result.totalExpected());
        verify(categoryRepository).findTotalsByCategoryName(anyString());
    }

    @Test
    void deveBuscarCategoriaPorNomeComSucesso() {
        when(categoryRepository.findByNameAndIsActive(anyString(), anyBoolean())).thenReturn(Optional.of(category));
        when(categoryConverter.converterToDTO(category)).thenReturn(categoryResponseDTO);

        CategoryResponseDTO result = categoryService.getCategoryByName("Teste");

        assertNotNull(result);
        assertEquals(categoryResponseDTO.name(), result.name());
    }

    @Test
    void deveLancarExceptionAoBuscarCategoriaPorNomeNaoExistente() {

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            categoryService.getCategoryByName("Teste");
        });

        assertEquals("Categoria não encontrada Teste", exception.getMessage());
    }

    @Test
    void deveLancarExceptionAoCalcularTotaisParaCategoriaNaoExistente() {

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            categoryService.calculateTotalsForCategory(categoryRequestDTO.name());
        });

        assertEquals("Categoria não encontrada ou sem transações: " + categoryRequestDTO.name(), exception.getMessage());
    }

    @Test
    void deveExcluirCategoriaComSucesso() {
        when(categoryRepository.findByNameAndIsActive(anyString(), anyBoolean())).thenReturn(Optional.of(category));

        categoryService.deactivationService("1L");

        assertEquals(categoryResponseDTO.isActive(), true);
    }

    @Test
    void deveLancarExceptionAoExcluirCategoriaNaoExistente() {
        when(categoryRepository.findByNameAndIsActive(anyString(), anyBoolean())).thenReturn(Optional.empty());

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            categoryService.deactivationService("1L");
        });

        assertEquals("Categoria não encontrada ", exception.getMessage());
    }

    @Test
    void deveBuscarTodasAsCategorias() {
        when(categoryRepository.findByIsActive(true)).thenReturn(Collections.singletonList(category));

        List<CategoryResponseDTO> result = categoryService.getAllCategories();
        assertNotNull(result);

    }

}
