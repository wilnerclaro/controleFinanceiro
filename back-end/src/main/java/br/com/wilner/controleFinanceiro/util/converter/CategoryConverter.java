package br.com.wilner.controleFinanceiro.util.converter;

import br.com.wilner.controleFinanceiro.DTO.CategoryDTO;
import br.com.wilner.controleFinanceiro.entities.Category;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CategoryConverter {

    public Category converterToEntity(CategoryDTO categoryDTO) {
        return Category.builder()
                .id(categoryDTO.getCategoryId())
                .name(categoryDTO.getCategoryName())
                .creationDate(LocalDateTime.now())
                .isActive(true)
                .description(categoryDTO.getDescription())
                .build();
    }

    public CategoryDTO converterToDTO(Category category) {
        return CategoryDTO.builder()
                .categoryName(category.getName())
                .description(category.getDescription())
                .build();
    }
}
