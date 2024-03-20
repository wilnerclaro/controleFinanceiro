package br.com.wilner.controleFinanceiro.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryDTO {

    @JsonProperty(value = "NOME_CATEGORIA")
    private String categoryName;
    @JsonProperty(value = "DESCRICAO_CATEGORIA")
    private String description;
    @JsonProperty(value = "TOTAL_PREVISTO")
    private BigDecimal totalPredicted;
    @JsonProperty(value = "TOTAL_REALIZADO")
    private BigDecimal totalRealized;

    public CategoryDTO(String categoryName, BigDecimal totalPredicted, BigDecimal totalRealized) {
        this.categoryName = categoryName;
        this.totalPredicted = totalPredicted;
        this.totalRealized = totalRealized;
    }
}
