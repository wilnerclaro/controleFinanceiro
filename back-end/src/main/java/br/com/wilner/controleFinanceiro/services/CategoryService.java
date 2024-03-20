package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.DTO.CategoryDTO;
import br.com.wilner.controleFinanceiro.entities.Category;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import br.com.wilner.controleFinanceiro.repositories.CategoryRepository;
import br.com.wilner.controleFinanceiro.services.SoftDeletes.DeactivationService;
import br.com.wilner.controleFinanceiro.services.ValidationSerice.CategoryValidationService;
import br.com.wilner.controleFinanceiro.util.converter.CategoryConverter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements DeactivationService {

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

    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        try {
            categories = categoryRepository.findByIsActive(true);
            return categories.stream()
                    .map(categoryConverter::converterToDTO)
                    .toList();

        } catch (Exception e) {
            throw new ValidationException("Erro ao listar categorias ");
        }
    }

    public CategoryDTO getCategoryByName(String name) {
        Category categoryName = categoryRepository.findByNameAndIsActive(name, true)
                .orElseThrow(() -> new ValidationException("Categoria não encontrada " + name));
        return categoryConverter.converterToDTO(categoryName);

    }


    @Override
    public void deactivationService(String name) {
        Category category = categoryRepository.findByNameAndIsActive(name, true)
                .orElseThrow(() -> new EntityNotFoundException("Categoria não encontrada "));
        category.setIsActive(false);
        category.setUpdateDate(LocalDateTime.now());
        categoryRepository.save(category);
    }

    public CategoryDTO calculateTotalsForCategory(String categoryName) {
        List<Object[]> results = categoryRepository.findTotalsByCategoryNameNative(categoryName);
        if (results.isEmpty()) {
            throw new EntityNotFoundException("Categoria não encontrada ou sem transações: " + categoryName);
        }
        Object[] result = results.get(0);
        return new CategoryDTO(
                (String) result[0],
                (BigDecimal) result[1],
                (BigDecimal) result[2]
        );
    }

}
