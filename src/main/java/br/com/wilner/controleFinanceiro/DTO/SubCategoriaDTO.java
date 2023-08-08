package br.com.wilner.controleFinanceiro.DTO;

import br.com.wilner.controleFinanceiro.DTO.interfaces.SubCategoriaInfo;
import br.com.wilner.controleFinanceiro.entities.SubCategoria;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class SubCategoriaDTO implements SubCategoriaInfo{

    private Long id;
    private String nome;
    private String descricao;
    private CategoriaDTO categoria;

    public SubCategoriaDTO(){}

    public SubCategoriaDTO(SubCategoria entity){
        BeanUtils.copyProperties(entity, this);
        this.categoria = new CategoriaDTO(entity.getCategoria());
    }


    @Override
    public Long getCategoriaId() {
        return null;
    }

    @Override
    public String getCategoriaNome() {
        return null;
    }
}
