package br.com.wilner.controleFinanceiro.util.converter;

import br.com.wilner.controleFinanceiro.DTO.UserDTO;
import br.com.wilner.controleFinanceiro.entities.User;
import br.com.wilner.controleFinanceiro.util.UserStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserConverter {


    public User converterToEntity(UserDTO dto) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .userStatus(dto.getUserStatus() ? UserStatus.ACTIVE : UserStatus.INACTIVE)
                .dataCriacao(LocalDateTime.now())
                .build();
    }

    public UserDTO converterToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .userStatus(user.getUserStatus() == UserStatus.ACTIVE)
                .build();
    }

    public User converterToEntityUpdate(User user, UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId() != null ? userDTO.getId() : user.getId())
                .name(userDTO.getName() != null ? userDTO.getName() : user.getName())
                .email(userDTO.getEmail() != null ? userDTO.getEmail() : user.getEmail())
                .userStatus(userDTO.getUserStatus() != null ? (userDTO.getUserStatus() ? UserStatus.ACTIVE : UserStatus.INACTIVE) : (user.getUserStatus() != null ? user.getUserStatus() : UserStatus.INACTIVE))
                .dataCriacao(user.getDataCriacao())
                .dataAtualizacao(LocalDateTime.now())
                .build();
    }

}
