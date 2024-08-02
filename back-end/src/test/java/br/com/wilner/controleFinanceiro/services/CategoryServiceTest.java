package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.entities.Category.CategoryDTO;
import br.com.wilner.controleFinanceiro.builder.CategoryBuilder;
import br.com.wilner.controleFinanceiro.entities.Category.Category;
import br.com.wilner.controleFinanceiro.entities.Category.CategoryRequestDTO;
import br.com.wilner.controleFinanceiro.entities.Category.CategoryResponseDTO;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static br.com.wilner.controleFinanceiro.builder.CategoryBuilder.umCategory;
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
    CategoryRequestDTO categoryRequestDTO;
    CategoryResponseDTO categoryResponseDTO;

    @BeforeEach
    void setUp() {
        categoryRequestDTO = new CategoryRequestDTO("teste", "teste", new BigDecimal("10"));
        categoryResponseDTO = new CategoryResponseDTO("teste", "teste", true, new BigDecimal("10"), new BigDecimal("10"));
    }

    @Test
    void deveCriarUmaCategoriaComSucesso() {
        Category mockCategory = umCategory().agora();

        when(categoryConverter.converterToEntity(categoryRequestDTO)).thenReturn(mockCategory);
        when(categoryConverter.converterToDTO(mockCategory)).thenReturn(categoryResponseDTO);
        when(categoryRepository.save(any(Category.class))).thenReturn(mockCategory);

        var savedCategory = categoryService.saveCategory(categoryRequestDTO);


        verify(categoryConverter).converterToDTO(mockCategory);
        verify(categoryConverter).converterToEntity(categoryRequestDTO);
        verify(categoryRepository).save(mockCategory);
        verifyNoMoreInteractions(categoryConverter, categoryRepository);

    }

    @Test
    void deveDarExcptionCasoOcorraAlgumErroDuranteACriacaoDeUmaCategoria() {
        Category mockCategory = umCategory().agora();

        when(categoryConverter.converterToEntity(categoryRequestDTO)).thenReturn(mockCategory);
        when(categoryRepository.save(any(Category.class))).thenThrow(new ValidationException("Erro ao Salvar Categoria"));

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            categoryService.saveCategory(categoryRequestDTO);
        });

        assertEquals("Erro ao Salvar Categoria", ex.getMessage());
    }

    @Test
    void deveListarTodasAsCategoriasComsucesso() {
        List<CategoryResponseDTO> mockCategoriesDTO = Collections.singletonList(categoryResponseDTO);

        Category mockCategory = umCategory().agora();
        List<Category> mockCategories = Collections.singletonList(mockCategory);

        when(categoryConverter.converterToDTO(mockCategory)).thenReturn(categoryResponseDTO);
        when(categoryRepository.findByIsActive(true)).thenReturn(mockCategories);

        List<CategoryResponseDTO> result = categoryService.getAllCategories();

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
        Category mockCategory = umCategory().agora();

        when(categoryConverter.converterToDTO(mockCategory)).thenReturn(categoryResponseDTO);
        when(categoryRepository.findByNameAndIsActive(categoryResponseDTO.name(), true)).thenReturn(Optional.of(mockCategory));

        var getCategoryByName = categoryService.getCategoryByName(categoryResponseDTO.name());

        assertEquals(categoryResponseDTO, getCategoryByName);
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
    void deveDesativarCategoriaComSucesso() {
        Category category = CategoryBuilder.umCategory().agora();

        when(categoryRepository.findByNameAndIsActive(category.getName(), true)).thenReturn(Optional.of(category));

        categoryService.deactivationService(category.getName());

        assertFalse(category.getIsActive());
        verify(categoryRepository).save(category);
        verify(categoryRepository).findByNameAndIsActive(category.getName(), true);
    }

    @Test
    void deveLancarExcptionQuandoUmaCategoriaNaoEhEncontrada() {
        String categoryName = "Inexistente";

        when(categoryRepository.findByNameAndIsActive(categoryName, true)).thenReturn(Optional.empty());


        ValidationException exception = assertThrows(ValidationException.class,
                () -> categoryService.deactivationService(categoryName));

        assertEquals("Categoria não encontrada ", exception.getMessage());
    }

    @Test
    void calculateTotalsForCategory_ShouldReturnTotals_WhenCategoryExists() {

        String categoryName = "Entertainment";
        BigDecimal totalPredicted = new BigDecimal("100");
        BigDecimal totalRealized = new BigDecimal("90");
        List<Object[]> mockResults = Collections.singletonList(new Object[]{categoryName, totalPredicted, totalRealized});

        when(categoryRepository.findTotalsByCategoryNameNative(categoryName)).thenReturn(mockResults);

        CategoryDTO expectedDTO = new CategoryDTO(categoryName, totalPredicted, totalRealized);

        CategoryDTO result = categoryService.calculateTotalsForCategory(categoryName);

        assertNotNull(result);
        assertEquals(expectedDTO.getCategoryName(), result.getCategoryName());
        assertEquals(0, expectedDTO.getTotalPredicted().compareTo(result.getTotalPredicted()));
        assertEquals(0, expectedDTO.getTotalRealized().compareTo(result.getTotalRealized()));
    }


    @Test
    void deveDarErroQuandoACategoriaOuTransacaoNaoForEncontrado() {
        String categoryName = "Nonexistent";
        when(categoryRepository.findTotalsByCategoryNameNative(categoryName)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(ValidationException.class, () ->
                categoryService.calculateTotalsForCategory(categoryName));

        assertEquals("Categoria não encontrada ou sem transações: " + categoryName, exception.getMessage());
    }


}
