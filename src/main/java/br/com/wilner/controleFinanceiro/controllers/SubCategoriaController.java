package br.com.wilner.controleFinanceiro.controllers;

import br.com.wilner.controleFinanceiro.DTO.SubCategoriaDTO;
import br.com.wilner.controleFinanceiro.services.SubCategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/sub_categoria")
public class SubCategoriaController {
    
    @Autowired
    SubCategoriaService subCategoriaService;
    
    @GetMapping
    public ResponseEntity<List<SubCategoriaDTO>> findAll(){
        List<SubCategoriaDTO> subCategoriaDTO = subCategoriaService.findAll();
        return ResponseEntity.ok(subCategoriaDTO);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SubCategoriaDTO>findById(@PathVariable Long id){
        SubCategoriaDTO subCategoriaDTO = subCategoriaService.findById(id);
        if (subCategoriaDTO !=null) {
            return ResponseEntity.ok(subCategoriaDTO);

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<SubCategoriaDTO>criarSubCategoria(@RequestBody SubCategoriaDTO subCategoriaDTO){
        SubCategoriaDTO novaSubCategoriaDTO = subCategoriaService.criarSubCategoria(subCategoriaDTO);
        String uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(subCategoriaDTO.getId())
                .toUriString();

        // Retornar a resposta com o status 201 (Created) e a URI do novo recurso no cabeçalho "Location"
        return ResponseEntity.created(URI.create(uri)).body(novaSubCategoriaDTO);

    }

    @PostMapping("/lancamentos")
    public ResponseEntity<SubCategoriaDTO> novoLancamento (@RequestBody SubCategoriaDTO subCategoriaDTO){
        SubCategoriaDTO novoLancamentoDTO = subCategoriaService.lancarInformacao(subCategoriaDTO);
        String uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(subCategoriaDTO.getId())
                .toUriString();
        return ResponseEntity.created(URI.create(uri)).body(novoLancamentoDTO);
    }


}
