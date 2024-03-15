package br.com.wilner.controleFinanceiro.controllers;

import br.com.wilner.controleFinanceiro.DTO.UserDTO;
import br.com.wilner.controleFinanceiro.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "user-controller")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Busca Todos os Usuarios", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao  realizar busca dos dados")
    })
    @GetMapping("")
    public ResponseEntity<List<UserDTO>> findAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Busca Usuario Por Nome", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao  realizar busca dos dados")
    })
    @GetMapping("/user")
    public ResponseEntity<UserDTO> getUserById(@RequestParam String name) {
        return ResponseEntity.ok(userService.getUserByName(name));
    }

    @Operation(summary = "Cria um novo usuario", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario criado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao  salvar usuario")
    })
    @PostMapping("/new")
    public ResponseEntity<UserDTO> newUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.saveUser(userDTO));
    }

    @Operation(summary = "Fazer um update de um usuario", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario atuaizado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao  realizar busca dos dados")
    })
    @PutMapping("/update")
    public ResponseEntity<UserDTO> updateUser(@RequestParam String name, @RequestBody UserDTO userDto) {
        return ResponseEntity.ok(userService.updateUser(name, userDto));
    }

    @Operation(summary = "Fazer um delete de um usuario", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario deletado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao  realizar busca dos dados")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestParam String name) {
        userService.deactivationService(name);
        return ResponseEntity.accepted().build();
    }


}
