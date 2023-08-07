package br.com.wilner.controleFinanceiro.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "tb_usuarios")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String email;
	private String senha;
	private LocalDateTime  dataCriacao = LocalDateTime.now();
	private LocalDateTime  dataAtualizacao = LocalDateTime.now();
	private Boolean ativo = true;
	
	@ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "usuarios_roles", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "role")
	private List<String> roles;

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	private List<UsuarioCategoria> usuarioCategorias;

}
