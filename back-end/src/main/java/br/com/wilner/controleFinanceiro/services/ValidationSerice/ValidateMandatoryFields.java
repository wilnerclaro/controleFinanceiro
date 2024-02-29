package br.com.wilner.controleFinanceiro.services.ValidationSerice;

public interface ValidateMandatoryFields<T> {
    void checkValidFields(T target);

}
