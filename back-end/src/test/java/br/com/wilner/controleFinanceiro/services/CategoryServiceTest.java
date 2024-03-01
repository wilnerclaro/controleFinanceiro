package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.DTO.CategoryDTO;
import br.com.wilner.controleFinanceiro.entities.Category;
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

import java.util.Collections;
import java.util.List;

import static br.com.wilner.controleFinanceiro.builder.CategoryBuilder.umCategory;
import static br.com.wilner.controleFinanceiro.builder.CategoryDTOBuilder.umCategoryDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @BeforeEach
    void setup() {

    }

    @Test
    void deveCriarUmaCategoriaComSucesso() {
        Category mockCategory = umCategory().agora();
        CategoryDTO mockCategoryDTO = umCategoryDTO().agora();

        when(categoryConverter.converterToEntity(mockCategoryDTO)).thenReturn(mockCategory);
        when(categoryConverter.converterToDTO(mockCategory)).thenReturn(mockCategoryDTO);
        when(categoryRepository.save(any(Category.class))).thenReturn(mockCategory);

        CategoryDTO savedCategory = categoryService.saveCategory(mockCategoryDTO);

        assertEquals(mockCategoryDTO, savedCategory);
        assertThat(savedCategory).isNotNull();
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
}
