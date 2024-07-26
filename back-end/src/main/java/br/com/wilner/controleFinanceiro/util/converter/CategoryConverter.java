package br.com.wilner.controleFinanceiro.util.converter;

import br.com.wilner.controleFinanceiro.entities.Category.Category;
import br.com.wilner.controleFinanceiro.entities.Category.CategoryRequestDTO;
import br.com.wilner.controleFinanceiro.entities.Category.CategoryResponseDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CategoryConverter {

    public Category converterToEntity(CategoryRequestDTO categoryDTO) {
        return Category.builder()
                .name(categoryDTO.name())
                .creationDate(LocalDateTime.now())
                .isActive(true)
                .description(categoryDTO.description())
                .valueExpected(categoryDTO.valueExpected())
                .build();
    }

    public CategoryResponseDTO converterToDTO(Category category) {
        return CategoryResponseDTO.builder()
                .name(category.getName())
                .description(category.getDescription())
                .valueRealized(category.getValueRealized())
                .valueExpected(category.getValueExpected())
                .isActive(category.getIsActive())
                .build();
    }
}
