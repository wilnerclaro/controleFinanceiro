package br.com.wilner.controleFinanceiro.DTO;

import br.com.wilner.controleFinanceiro.entities.SubCategoria;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class SubCategoriaDTO {

    private Long id;
    private String nome;
    private String descricao;
    private Double previsto;
    private Double realizado;
    private String mes;
    private String tipo; // RECEITA OU DESPESA
    private CategoriaDTO categoria;

    public SubCategoriaDTO(){}

    public SubCategoriaDTO(SubCategoria entity){
        BeanUtils.copyProperties(entity, this);
        this.categoria = new CategoriaDTO(entity.getCategoria());
    }

}
