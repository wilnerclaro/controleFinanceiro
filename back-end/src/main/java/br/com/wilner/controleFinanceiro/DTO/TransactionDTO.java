package br.com.wilner.controleFinanceiro.DTO;

import br.com.wilner.controleFinanceiro.entities.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {

    @JsonProperty(value = "ID")
    private Long id;
    @JsonProperty(value = "TIPO_TRANSACAO")
    private String transactionType;
    @JsonProperty(value = "VALOR_TRANSACAO")
    private BigDecimal transactionValue;
    @JsonProperty(value = "CATEGORIA")
    private Category category;
    @JsonProperty(value = "DESCRICAO")
    private String description;
    @JsonProperty(value = "ID_USUARIO")
    private Long userId;
    @JsonProperty(value = "FORMA_PAGAMENTO")
    private String paymentMethod;
}
