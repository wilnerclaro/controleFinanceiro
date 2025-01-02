package br.com.wilner.controleFinanceiro.entities.Transaction;

import br.com.wilner.controleFinanceiro.entities.Category.Category;
import br.com.wilner.controleFinanceiro.entities.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRANSACOES")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "TIPO_TRANSACAO")
    @NotNull(message = "O tipo de transação é obrigatório")
    private String transactionType;
    @Column(name = "VALOR_TRANSACAO")
    private BigDecimal transactionValue;
    @Column(name = "DATA_TRANSACAO")
    @CreatedDate
    private LocalDateTime transactionDate;
    @Column(name = "DATA_ATUALIZACAO")
    @LastModifiedDate
    private LocalDateTime updateDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Category category;
    @Column(name = "DESCRICAO")
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;
    @Column(name = "FORMA_PAGAMENTO")
    private String paymentMethod;
}
