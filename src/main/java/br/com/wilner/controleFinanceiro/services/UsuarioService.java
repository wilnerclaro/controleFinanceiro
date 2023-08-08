package br.com.wilner.controleFinanceiro.services;

import br.com.wilner.controleFinanceiro.DTO.UsuarioDTO;
import br.com.wilner.controleFinanceiro.entities.Categoria;
import br.com.wilner.controleFinanceiro.entities.Usuario;
import br.com.wilner.controleFinanceiro.repositories.CategoriaRepository;
import br.com.wilner.controleFinanceiro.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;
    CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<UsuarioDTO> findAll() {
        List<Usuario> result = usuarioRepository.findAll();
        return result.stream().map(UsuarioDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioDTO findById(Long id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            return new UsuarioDTO(usuario);
        }else{
            return null;
        }
    }

        @Transactional
	    public UsuarioDTO novoUsuario(UsuarioDTO usuarioDTO){
           Usuario usuario = new Usuario();
           usuario.setAtivo(usuarioDTO.getAtivo());
           usuario.setNome(usuarioDTO.getNome());
           usuario.setEmail(usuarioDTO.getEmail());
           usuario.setSenha(usuarioDTO.getSenha());
           usuario.setDataCriacao(usuarioDTO.getDataCriacao());
           usuario.setDataAtualizacao(usuarioDTO.getDataAtualizacao());



            Usuario novoUsuario = usuarioRepository.save(usuario);
            return new UsuarioDTO(novoUsuario);
        }



}



