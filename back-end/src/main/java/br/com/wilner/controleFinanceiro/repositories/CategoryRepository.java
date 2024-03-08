package br.com.wilner.controleFinanceiro.repositories;

import br.com.wilner.controleFinanceiro.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByIsActive(Boolean isActive);

    Optional<Category> findByNameIgnoreCase(String name);

    Optional<Category> findByNameAndIsActive(String name, Boolean isActive);
}
