package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.DTO.UserDTO;
import br.com.wilner.controleFinanceiro.entities.User;
import br.com.wilner.controleFinanceiro.exception.ValidationException;
import br.com.wilner.controleFinanceiro.repositories.UserRepository;
import br.com.wilner.controleFinanceiro.util.converter.UserConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class UserService {

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

    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ValidationException("Usuario não encontrado: " + id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao deletar usuário: " + id, e);
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


}



