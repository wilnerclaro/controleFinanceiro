package br.com.wilner.controleFinanceiro.builder;

import br.com.wilner.controleFinanceiro.DTO.CategoryDTO;

public class CategoryDTOBuilder {
    private CategoryDTO elemento;

    private CategoryDTOBuilder() {
    }

    public static CategoryDTOBuilder umCategoryDTO() {
        CategoryDTOBuilder builder = new CategoryDTOBuilder();
        inicializarDadosPadroes(builder);
        return builder;
    }

    public static void inicializarDadosPadroes(CategoryDTOBuilder builder) {
        builder.elemento = new CategoryDTO();
        CategoryDTO elemento = builder.elemento;
        elemento.setCategoryName("Moradia");
        elemento.setDescription("Categoria Moradia");
    }

    public CategoryDTOBuilder comCategoryName(String param) {
        elemento.setCategoryName(param);
        return this;
    }

    public CategoryDTOBuilder comDescription(String param) {
        elemento.setDescription(param);
        return this;
    }

    public CategoryDTO agora() {
        return elemento;
    }
}
