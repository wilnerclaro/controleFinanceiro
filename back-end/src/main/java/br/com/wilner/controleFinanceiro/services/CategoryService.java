package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.DTO.CategoryDTO;
import br.com.wilner.controleFinanceiro.entities.Category;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import br.com.wilner.controleFinanceiro.repositories.CategoryRepository;
import br.com.wilner.controleFinanceiro.services.ValidationSerice.CategoryValidationService;
import br.com.wilner.controleFinanceiro.util.converter.CategoryConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;
    private final CategoryValidationService categoryValidationService;

    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {

        Category category = categoryConverter.converterToEntity(categoryDTO);
        categoryValidationService.checkValidFields(category);
        try {
            Category savedCategory = categoryRepository.save(category);
            return categoryConverter.converterToDTO(savedCategory);
        } catch (Exception e) {
            throw new ValidationException("Erro ao Salvar Categoria");
        }
    }


}
