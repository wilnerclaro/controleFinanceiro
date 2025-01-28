package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.entities.Category.Category;
import br.com.wilner.controleFinanceiro.entities.Category.CategoryRequestDTO;
import br.com.wilner.controleFinanceiro.entities.Category.CategoryResponseDTO;
import br.com.wilner.controleFinanceiro.entities.Category.CategoryTotals;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import br.com.wilner.controleFinanceiro.repositories.CategoryRepository;
import br.com.wilner.controleFinanceiro.services.SoftDeletes.DeactivationService;
import br.com.wilner.controleFinanceiro.services.ValidationSerice.CategoryValidationService;
import br.com.wilner.controleFinanceiro.util.converter.CategoryConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class CategoryService implements DeactivationService {

    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;
    private final CategoryValidationService categoryValidationService;

    public CategoryResponseDTO saveCategory(CategoryRequestDTO categoryDTO) {

        Category category = categoryConverter.converterToEntity(categoryDTO);
        categoryValidationService.checkValidFields(category);
        try {
            return categoryConverter.converterToDTO(categoryRepository.save(category));
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException("Erro ao Salvar Categoria: dados duplicados");
        }
    }

    public List<CategoryResponseDTO> getAllCategories() {
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

    public CategoryResponseDTO getCategoryByName(String name) {
        Category categoryName = categoryRepository.findByNameAndIsActive(name, true)
                .orElseThrow(() -> new ValidationException("Categoria não encontrada " + name));
        return categoryConverter.converterToDTO(categoryName);

    }


    @Override
    public void deactivationService(String name) {
        Category category = categoryRepository.findByNameAndIsActive(name, true)
                .orElseThrow(() -> new ValidationException("Categoria não encontrada "));
        category.setIsActive(false);
        category.setUpdateDate(LocalDateTime.now());
        categoryRepository.save(category);
    }

    @Transactional
    public CategoryTotals calculateTotalsForCategory(String categoryName) {
        log.info("Calculando totais para a categoria: {}", categoryName);

        return categoryRepository.findTotalsByCategoryName(categoryName)
                .orElseThrow(() -> {
                    log.warn("Categoria não encontrada ou sem transações: {}", categoryName);
                    return new ValidationException("Categoria não encontrada ou sem transações: " + categoryName);
                });
    }

    public CategoryResponseDTO updateCategory(String name, CategoryRequestDTO categoryDTO) {
        Category categoryName = categoryRepository.findByNameAndIsActive(name, true)
                .orElseThrow(() -> new ValidationException("Categoria não encontrada ou não esta ativa"));
        Category category = categoryConverter.converterToEntityUpdate(categoryName, categoryDTO);
        return categoryConverter.converterToDTO(categoryRepository.save(category));
    }
}


