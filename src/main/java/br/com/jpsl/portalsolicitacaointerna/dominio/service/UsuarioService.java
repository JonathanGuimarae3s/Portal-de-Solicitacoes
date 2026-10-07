package br.com.jpsl.portalsolicitacaointerna.dominio.service;

import br.com.jpsl.portalsolicitacaointerna.api.model.dto.usuario.request.UsuarioUpdateRequest;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.PerfilUsuario;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.Setor;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.EntidadeEmUsoException;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.EntidadeNaoEncontradaException;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.NegocioException;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
import br.com.jpsl.portalsolicitacaointerna.dominio.repositorio.UsuarioRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Usuário não encontrado com o ID: " + id)
        );
    }

    @Transactional(readOnly = true)
    public Usuario retornaUsuarioPorId(Long id, Setor setor, PerfilUsuario perfilUsuario, Long idUsuarioLogado) {

        Usuario usuario = null;

        switch (perfilUsuario) {
            case PerfilUsuario.GESTOR:
                usuario = usuarioRepository.findByIdAndSetor(id, setor);
                break;

            case PerfilUsuario.USUARIO:

                if (id.equals(idUsuarioLogado)) {
                    usuario = buscarPorId(id);
                    break;
                } else {
                    throw new AccessDeniedException("Usuário não possui permissão para consultar os dados de outro usuário.");
                }

            case PerfilUsuario.ADMIN:
            default:
                usuario = buscarPorId(id);
                break;
        }

        if (usuario == null) {
            throw new EntidadeNaoEncontradaException("Usuário não encontrado.");
        }

        return usuario;

    }

    @Transactional(readOnly = true)
    public Usuario buscarPorEmail(String email) {

        if (!StringUtils.hasLength(email)) {
            throw new NegocioException("O e-mail do usuário não pode ser vazio.");
        }

        return usuarioRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Page<Usuario> listar(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Usuario> buscaPorSetor(Setor setor, Pageable pageable) {
        return usuarioRepository.findAllBySetor(setor, pageable);
    }

    @Transactional(readOnly = true)
    public Long contabilizarUsuariosAtivos(PerfilUsuario perfilUsuario, Setor setor, Usuario usuarioLogado) {
        return switch (perfilUsuario) {
            case GESTOR -> usuarioRepository.countByAtivoTrueAndSetor(setor);
            case ADMIN -> usuarioRepository.countByAtivoTrue();
            case USUARIO -> usuarioLogado.isAtivo() ? 1L : 0L;
        };
    }

    @Transactional
    public Usuario atualizar(UsuarioUpdateRequest request, Long id, Usuario usuarioLogado) {

        Usuario usuario = buscarPorId(id);

        if (StringUtils.hasLength(request.nome())) {
            usuario.setNome(request.nome());
        }

        if (StringUtils.hasLength(request.senha()) && (usuarioLogado.isAdmin() || usuarioLogado.getId().equals(id))) {
            String senha = passwordEncoder.encode(request.senha());
            usuario.setSenha(senha);
        }

        if (StringUtils.hasLength(request.email())) {

            Usuario usuarioSalvo = buscarPorEmail(request.email());

            if (usuarioSalvo != null && !usuarioSalvo.getId().equals(id)) {
                throw new EntidadeEmUsoException("Já existe um usuário cadastrado com este e-mail.");
            }

            usuario.setEmail(request.email());
        }

        if (usuarioLogado.isAdmin()) {

            if (StringUtils.hasLength(request.setor())) {
                usuario.setSetor(Setor.converterSetorSTRING(request.setor()));
            }

            if (StringUtils.hasLength(request.perfil())) {
                usuario.setPerfil(PerfilUsuario.converterPerfilUsuarioSTRING(request.perfil()));
            }

        } else {
            throw new AccessDeniedException("Usuário não possui permissão para alterar perfil e setor de usuário.");
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario salvar(Usuario usuario ) {
        Usuario usuarioExistente = buscarPorEmail(usuario.getEmail());
        if (usuarioExistente != null && !usuarioExistente.getId().equals(usuario.getId())) {
            throw new EntidadeEmUsoException("Já existe um usuário cadastrado com este e-mail.");
        }

        if (usuario.getSetor() == null) {
            throw new NegocioException("O setor do usuário é obrigatório.");
        }

        if (!StringUtils.hasLength(usuario.getNome())) {
            throw new NegocioException("O nome do usuário é obrigatório.");
        }

        return usuarioRepository.save(usuario);
    }



    @Transactional
    public Usuario atualizarStatus(Long id, @NotNull Boolean ativo) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(ativo);
        return usuarioRepository.save(usuario);
    }

}
