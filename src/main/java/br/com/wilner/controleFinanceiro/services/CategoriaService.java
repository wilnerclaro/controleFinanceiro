package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.DTO.CategoriaDTO;
import br.com.wilner.controleFinanceiro.DTO.UsuarioDTO;
import br.com.wilner.controleFinanceiro.entities.Categoria;
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
    CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<CategoriaDTO> findAll(){
        List<Categoria> results = categoriaRepository.findAll();
        return results.stream().map(CategoriaDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public CategoriaDTO findById(Long id){
        Optional<Categoria> optionalCategoria = categoriaRepository.findById(id);
        if (optionalCategoria.isPresent()) {
            Categoria categoria = optionalCategoria.get();
            return new CategoriaDTO(categoria);
        }else{
            return null;
        }
    }

    @Transactional
    public CategoriaDTO criarCategoria(CategoriaDTO categoriaDTO){
        Categoria categoria = new Categoria();
        categoria.setNome(categoriaDTO.getNome());
        categoria.setTipo(categoriaDTO.getTipo());
        categoria.setDescricao(categoriaDTO.getDescricao());

        Categoria novaCategoria = categoriaRepository.save(categoria);
        return new CategoriaDTO(novaCategoria);
    }

}
