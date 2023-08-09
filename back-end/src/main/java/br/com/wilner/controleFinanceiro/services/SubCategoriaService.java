package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.DTO.SubCategoriaDTO;
import br.com.wilner.controleFinanceiro.entities.Categoria;
import br.com.wilner.controleFinanceiro.entities.SubCategoria;
import br.com.wilner.controleFinanceiro.repositories.CategoriaRepository;
import br.com.wilner.controleFinanceiro.repositories.SubCategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SubCategoriaService {

    @Autowired
    private  SubCategoriaRepository subCategoriaRepository;

    @Autowired
    CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<SubCategoriaDTO> findAll() {
        List<SubCategoria> results = subCategoriaRepository.findAll();
        return results.stream().map(SubCategoriaDTO::new).toList();

    }

    @Transactional(readOnly = true)
    public SubCategoriaDTO findById(Long id) {
        SubCategoria subCategoria = subCategoriaRepository.findById(id).get();
        return new SubCategoriaDTO(subCategoria);
    }

    @Transactional
    public SubCategoriaDTO criarSubCategoria(SubCategoriaDTO subCategoriaDTO) {
        SubCategoria subCategoria = new SubCategoria();
        subCategoria.setDescricao(subCategoriaDTO.getDescricao());
        subCategoria.setNome(subCategoriaDTO.getNome());

        // Recupere a categoria pelo ID fornecido no DTO ou pelo nome da categoria, dependendo de como você deseja fornecer a categoria na requisição
        Categoria categoria = categoriaRepository.findById(subCategoriaDTO.getCategoria().getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        subCategoria.setCategoria(categoria);

        SubCategoria novaSubCategoria = subCategoriaRepository.save(subCategoria);
        return new SubCategoriaDTO(novaSubCategoria);
    }

    @Transactional
    public SubCategoriaDTO lancarInformacao(SubCategoriaDTO subCategoriaDTO) {
        SubCategoria subCategoria = new SubCategoria();

        subCategoria.setDescricao(subCategoriaDTO.getDescricao());
        subCategoria.setNome(subCategoriaDTO.getNome());
        subCategoria.setPrevisto(subCategoriaDTO.getPrevisto());
        subCategoria.setRealizado(subCategoriaDTO.getRealizado());
        subCategoria.setMes(subCategoriaDTO.getMes());
        subCategoria.setTipo(subCategoriaDTO.getTipo());

        Categoria categoria = categoriaRepository.findById(subCategoriaDTO.getCategoria().getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        subCategoria.setCategoria(categoria);

        SubCategoria novoLancamento = subCategoriaRepository.save(subCategoria);

        return  new SubCategoriaDTO(novoLancamento);

    }

}
