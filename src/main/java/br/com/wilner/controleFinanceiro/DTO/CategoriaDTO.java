package br.com.wilner.controleFinanceiro.DTO;

import br.com.wilner.controleFinanceiro.entities.Categoria;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
public class CategoriaDTO {

    private Long id;
    private String nome;
    private String descricao;
    private Boolean tipo;
    private String cor;
    private LocalDateTime dataCriacao = LocalDateTime.now();
    private LocalDateTime  dataAtualizacao = LocalDateTime.now();


    public CategoriaDTO(){

    }
    public CategoriaDTO(Categoria entity) {
        BeanUtils.copyProperties(entity, this);
    }


    }
