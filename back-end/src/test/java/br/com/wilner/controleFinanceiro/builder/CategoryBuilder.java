package br.com.wilner.controleFinanceiro.builder;

import br.com.wilner.controleFinanceiro.entities.Category.Category;
import br.com.wilner.controleFinanceiro.entities.Transaction.Transaction;

import java.time.LocalDateTime;
import java.util.Arrays;

public class CategoryBuilder {
    private Category elemento;

    private CategoryBuilder() {
    }

    public static CategoryBuilder umCategory() {
        CategoryBuilder builder = new CategoryBuilder();
        inicializarDadosPadroes(builder);
        return builder;
    }

    public static void inicializarDadosPadroes(CategoryBuilder builder) {
        builder.elemento = new Category();
        Category elemento = builder.elemento;


        elemento.setId(1L);
        elemento.setName("Moradia");
        elemento.setDescription("Gastos com Moradia");
        elemento.setCreationDate(LocalDateTime.now());
        elemento.setUpdateDate(LocalDateTime.now());
        // elemento.setTransactions(Collections.singletonList(TransactionBuilder.umTransaction().agora()));
        elemento.setIsActive(true);
    }

    public CategoryBuilder comId(Long param) {
        elemento.setId(param);
        return this;
    }

    public CategoryBuilder comName(String param) {
        elemento.setName(param);
        return this;
    }

    public CategoryBuilder comDescription(String param) {
        elemento.setDescription(param);
        return this;
    }

    public CategoryBuilder comCreationDate(LocalDateTime param) {
        elemento.setCreationDate(param);
        return this;
    }

    public CategoryBuilder comUpdateDate(LocalDateTime param) {
        elemento.setUpdateDate(param);
        return this;
    }

    public CategoryBuilder comListaTransactions(Transaction... params) {
        elemento.setTransactions(Arrays.asList(params));
        return this;
    }

    public CategoryBuilder comIsActive(Boolean param) {
        elemento.setIsActive(param);
        return this;
    }

    public Category agora() {
        return elemento;
    }
}
