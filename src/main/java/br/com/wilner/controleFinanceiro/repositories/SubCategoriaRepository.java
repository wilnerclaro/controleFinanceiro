package br.com.wilner.controleFinanceiro.repositories;

import br.com.wilner.controleFinanceiro.DTO.SubCategoriaDTO;
import br.com.wilner.controleFinanceiro.DTO.interfaces.SubCategoriaInfo;
import br.com.wilner.controleFinanceiro.entities.SubCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubCategoriaRepository extends JpaRepository<SubCategoria, Long> {

    @Query("SELECT s.id AS id, s.nome AS nome, s.descricao AS descricao, c.id AS categoriaId, c.nome AS categoriaNome FROM SubCategoria s INNER JOIN s.categoria c WHERE s.id = :id")
    SubCategoriaInfo findSubCategoriaInfoById(Long id);
}
