package br.com.wilner.controleFinanceiro.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wilner.controleFinanceiro.entities.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

    @EntityGraph(attributePaths = "categorias")
    Optional<Usuario> findById(Long id);
}
