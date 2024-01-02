package br.com.wilner.controleFinanceiro.DTO;

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

    // todo deve ser criado ua entidade Category , para fazer a implementação da classe aqui no transaction
    // private Category category;
    @JsonProperty(value = "DESCRICAO")
    private String description;

    @JsonProperty(value = "ID_USUARIO")
    private Long userId;

    @JsonProperty(value = "FORMA_PAGAMENTO")
    private String paymentMethod;
}
