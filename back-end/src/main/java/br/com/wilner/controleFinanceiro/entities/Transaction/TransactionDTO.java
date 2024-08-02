package br.com.wilner.controleFinanceiro.entities.Transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {


    @JsonProperty(value = "TIPO_TRANSACAO")
    private String transactionType;
    @JsonProperty(value = "VALOR_TRANSACAO")
    private BigDecimal transactionValue;
    @JsonProperty(value = "NOME_CATEGORIA")
    private String categoryName;
    @JsonProperty(value = "DESCRICAO")
    private String description;
    @JsonProperty(value = "USER_NAME")
    private String userName;
    @JsonProperty(value = "FORMA_PAGAMENTO")
    private String paymentMethod;

}
