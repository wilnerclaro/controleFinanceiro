package br.com.wilner.controleFinanceiro.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "tb_sub_categoria")
public class SubCategoria {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "categoria_id")
	@ToString.Exclude
	private Categoria categoria;
}
