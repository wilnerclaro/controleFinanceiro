package br.com.wilner.controleFinanceiro.util.converter;

import br.com.wilner.controleFinanceiro.entities.User.User;
import br.com.wilner.controleFinanceiro.entities.User.UserDTO;
import br.com.wilner.controleFinanceiro.util.UserStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserConverter {


    public User converterToEntity(UserDTO dto) {
        return User.builder()
                .name(dto.name())
                .email(dto.email())
                .userStatus(UserStatus.ACTIVE)
                .createDate(LocalDateTime.now())
                .build();
    }

    public UserDTO converterToDTO(User user) {
        return UserDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User converterToEntityUpdate(User user, UserDTO userDTO) {
        return User.builder()
                .name(userDTO.name() != null ? userDTO.name() : user.getName())
                .email(userDTO.email() != null ? userDTO.email() : user.getEmail())
                .createDate(user.getCreateDate())
                .updateDate(LocalDateTime.now())
                .build();
    }

}
