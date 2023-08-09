package br.com.wilner.controleFinanceiro.repositories;

import br.com.wilner.controleFinanceiro.entities.SubCategoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoriaRepository extends JpaRepository<SubCategoria, Long> {
}
