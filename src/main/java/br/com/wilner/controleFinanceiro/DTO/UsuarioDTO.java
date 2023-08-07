package br.com.wilner.controleFinanceiro.DTO;

import br.com.wilner.controleFinanceiro.entities.Usuario;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
public class UsuarioDTO {

    private Long id;
    private String nome;
    private String email;
    private String senha;
    private LocalDateTime dataCriacao = LocalDateTime.now();
    private LocalDateTime  dataAtualizacao = LocalDateTime.now();
    private Boolean ativo = true;

    public UsuarioDTO() {
    }
    public UsuarioDTO(Usuario entity) {
        BeanUtils.copyProperties(entity, this);
    }
}
