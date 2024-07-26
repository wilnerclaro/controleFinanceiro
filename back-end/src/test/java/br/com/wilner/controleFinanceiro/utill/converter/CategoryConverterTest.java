package br.com.wilner.controleFinanceiro.utill.converter;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryConverterTest {

    /*@InjectMocks
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

    }*/
}
