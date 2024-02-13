package br.com.wilner.controleFinanceiro.entities;

import jakarta.persistence.*;
import lombok.*;

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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private Long id;
    @Column(name = "TIPO_TRANSACAO")
    private String transactionType;
    @Column(name = "VALOR_TRANSACAO")
    private BigDecimal transactionValue;
    @Column(name = "DATA_TRANSACAO")
    private LocalDateTime transactionDate;
    @Column(name = "DATA_ATUALIZACAO")
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
