package br.com.wilner.controleFinanceiro.builder;

import br.com.wilner.controleFinanceiro.entities.User.User;
import br.com.wilner.controleFinanceiro.util.UserStatus;

import java.time.LocalDateTime;

import static br.com.wilner.controleFinanceiro.util.UserStatus.ACTIVE;


public class UserBuilder {
    private User elemento;

    private UserBuilder() {
    }

    public static UserBuilder umUser() {
        UserBuilder builder = new UserBuilder();
        inicializarDadosPadroes(builder);
        return builder;
    }

    public static void inicializarDadosPadroes(UserBuilder builder) {
        builder.elemento = new User();
        User elemento = builder.elemento;


        elemento.setId(1L);
        elemento.setName("Novo Usuário");
        elemento.setEmail("teste@email.com");
        elemento.setUserStatus(ACTIVE);
        elemento.setCreateDate(LocalDateTime.now());
        elemento.setUpdateDate(LocalDateTime.now());
    }

    public UserBuilder comId(Long param) {
        elemento.setId(param);
        return this;
    }

    public UserBuilder comName(String param) {
        elemento.setName(param);
        return this;
    }

    public UserBuilder comEmail(String param) {
        elemento.setEmail(param);
        return this;
    }

    public UserBuilder comUserStatus(UserStatus param) {
        elemento.setUserStatus(param);
        return this;
    }

    public UserBuilder comDataCriacao(LocalDateTime param) {
        elemento.setCreateDate(param);
        return this;
    }

    public UserBuilder comDataAtualizacao(LocalDateTime param) {
        elemento.setUpdateDate(param);
        return this;
    }

    public User agora() {
        return elemento;
    }
}
