package br.com.jpsl.portalsolicitacaointerna.dominio.service;

import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.EntidadeNaoEncontradaException;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.NegocioException;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
import br.com.jpsl.portalsolicitacaointerna.dominio.repositorio.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Usuario não encontrado com id: " + id)
        );
    }

    public Usuario buscarPorEmail(String email) {

        if (!StringUtils.hasLength(email)) {
            throw new NegocioException("O email do usuário não pode ser vazio.");
        }

        return usuarioRepository.findByEmail(email);
    }

    public Usuario adicionarUsuario(Usuario usuario) {

        if (buscarPorEmail(usuario.getEmail()) != null) {
            throw new NegocioException("Já existe um usuário com o email: " + usuario.getEmail());
        }

        if (!StringUtils.hasLength(usuario.getSetor())) {
            throw new NegocioException("O setor do usuário é obrigatório.");
        }

        if (!StringUtils.hasLength(usuario.getNome())) {
            throw new NegocioException("O nome do usuário é obrigatório.");
        }

        return usuarioRepository.save(usuario);
    }
}
