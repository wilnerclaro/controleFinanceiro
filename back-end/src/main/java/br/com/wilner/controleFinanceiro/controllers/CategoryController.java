package br.com.wilner.controleFinanceiro.controllers;

import br.com.wilner.controleFinanceiro.DTO.CategoryDTO;
import br.com.wilner.controleFinanceiro.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "Cria um nava categoria", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao  salvar categoria")
    })
    @PostMapping("/new")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.saveCategory(categoryDTO));
    }

    @Operation(summary = "Busca Todas as Categorias", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao  realizar busca dos dados")
    })
    @GetMapping("")
    public ResponseEntity<List<CategoryDTO>> findAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Operation(summary = "Busca Categoria Por Nome", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao  realizar busca dos dados")
    })
    @GetMapping("/category")
    public ResponseEntity<CategoryDTO> findCategoryByName(@RequestParam String name) {
        return ResponseEntity.ok(categoryService.getCategoryByName(name));
    }

    @Operation(summary = "Fazer um delete de uma Categoria", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria deletada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao  realizar busca dos dados")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<CategoryDTO> deleteCategory(@RequestParam String name) {
        categoryService.deactivationService(name);
        return ResponseEntity.accepted().build();
    }
}


