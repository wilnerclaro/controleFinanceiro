package br.com.wilner.controleFinanceiro.builder;

import br.com.wilner.controleFinanceiro.entities.User.UserDTO;


public class UserDTOBuilder {
    private UserDTO elemento;

    private UserDTOBuilder() {
    }

    public static UserDTOBuilder umUserDTO() {
        UserDTOBuilder builder = new UserDTOBuilder();
        inicializarDadosPadroes(builder);
        return builder;
    }

    public static void inicializarDadosPadroes(UserDTOBuilder builder) {
        builder.elemento = new UserDTO();
        UserDTO elemento = builder.elemento;


        elemento.setName("Novo Usuário");
        elemento.setEmail("teste@email.com");
        elemento.setUserStatus(true);
    }


    public UserDTOBuilder comName(String param) {
        elemento.setName(param);
        return this;
    }

    public UserDTOBuilder comEmail(String param) {
        elemento.setEmail(param);
        return this;
    }

    public UserDTOBuilder comUserStatus(Boolean param) {
        elemento.setUserStatus(param);
        return this;
    }

    public UserDTO agora() {
        return elemento;
    }
}

