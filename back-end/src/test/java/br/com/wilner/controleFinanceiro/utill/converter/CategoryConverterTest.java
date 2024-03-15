package br.com.wilner.controleFinanceiro.utill.converter;

import br.com.wilner.controleFinanceiro.DTO.CategoryDTO;
import br.com.wilner.controleFinanceiro.entities.Category;
import br.com.wilner.controleFinanceiro.util.converter.CategoryConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static br.com.wilner.controleFinanceiro.builder.CategoryBuilder.umCategory;
import static br.com.wilner.controleFinanceiro.builder.CategoryDTOBuilder.umCategoryDTO;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoryConverterTest {

    @InjectMocks
    CategoryConverter categoryConverter;
    @Mock
    CategoryDTO categoryDTO;
    @Mock
    Category category;

    @BeforeEach
    void setup() {
        category = umCategory().agora();
        categoryDTO = umCategoryDTO().agora();
    }

    @Test
    void deveConverterParaCategoryEntityComSucesso() {
        Category categoryToEntity = categoryConverter.converterToEntity(categoryDTO);

        assertAll("CategoryEntity",
                () -> assertEquals(category.getName(), categoryToEntity.getName()),
                () -> assertEquals(category.getDescription(), categoryToEntity.getDescription())
        );
        assertNotNull(categoryToEntity.getName());
        assertNotNull(categoryToEntity.getName());


    }

    @Test
    void deveConverterParaCategoryDTOComSucesso() {
        CategoryDTO categoryToDTO = categoryConverter.converterToDTO(category);

        assertAll("CategoryDTO",
                () -> assertEquals(categoryDTO.getCategoryName(), categoryToDTO.getCategoryName()),
                () -> assertEquals(categoryDTO.getDescription(), categoryToDTO.getDescription())
        );
        assertNotNull(categoryToDTO.getCategoryName());

    }
}
