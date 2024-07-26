package br.com.wilner.controleFinanceiro.entities.Category;

import br.com.wilner.controleFinanceiro.entities.Transaction.Transaction;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Categorias")
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome", nullable = false)
    private String name;
    @Column(name = "descricao")
    private String description;
    @Column(name = "data_criacao")
    @CreatedDate
    private LocalDateTime creationDate;
    @Column(name = "data_atualizacao")
    @LastModifiedDate
    private LocalDateTime updateDate;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;
    @Column(name = "IS_ACTIVE")
    private Boolean isActive = true;
    @Column(name = "valor_previsto")
    private BigDecimal valueExpected;
    @Column(name = "valor_realizado")
    private BigDecimal valueRealized;

}
