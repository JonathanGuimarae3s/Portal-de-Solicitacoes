package br.com.jpsl.portalsolicitacaointerna.dominio.service;

import br.com.jpsl.portalsolicitacaointerna.api.model.dto.usuario.request.UsuarioRequest;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.usuario.request.UsuarioStatusRequest;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.EntidadeNaoEncontradaException;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.NegocioException;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Solicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
import br.com.jpsl.portalsolicitacaointerna.dominio.repositorio.UsuarioRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Usuario.js não encontrado com id: " + id)
        );
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) {

        if (!StringUtils.hasLength(email)) {
            throw new NegocioException("O email do usuário não pode ser vazio.");
        }

        return usuarioRepository.findByEmail(email);
    }

    @Transactional
    public Usuario atualizar(UsuarioRequest request, Long id) {
        Usuario usuario = buscarPorId(id);

        if (StringUtils.hasLength(request.nome())) {
            usuario.setNome(request.nome());
        }

        if (StringUtils.hasLength(request.email())) {
            Usuario usuarioSalvo = buscarPorEmail(request.email());
            if (usuarioSalvo != null && !usuarioSalvo.getId().equals(id)) {
                throw new NegocioException("Email inválido ou email já cadastrado no sistema.");
            }

            usuario.setEmail(request.email());
        }

        if (StringUtils.hasLength(request.setor())) {
            usuario.setSetor(request.setor());
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario salvar(Usuario usuario) {
        Usuario usuarioExistente = buscarPorEmail(usuario.getEmail());
        if (usuarioExistente != null && !usuarioExistente.getId().equals(usuario.getId())) {
            throw new NegocioException("Email inválido ou email já cadastrado no sistema.");
        }

        if (!StringUtils.hasLength(usuario.getSetor())) {
            throw new NegocioException("O setor do usuário é obrigatório.");
        }

        if (!StringUtils.hasLength(usuario.getNome())) {
            throw new NegocioException("O nome do usuário é obrigatório.");
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Page<Usuario> listar(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Usuario> buscaPorSetor(String setor, Pageable pageable) {
        return usuarioRepository.findAllBySetor(setor, pageable);
    }

    @Transactional
    public Usuario atualizarStatus(Long id, @NotNull Boolean ativo) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(ativo);
        return usuarioRepository.save(usuario);
    }
}
