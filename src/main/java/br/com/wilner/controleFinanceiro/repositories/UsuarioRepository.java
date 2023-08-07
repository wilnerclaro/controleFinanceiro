package br.com.wilner.controleFinanceiro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wilner.controleFinanceiro.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}
