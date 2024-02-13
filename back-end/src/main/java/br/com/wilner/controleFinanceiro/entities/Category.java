package br.com.wilner.controleFinanceiro.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Categorias")
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private Long id;
    @Column(name = "nome", nullable = false)
    private String name;
    @Column(name = "descricao")
    private String description;
    @Column(name = "data_criacao")
    private LocalDateTime criationDate;
    @Column(name = "data_atualizacao")
    private LocalDateTime updateDate;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

}
