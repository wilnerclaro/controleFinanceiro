package br.com.wilner.controleFinanceiro.repositories;

import br.com.wilner.controleFinanceiro.entities.Category.Category;
import br.com.wilner.controleFinanceiro.entities.Category.CategoryTotals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByIsActive(Boolean isActive);

    Optional<Category> findByNameIgnoreCase(String name);

    Optional<Category> findByNameAndIsActive(String name, Boolean isActive);

    @Query(value = "SELECT C.NOME,SUM(C.VALOR_PREVISTO), SUM(C.VALOR_REALIZADO) FROM CATEGORIAS C,TRANSACOES T\n" +
            "WHERE C.ID = T.CATEGORIA_ID " +
            "AND C.NOME = :categoryName " +
            "GROUP BY c.nome", nativeQuery = true)
    Optional<CategoryTotals> findTotalsByCategoryName(@Param("categoryName") String categoryName);


}


