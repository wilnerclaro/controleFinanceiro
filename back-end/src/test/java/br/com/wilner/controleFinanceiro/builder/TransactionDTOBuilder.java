package br.com.wilner.controleFinanceiro.builder;

import br.com.wilner.controleFinanceiro.entities.Transaction.TransactionDTO;

import java.math.BigDecimal;


public class TransactionDTOBuilder {
    private TransactionDTO elemento;

    private TransactionDTOBuilder() {
    }

    public static TransactionDTOBuilder umTransactionDTO() {
        TransactionDTOBuilder builder = new TransactionDTOBuilder();
        inicializarDadosPadroes(builder);
        return builder;
    }

    public static void inicializarDadosPadroes(TransactionDTOBuilder builder) {
        builder.elemento = new TransactionDTO();
        TransactionDTO elemento = builder.elemento;


        elemento.setTransactionType("Despesa");
        elemento.setTransactionValue(BigDecimal.valueOf(100));
        elemento.setCategoryName("Moradia");
        elemento.setDescription("Gasto com Aluguel");
        elemento.setUserName("Novo Usuário");
        elemento.setPaymentMethod("Pix");

    }

    public TransactionDTOBuilder comTransactionType(String param) {
        elemento.setTransactionType(param);
        return this;
    }

    public TransactionDTOBuilder comTransactionValue(BigDecimal param) {
        elemento.setTransactionValue(param);
        return this;
    }

    public TransactionDTOBuilder comCategoryName(String param) {
        elemento.setCategoryName(param);
        return this;
    }

    public TransactionDTOBuilder comDescription(String param) {
        elemento.setDescription(param);
        return this;
    }

    public TransactionDTOBuilder comUserName(String param) {
        elemento.setUserName(param);
        return this;
    }

    public TransactionDTOBuilder comPaymentMethod(String param) {
        elemento.setPaymentMethod(param);
        return this;
    }


    public TransactionDTO agora() {
        return elemento;
    }
}