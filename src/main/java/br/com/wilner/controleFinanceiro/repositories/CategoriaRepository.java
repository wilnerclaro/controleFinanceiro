package br.com.wilner.controleFinanceiro.repositories;

import br.com.wilner.controleFinanceiro.entities.Categoria;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository <Categoria, Long> {
}
