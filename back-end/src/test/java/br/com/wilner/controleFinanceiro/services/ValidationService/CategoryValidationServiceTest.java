package br.com.wilner.controleFinanceiro.services.ValidationService;

import br.com.wilner.controleFinanceiro.builder.CategoryBuilder;
import br.com.wilner.controleFinanceiro.entities.Category.Category;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import br.com.wilner.controleFinanceiro.services.ValidationSerice.CategoryValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CategoryValidationServiceTest {
    @InjectMocks
    private CategoryValidationService categoryValidationService;


    @Test
    void deveValidarCamposObrigatoriosAoSalvar() {

        Category category = CategoryBuilder.umCategory().comName("").agora();

        ValidationException exception = assertThrows(ValidationException.class, () ->
                categoryValidationService.checkValidFields(category));

        assertEquals("O nome da categoria é obrigatório", exception.getMessage());
    }


}



