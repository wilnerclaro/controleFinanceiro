package br.com.wilner.controleFinanceiro.controllers;

import br.com.wilner.controleFinanceiro.DTO.UsuarioDTO;
import br.com.wilner.controleFinanceiro.entities.Usuario;
import br.com.wilner.controleFinanceiro.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/usuario")
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        List<UsuarioDTO> usuariosDTO = usuarioService.findAll();
        return ResponseEntity.ok(usuariosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable Long id) {
        UsuarioDTO usuarioDTO = usuarioService.findById(id);
        if (usuarioDTO !=null) {
            return ResponseEntity.ok(usuarioDTO);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO>criarUsuario(@RequestBody UsuarioDTO usuarioDTO){
       UsuarioDTO novoUsuarioDTO = usuarioService.novoUsuario(usuarioDTO);
        String uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuarioDTO.getId())
                .toUriString();

        // Retornar a resposta com o status 201 (Created) e a URI do novo recurso no cabeçalho "Location"
        return ResponseEntity.created(URI.create(uri)).body(novoUsuarioDTO);
    }
}
