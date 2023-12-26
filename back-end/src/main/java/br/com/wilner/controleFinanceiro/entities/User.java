package br.com.wilner.controleFinanceiro.entities;

import br.com.wilner.controleFinanceiro.util.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Usuarios")
@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;
    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

}
