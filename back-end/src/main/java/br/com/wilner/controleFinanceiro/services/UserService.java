package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.DTO.UserDTO;
import br.com.wilner.controleFinanceiro.entities.User;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import br.com.wilner.controleFinanceiro.repositories.UserRepository;
import br.com.wilner.controleFinanceiro.services.SoftDeletes.DeactivationService;
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
            users = userRepository.findAll();
            return users.stream()
                    .map(userConverter::converterToDTO)
                    .toList();

        } catch (Exception e) {
            throw new ValidationException("Erro ao listar usuarios ");
        }
    }

    public UserDTO getUserById(Long id) {

        User userId = userRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Usuario não encontrado " + id));
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

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        try {
            User userById = userRepository.findById(id).orElseThrow(() -> new ValidationException("Usuario não encontrado " + id));
            userById = userConverter.converterToEntityUpdate(userById, userDTO);
            return userConverter.converterToDTO(userRepository.save(userById));

        } catch (ValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Falha ao atualizar usaurio: " + id, e);
        }
    }

    @Override
    public void deactivationService(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + id));
        user.setUserStatus(INACTIVE);
        user.setDataAtualizacao(LocalDateTime.now());
        userRepository.save(user);
    }
}



