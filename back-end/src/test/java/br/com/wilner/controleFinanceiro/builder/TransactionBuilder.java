package br.com.wilner.controleFinanceiro.builder;

import br.com.wilner.controleFinanceiro.entities.Category.Category;
import br.com.wilner.controleFinanceiro.entities.Transaction.Transaction;
import br.com.wilner.controleFinanceiro.entities.User.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public class TransactionBuilder {
    private Transaction elemento;

    private TransactionBuilder() {
    }

    public static TransactionBuilder umTransaction() {
        TransactionBuilder builder = new TransactionBuilder();
        inicializarDadosPadroes(builder);
        return builder;
    }

    public static void inicializarDadosPadroes(TransactionBuilder builder) {
        builder.elemento = new Transaction();
        Transaction elemento = builder.elemento;


        elemento.setId(1L);
        elemento.setTransactionType("Despesa");
        elemento.setTransactionValue(BigDecimal.valueOf(100));
        elemento.setTransactionDate(LocalDateTime.now());
        elemento.setUpdateDate(LocalDateTime.now());
        elemento.setCategory(CategoryBuilder.umCategory().agora());
        elemento.setDescription("Gasto com Aluguel");
        elemento.setUser(UserBuilder.umUser().agora());
        elemento.setPaymentMethod("Pix");

    }

    public TransactionBuilder comId(Long param) {
        elemento.setId(param);
        return this;
    }

    public TransactionBuilder comTransactionType(String param) {
        elemento.setTransactionType(param);
        return this;
    }

    public TransactionBuilder comTransactionValue(BigDecimal param) {
        elemento.setTransactionValue(param);
        return this;
    }

    public TransactionBuilder comTransactionDate(LocalDateTime param) {
        elemento.setTransactionDate(param);
        return this;
    }

    public TransactionBuilder comUpdateDate(LocalDateTime param) {
        elemento.setUpdateDate(param);
        return this;
    }

    public TransactionBuilder comCategory(Category param) {
        elemento.setCategory(param);
        return this;
    }

    public TransactionBuilder comDescription(String param) {
        elemento.setDescription(param);
        return this;
    }

    public TransactionBuilder comUser(User param) {
        elemento.setUser(param);
        return this;
    }

    public TransactionBuilder comPaymentMethod(String param) {
        elemento.setPaymentMethod(param);
        return this;
    }

    public Transaction agora() {
        return elemento;
    }
}

