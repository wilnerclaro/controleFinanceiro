package br.com.wilner.controleFinanceiro.controllers;

import br.com.wilner.controleFinanceiro.DTO.CategoriaDTO;
import br.com.wilner.controleFinanceiro.entities.Categoria;
import br.com.wilner.controleFinanceiro.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> findAll(){
        List<CategoriaDTO> categoriaDTO = categoriaService.findAll();
        return ResponseEntity.ok(categoriaDTO);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<CategoriaDTO>findById(@PathVariable Long id){
        CategoriaDTO categoriaDTO = categoriaService.findById(id);
        if (categoriaDTO !=null) {
            return ResponseEntity.ok(categoriaDTO);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<CategoriaDTO>criarCategoria(@RequestBody CategoriaDTO categoriaDTO){
        CategoriaDTO novaCategoriaDTO = categoriaService.criarCategoria(categoriaDTO);
        String uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoriaDTO.getId())
                .toUriString();

        // Retornar a resposta com o status 201 (Created) e a URI do novo recurso no cabeçalho "Location"
        return ResponseEntity.created(URI.create(uri)).body(novaCategoriaDTO);
    }


}
