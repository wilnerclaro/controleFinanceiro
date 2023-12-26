package br.com.wilner.controleFinanceiro.exception;

public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 3759575457881367859L;

    public ValidationException(String message) {
        super(message);
    }

}