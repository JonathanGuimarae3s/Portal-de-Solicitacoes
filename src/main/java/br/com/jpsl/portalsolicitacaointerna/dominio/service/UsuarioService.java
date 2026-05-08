package br.com.jpsl.portalsolicitacaointerna.dominio.service;

import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.EntidadeNaoEncontradaException;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
import br.com.jpsl.portalsolicitacaointerna.dominio.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    public Usuario buscarPorId(Long id){
        return  usuarioRepository.findById(id).orElseThrow(
                ()-> new EntidadeNaoEncontradaException("Usuario não encontrado com id: " + id)
        );
    }

    public Usuario adicionarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}
