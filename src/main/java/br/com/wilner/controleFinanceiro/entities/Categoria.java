package br.com.wilner.controleFinanceiro.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "tb_categorias")
public class Categoria  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String descricao;
	private Boolean tipo;
	private String cor;
	private LocalDateTime  dataCriacao = LocalDateTime.now();
	private LocalDateTime  dataAtualizacao = LocalDateTime.now();

	@OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
	private List<UsuarioCategoria> usuarioCategorias;
	
	
}
