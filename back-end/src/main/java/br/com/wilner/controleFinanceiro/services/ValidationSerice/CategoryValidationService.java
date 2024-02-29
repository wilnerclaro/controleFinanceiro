package br.com.wilner.controleFinanceiro.services.ValidationSerice;

import br.com.wilner.controleFinanceiro.entities.Category;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class CategoryValidationService implements ValidateMandatoryFields<Category> {
    @Override
    public void checkValidFields(Category category) {
        if (category.getName().isEmpty()) {
            throw new ValidationException("O nome da categoria é obrigatório");
        }

    }
}
