package br.com.wilner.controleFinanceiro.controllers;

import br.com.wilner.controleFinanceiro.entities.Category.CategoryRequestDTO;
import br.com.wilner.controleFinanceiro.entities.Category.CategoryResponseDTO;
import br.com.wilner.controleFinanceiro.entities.Category.CategoryTotals;
import br.com.wilner.controleFinanceiro.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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
            @ApiResponse(responseCode = "400", description = "Erro ao  salvar categoria")
    })
    @PostMapping("/new")
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO categoryDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.saveCategory(categoryDTO));
    }

    @Operation(summary = "Busca Todas as Categorias", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao  realizar busca dos dados")
    })
    @GetMapping("")
    public ResponseEntity<List<CategoryResponseDTO>> findAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @Operation(summary = "Busca Categoria Por Nome", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao  realizar busca dos dados")
    })
    @GetMapping("/category")
    public ResponseEntity<CategoryResponseDTO> findCategoryByName(@RequestParam String name) {
        return ResponseEntity.ok(categoryService.getCategoryByName(name));
    }

    @Operation(summary = "Fazer um delete de uma Categoria", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria deletada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao  realizar busca dos dados")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<CategoryRequestDTO> deleteCategory(@RequestParam String name) {
        categoryService.deactivationService(name);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Realiza a soma por categoria", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Soma realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Falha ao realizar soma dos valores")
    })
    @GetMapping("/totals/{categoryName}")
    public ResponseEntity<CategoryTotals> getCategoryTotals(@PathVariable String categoryName) {
        CategoryTotals totals = categoryService.calculateTotalsForCategory(categoryName);
        return ResponseEntity.ok(totals);
    }

    @Operation(summary = "Fazer atualização de uma Categoria", method = "PATCH")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao  realizar atualização dos dados")
    })
    @PatchMapping("/category-update")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@Valid @RequestParam String name, @RequestBody CategoryRequestDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.updateCategory(name, categoryDTO));
    }
}


