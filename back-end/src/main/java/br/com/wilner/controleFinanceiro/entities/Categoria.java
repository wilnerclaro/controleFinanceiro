package br.com.wilner.controleFinanceiro.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
	private String cor;
	private LocalDateTime  dataCriacao = LocalDateTime.now();
	private LocalDateTime  dataAtualizacao = LocalDateTime.now();

	@ManyToMany(mappedBy = "categorias")
	private List<Usuario> usuarios = new ArrayList<>();

	@OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<SubCategoria> subCategorias = new ArrayList<>();


}
