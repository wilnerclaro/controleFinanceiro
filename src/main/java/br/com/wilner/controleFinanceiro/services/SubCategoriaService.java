package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.DTO.CategoriaDTO;
import br.com.wilner.controleFinanceiro.DTO.SubCategoriaDTO;
import br.com.wilner.controleFinanceiro.DTO.interfaces.SubCategoriaInfo;
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
    SubCategoriaRepository subCategoriaRepository;

    @Autowired
    CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<SubCategoriaDTO> findAll() {
        List<SubCategoria> results = subCategoriaRepository.findAll();
        return results.stream().map(SubCategoriaDTO::new).toList();

    }

    @Transactional(readOnly = true)
    public SubCategoriaDTO findById(Long id) {
        Optional<SubCategoria> subCategoriaOptional = subCategoriaRepository.findById(id);
        if (subCategoriaOptional.isPresent()) {
            SubCategoria subCategoria = subCategoriaOptional.get();
            return new SubCategoriaDTO(subCategoria);
        } else {
            return null;
        }
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

    @Transactional(readOnly = true)
    public SubCategoriaDTO findSubCategoriaInfoById(Long id) {
        SubCategoriaInfo subCategoriaInfo = subCategoriaRepository.findSubCategoriaInfoById(id);

        if (subCategoriaInfo == null) {
            throw new RuntimeException("Subcategoria não encontrada");
        }

        return createSubCategoriaDTO(subCategoriaInfo);
    }

    private SubCategoriaDTO createSubCategoriaDTO(SubCategoriaInfo subCategoriaInfo) {
        SubCategoriaDTO subCategoriaDTO = new SubCategoriaDTO();
        subCategoriaDTO.setId(subCategoriaInfo.getId());
        subCategoriaDTO.setNome(subCategoriaInfo.getNome());
        subCategoriaDTO.setDescricao(subCategoriaInfo.getDescricao());

        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(subCategoriaInfo.getCategoriaId());
        categoriaDTO.setNome(subCategoriaInfo.getCategoriaNome());

        subCategoriaDTO.setCategoria(categoriaDTO);

        return subCategoriaDTO;
    }
}
