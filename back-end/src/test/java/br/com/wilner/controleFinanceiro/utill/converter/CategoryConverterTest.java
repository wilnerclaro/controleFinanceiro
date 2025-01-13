package br.com.wilner.controleFinanceiro.utill.converter;

import br.com.wilner.controleFinanceiro.entities.Category.Category;
import br.com.wilner.controleFinanceiro.entities.Category.CategoryRequestDTO;
import br.com.wilner.controleFinanceiro.entities.Category.CategoryResponseDTO;
import br.com.wilner.controleFinanceiro.util.converter.CategoryConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CategoryConverterTest {

    @InjectMocks
    private CategoryConverter categoryConverter;

    private Category category;
    private CategoryRequestDTO categoryRequestDTO;
    private CategoryResponseDTO categoryResponseDTO;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "LAZER", "CATEGORIA DE LAZER", LocalDateTime.now(), LocalDateTime.now(),
                null, true, new BigDecimal("300"), new BigDecimal("300"));
        categoryRequestDTO = new CategoryRequestDTO("LAZER", "CATEGORIA DE LAZER", new BigDecimal("300"));
        categoryResponseDTO = new CategoryResponseDTO("LAZER", "CATEGORIA DE LAZER", true, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    @Test
    void deveConverterDeRequestDTOParaEntidade() {
        Category result = categoryConverter.converterToEntity(categoryRequestDTO);

        assertNotNull(result);
        assertEquals(categoryRequestDTO.name(), result.getName());
        assertEquals(categoryRequestDTO.description(), result.getDescription());
    }

    @Test
    void deveConverterDeEntidadeParaResponseDTO() {
        CategoryResponseDTO result = categoryConverter.converterToDTO(category);

        assertNotNull(result);
        assertEquals(category.getName(), result.name());
        assertEquals(category.getDescription(), result.description());
        assertEquals(category.getValueExpected(), result.valueExpected());
        assertEquals(category.getValueRealized(), result.valueRealized());
    }
}
