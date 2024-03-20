package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.DTO.CategoryDTO;
import br.com.wilner.controleFinanceiro.entities.Category;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import br.com.wilner.controleFinanceiro.repositories.CategoryRepository;
import br.com.wilner.controleFinanceiro.services.ValidationSerice.CategoryValidationService;
import br.com.wilner.controleFinanceiro.util.converter.CategoryConverter;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static br.com.wilner.controleFinanceiro.builder.CategoryBuilder.umCategory;
import static br.com.wilner.controleFinanceiro.builder.CategoryDTOBuilder.umCategoryDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;

    @Mock
    CategoryRepository categoryRepository;
    @Mock
    CategoryConverter categoryConverter;
    @Mock
    CategoryValidationService categoryValidationService;


    @Test
    void deveCriarUmaCategoriaComSucesso() {
        Category mockCategory = umCategory().agora();
        CategoryDTO mockCategoryDTO = umCategoryDTO().agora();

        when(categoryConverter.converterToEntity(mockCategoryDTO)).thenReturn(mockCategory);
        when(categoryConverter.converterToDTO(mockCategory)).thenReturn(mockCategoryDTO);
        when(categoryRepository.save(any(Category.class))).thenReturn(mockCategory);

        CategoryDTO savedCategory = categoryService.saveCategory(mockCategoryDTO);

        assertEquals(mockCategoryDTO, savedCategory);
        verify(categoryConverter).converterToDTO(mockCategory);
        verify(categoryConverter).converterToEntity(mockCategoryDTO);
        verify(categoryRepository).save(mockCategory);
        verifyNoMoreInteractions(categoryConverter, categoryRepository);

    }

    @Test
    void deveDarExcptionCasoOcorraAlgumErroDuranteACriacaoDeUmaCategoria() {
        CategoryDTO mockCategoryDTO = umCategoryDTO().agora();
        Category mockCategory = umCategory().agora();

        when(categoryConverter.converterToEntity(mockCategoryDTO)).thenReturn(mockCategory);
        when(categoryRepository.save(any(Category.class))).thenThrow(new ValidationException("Erro ao Salvar Categoria"));

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            categoryService.saveCategory(mockCategoryDTO);
        });

        assertEquals("Erro ao Salvar Categoria", ex.getMessage());
    }

    @Test
    void deveListarTodasAsCategoriasComsucesso() {
        CategoryDTO mockCategoryDTO = umCategoryDTO().agora();
        List<CategoryDTO> mockCategoriesDTO = Collections.singletonList(mockCategoryDTO);

        Category mockCategory = umCategory().agora();
        List<Category> mockCategories = Collections.singletonList(mockCategory);

        when(categoryConverter.converterToDTO(mockCategory)).thenReturn(mockCategoryDTO);
        when(categoryRepository.findByIsActive(true)).thenReturn(mockCategories);

        List<CategoryDTO> result = categoryService.getAllCategories();

        assertEquals(mockCategoriesDTO, result);
        verify(categoryConverter).converterToDTO(mockCategory);
        verify(categoryRepository).findByIsActive(true);
        verifyNoMoreInteractions(categoryConverter, categoryRepository);


    }

    @Test
    void deveLancarExceptionCasoNaoConsigaListarAsCategorias() {
        when(categoryRepository.findByIsActive(true)).thenThrow(new ValidationException("Erro ao listar categorias "));

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            categoryService.getAllCategories();
        });

        assertEquals("Erro ao listar categorias ", ex.getMessage());
    }

    @Test
    void deveBuscarCategoriaPorNomeComsucesso() {
        CategoryDTO mockCategoryDTO = umCategoryDTO().agora();
        Category mockCategory = umCategory().agora();

        when(categoryConverter.converterToDTO(mockCategory)).thenReturn(mockCategoryDTO);
        when(categoryRepository.findByNameAndIsActive(mockCategoryDTO.getCategoryName(), true)).thenReturn(Optional.of(mockCategory));

        CategoryDTO getCategoryByName = categoryService.getCategoryByName(mockCategory.getName());

        assertEquals(mockCategoryDTO, getCategoryByName);
        verify(categoryConverter).converterToDTO(mockCategory);
        verifyNoMoreInteractions(categoryConverter, categoryRepository);
    }

    @Test
    void deveLancarValidationExceptionQuandoNomeDaCategoriaNaoEncontrado() {
        Category mockCategory = umCategory().comName("Teste").agora();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> categoryService.getCategoryByName(mockCategory.getName()));

        assertEquals("Categoria não encontrada " + mockCategory.getName(), exception.getMessage());
    }

    @Test
    void shouldDeactivateCategorySuccessfully() {
        // Setup
        String categoryName = "Lazer";
        Category mockCategory = new Category(); // Use o construtor real da sua entidade Category
        mockCategory.setIsActive(true);

        when(categoryRepository.findByNameAndIsActive(categoryName, true)).thenReturn(Optional.of(mockCategory));

        // Execute
        categoryService.deactivationService(categoryName);

        // Verify
        assertFalse(mockCategory.getIsActive());
        verify(categoryRepository).save(mockCategory);
        verify(categoryRepository).findByNameAndIsActive(categoryName, true);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenCategoryNotFound() {
        // Setup
        String categoryName = "Inexistente";

        when(categoryRepository.findByNameAndIsActive(categoryName, true)).thenReturn(Optional.empty());


        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.deactivationService(categoryName));

        assertEquals("Categoria não encontrada ", exception.getMessage());
    }

    @Test
    void calculateTotalsForCategory_ShouldReturnTotals_WhenCategoryExists() {
        // Given
        String categoryName = "Entertainment";
        BigDecimal totalPredicted = new BigDecimal("100");
        BigDecimal totalRealized = new BigDecimal("90");
        List<Object[]> mockResults = Collections.singletonList(new Object[]{categoryName, totalPredicted, totalRealized});

        when(categoryRepository.findTotalsByCategoryNameNative(categoryName)).thenReturn(mockResults);

        // Prepare o CategoryDTO esperado para comparação (ajuste conforme seu construtor real)
        CategoryDTO expectedDTO = new CategoryDTO(categoryName, totalPredicted, totalRealized);

        // When
        CategoryDTO result = categoryService.calculateTotalsForCategory(categoryName);

        // Then
        assertNotNull(result);
        assertEquals(expectedDTO.getCategoryName(), result.getCategoryName());
        assertEquals(0, expectedDTO.getTotalPredicted().compareTo(result.getTotalPredicted()));
        assertEquals(0, expectedDTO.getTotalRealized().compareTo(result.getTotalRealized()));
    }


    @Test
    void calculateTotalsForCategory_ShouldThrowEntityNotFoundException_WhenCategoryNotFound() {
        // Given
        String categoryName = "Nonexistent";
        when(categoryRepository.findTotalsByCategoryNameNative(categoryName)).thenReturn(Collections.emptyList());

        // When & Then
        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                categoryService.calculateTotalsForCategory(categoryName));

        assertEquals("Categoria não encontrada ou sem transações: " + categoryName, exception.getMessage());
    }


}
