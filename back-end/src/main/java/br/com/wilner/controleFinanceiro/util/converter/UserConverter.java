package br.com.wilner.controleFinanceiro.util.converter;

import br.com.wilner.controleFinanceiro.DTO.UserDTO;
import br.com.wilner.controleFinanceiro.entities.User.User;
import br.com.wilner.controleFinanceiro.util.UserStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserConverter {


    public User converterToEntity(UserDTO dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .userStatus(dto.getUserStatus() ? UserStatus.ACTIVE : UserStatus.INACTIVE)
                .createDate(LocalDateTime.now())
                .build();
    }

    public UserDTO converterToDTO(User user) {
        return UserDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .userStatus(user.getUserStatus() == UserStatus.ACTIVE)
                .build();
    }

    public User converterToEntityUpdate(User user, UserDTO userDTO) {
        return User.builder()
                .name(userDTO.getName() != null ? userDTO.getName() : user.getName())
                .email(userDTO.getEmail() != null ? userDTO.getEmail() : user.getEmail())
                .userStatus(userDTO.getUserStatus() != null ? (userDTO.getUserStatus() ? UserStatus.ACTIVE : UserStatus.INACTIVE) : (user.getUserStatus() != null ? user.getUserStatus() : UserStatus.INACTIVE))
                .createDate(user.getCreateDate())
                .updateDate(LocalDateTime.now())
                .build();
    }

}
