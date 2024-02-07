package br.com.wilner.controleFinanceiro.controllers;

import br.com.wilner.controleFinanceiro.DTO.TransactionDTO;
import br.com.wilner.controleFinanceiro.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Cria um nava transação", method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transação criada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao  salvar transação")
    })
    @PostMapping("/new")
    public ResponseEntity<TransactionDTO> saveTransaction(@RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transactionService.saveTransaction(transactionDTO));
    }

    @Operation(summary = "Fazer um update de uma transacao", method = "PUT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Trasação atuaizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao  realizar busca dos dados")
    })
    @PutMapping("/update")
    public ResponseEntity<TransactionDTO> updateTransaction(@RequestParam Long id, @RequestBody TransactionDTO transactionDTO) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, transactionDTO));
    }

    @Operation(summary = "Fazer um delete de uma transação", method = "DELETE")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transação deletada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao  realizar busca dos dados")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteTransaction(@RequestParam Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Busca Transação Por Id", method = "GET")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro ao  realizar busca dos dados")
    })

    @GetMapping("/user")
    public ResponseEntity<TransactionDTO> getTransactionByUserId(@RequestParam Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }


}
