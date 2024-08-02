package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.entities.User.UserDTO;
import br.com.wilner.controleFinanceiro.entities.User.User;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import br.com.wilner.controleFinanceiro.repositories.UserRepository;
import br.com.wilner.controleFinanceiro.services.SoftDeletes.DeactivationService;
import br.com.wilner.controleFinanceiro.util.UserStatus;
import br.com.wilner.controleFinanceiro.util.converter.UserConverter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static br.com.wilner.controleFinanceiro.util.UserStatus.INACTIVE;

@Service
@RequiredArgsConstructor

public class UserService implements DeactivationService {

    private final UserRepository userRepository;

    private final UserConverter userConverter;

    public List<UserDTO> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            users = userRepository.findByUserStatus(UserStatus.ACTIVE);
            return users.stream()
                    .map(userConverter::converterToDTO)
                    .toList();

        } catch (Exception e) {
            throw new ValidationException("Erro ao listar usuarios ");
        }
    }

    public UserDTO getUserByName(String name) {

        User userId = userRepository.findByNameAndUserStatus(name, UserStatus.ACTIVE)
                .orElseThrow(() -> new ValidationException("Usuario não encontrado " + name));
        return userConverter.converterToDTO(userId);
    }

    public UserDTO saveUser(UserDTO userDTO) {
        try {
            User user = userConverter.converterToEntity(userDTO);
            User savedUser = userRepository.save(user);
            return userConverter.converterToDTO(savedUser);
        } catch (Exception e) {
            throw new ValidationException("Erro ao salvar usuário ");
        }
    }

    public UserDTO updateUser(String name, UserDTO userDTO) {
        try {
            User userByName = userRepository.findByNameAndUserStatus(name, UserStatus.ACTIVE).orElseThrow(() -> new ValidationException("Usuario não encontrado " + name));
            userByName = userConverter.converterToEntityUpdate(userByName, userDTO);
            return userConverter.converterToDTO(userRepository.save(userByName));

        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao atualizar usaurio: " + name, e);
        }
    }

    @Override
    public void deactivationService(String name) {
        User user = userRepository.findByNameAndUserStatus(name, UserStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + name));
        user.setUserStatus(INACTIVE);
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);
    }
}



