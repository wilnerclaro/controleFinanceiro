package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.DTO.CategoriaDTO;
import br.com.wilner.controleFinanceiro.DTO.SubCategoriaDTO;
import br.com.wilner.controleFinanceiro.DTO.UsuarioDTO;
import br.com.wilner.controleFinanceiro.entities.Categoria;
import br.com.wilner.controleFinanceiro.entities.SubCategoria;
import br.com.wilner.controleFinanceiro.entities.Usuario;
import br.com.wilner.controleFinanceiro.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<CategoriaDTO> findAll(){
        List<Categoria> results = categoriaRepository.findAll();
        return results.stream().map(CategoriaDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public CategoriaDTO findById(Long id){
        Categoria categoria = categoriaRepository.findById(id).get();
        return new CategoriaDTO(categoria);
    }

    @Transactional
    public CategoriaDTO criarCategoria(CategoriaDTO categoriaDTO){
        Categoria categoria = new Categoria();
        categoria.setNome(categoriaDTO.getNome());
        categoria.setDescricao(categoriaDTO.getDescricao());

        Categoria novaCategoria = categoriaRepository.save(categoria);
        return new CategoriaDTO(novaCategoria);
    }

}
