package br.com.wilner.controleFinanceiro.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

	@ManyToMany
	@JoinTable(
			name = "usuario_categoria",
			joinColumns = @JoinColumn(name = "usuario_id"),
			inverseJoinColumns = @JoinColumn(name = "categoria_id")
	)
	private List<Categoria> categorias = new ArrayList<>();


}
