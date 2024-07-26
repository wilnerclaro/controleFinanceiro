package br.com.wilner.controleFinanceiro.services.ValidationSerice;

import br.com.wilner.controleFinanceiro.entities.Category.Category;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import br.com.wilner.controleFinanceiro.repositories.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoryValidationService implements ValidateMandatoryFields<Category> {

    private final CategoryRepository categoryRepository;

    public CategoryValidationService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void checkValidFields(Category category) {
        if (category.getName().isEmpty()) {
            throw new ValidationException("O nome da categoria é obrigatório");
        }
        Optional<Category> categoryName = categoryRepository.findByNameIgnoreCase(category.getName());
        if (categoryName.isPresent()) {
            throw new ValidationException("Categoria já Cadastrada com o nome " + category.getName());
        }

    }
}
