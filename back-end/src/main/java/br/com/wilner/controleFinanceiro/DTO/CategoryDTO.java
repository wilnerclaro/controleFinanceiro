package br.com.wilner.controleFinanceiro.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {
    @JsonProperty(value = "ID_CATEGORIA")
    private Long categoryId;
    @JsonProperty(value = "NOME_CATEGORIA")
    private String categoryName;
    @JsonProperty(value = "DESCRICAO_CATEGORIA")
    private String description;

}
