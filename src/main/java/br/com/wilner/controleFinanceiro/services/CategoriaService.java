package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.entities.Categoria;
import br.com.wilner.controleFinanceiro.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    CategoriaRepository categoriaRepository;

    public List<Categoria> findAllCategorias(){
        return categoriaRepository.findAll();
    }
}
