package br.com.jpsl.portalsolicitacaointerna.dominio.service;

import br.com.jpsl.portalsolicitacaointerna.api.model.dto.solicitacao.request.SolicitacaoRequest;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.PerfilUsuario;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.PrioridadeSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.Setor;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.StatusSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.EntidadeNaoEncontradaException;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.NegocioException;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Solicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.TipoSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
import br.com.jpsl.portalsolicitacaointerna.dominio.repositorio.SolicitacaoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;

@Service
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;

    private final TipoSolicitacaoService tipoSolicitacaoService;

    private final UsuarioService usuarioService;

    public SolicitacaoService(SolicitacaoRepository solicitacaoRepository, TipoSolicitacaoService tipoSolicitacaoService, UsuarioService usuarioService) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.tipoSolicitacaoService = tipoSolicitacaoService;
        this.usuarioService = usuarioService;
    }

    @Transactional(readOnly = true)
    public Solicitacao buscarPorId(Long id) {
        return solicitacaoRepository.findById(id).orElseThrow(() ->
                new EntidadeNaoEncontradaException("Solicitação não encontrada com o ID: " + id));
    }


    @Transactional(readOnly = true)
    public Solicitacao retornaSolicitacaoPorId(Long id, Setor setor, Usuario usuario, PerfilUsuario perfilUsuario) {

        Solicitacao solicitacao = null;

        switch (perfilUsuario) {
            case PerfilUsuario.GESTOR:
                solicitacao = solicitacaoRepository.findByIdAndUsuarioSetor(id, setor);
                break;

            case PerfilUsuario.ADMIN:

                solicitacao = buscarPorId(id);
                break;

            case PerfilUsuario.USUARIO:
            default:


                solicitacao = solicitacaoRepository.findByIdAndUsuario(id, usuario);
                break;
        }

        if (solicitacao == null) {
            throw new EntidadeNaoEncontradaException("Solicitação não encontrada com o ID: " + id);
        }

        return solicitacao;


    }


    @Transactional(readOnly = true)
    public Page<Solicitacao> filtrarSolicitacoes(String status, String prioridade, Usuario usuario, Setor setor, Pageable pageable) {
        try {
            StatusSolicitacao statusEnum = null;
            if (StringUtils.hasLength(status)) {
                statusEnum = StatusSolicitacao.valueOf(status.toUpperCase());
            }

            PrioridadeSolicitacao prioridadeEnum = null;
            if (StringUtils.hasLength(prioridade)) {
                prioridadeEnum = PrioridadeSolicitacao.valueOf(prioridade.toUpperCase());
            }

            return solicitacaoRepository.filtrar(statusEnum, prioridadeEnum, usuario, setor, pageable);
        } catch (IllegalArgumentException e) {
            throw new NegocioException("Status ou prioridade inválidos. Valores válidos: " + "Status: " +
                    Arrays.toString(StatusSolicitacao.values()) + ", " + "Prioridade: " + Arrays.toString(PrioridadeSolicitacao.values()));
        }
    }

    @Transactional(readOnly = true)
    public Page<Solicitacao> listarTodas(Pageable pageable) {
        return solicitacaoRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Solicitacao> listarTodasPorSetor(Setor setor, Pageable pageable) {
        return solicitacaoRepository.findAllByUsuarioSetor(setor, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Solicitacao> listarTodasPorUsuario(Usuario usuario, Pageable pageable) {
        return solicitacaoRepository.findAllByUsuario(usuario, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Solicitacao> retornaSolicitacoes(String status, String prioridade, Pageable pageable, PerfilUsuario perfilUsuario, Setor setor, Usuario usuarioLogado) {
        switch (perfilUsuario) {
            case PerfilUsuario.GESTOR:

                if (StringUtils.hasLength(status) || StringUtils.hasLength(prioridade)) {
                    return filtrarSolicitacoes(status, prioridade, null, setor, pageable);
                }

                return listarTodasPorSetor(setor, pageable);

            case PerfilUsuario.ADMIN:

                if (StringUtils.hasLength(status) || StringUtils.hasLength(prioridade)) {
                    return filtrarSolicitacoes(status, prioridade, null, null, pageable);
                }

                return listarTodas(pageable);

            case PerfilUsuario.USUARIO:
            default:

                if (StringUtils.hasLength(status) || StringUtils.hasLength(prioridade)) {
                    return filtrarSolicitacoes(status, prioridade, usuarioLogado, null, pageable);
                }

                return listarTodasPorUsuario(usuarioLogado, pageable);
        }
    }

    @Transactional
    public Solicitacao salvar(SolicitacaoRequest request, Usuario usuarioLogado) {
        Long idUsuario = request.usuario().id();
        Usuario usuario = usuarioService.buscarPorId(idUsuario);


        Usuario usuarioDestino =
                usuarioService.buscarPorId(request.usuario().id());

        validarPermissao(usuarioLogado, usuarioDestino, "criar");

        if (!usuario.isAtivo()) {
            throw new NegocioException("Usuário inativo não pode abrir uma solicitação.");
        }

        Solicitacao solicitacao = new Solicitacao();

        if (!StringUtils.hasLength(request.titulo())) {
            throw new NegocioException("O título é obrigatório.");
        }

        if (!StringUtils.hasLength(request.descricao())) {
            throw new NegocioException("A descrição é obrigatória.");
        }

        if (StringUtils.hasLength(request.prioridade())) {
            PrioridadeSolicitacao prioridade = PrioridadeSolicitacao.fromString(request.prioridade())
                    .orElseThrow(() -> new NegocioException("Prioridade inválida. Valores válidos: " +
                            "Prioridade: " + Arrays.toString(PrioridadeSolicitacao.values())));

            solicitacao.setPrioridade(prioridade);
        }

        Long idTipoSolicitacao = request.tipo().id();

        TipoSolicitacao tipoSolicitacao = tipoSolicitacaoService.buscarPorId(idTipoSolicitacao);

        solicitacao.setTitulo(request.titulo());
        solicitacao.setDescricao(request.descricao());
        solicitacao.setTipo(tipoSolicitacao);
        solicitacao.setUsuario(usuario);

        return solicitacaoRepository.saveAndFlush(solicitacao);
    }

    private void validarPermissao(Usuario usuarioLogado, Usuario usuarioDestino, String metodo) {

        boolean invocandoPraSiMesmo =
                usuarioLogado.getId().equals(usuarioDestino.getId());

        if (invocandoPraSiMesmo) {
            return;
        }


        switch (usuarioLogado.getPerfil()) {
            case USUARIO -> throw new AccessDeniedException(
                    "Usuário só pode criar solicitações para si mesmo."
            );

            case GESTOR -> {
                boolean destinoEhUsuario =
                        usuarioDestino.getPerfil() == PerfilUsuario.USUARIO;

                boolean mesmoSetor =
                        usuarioLogado.getSetor() == usuarioDestino.getSetor();

                if (!destinoEhUsuario || !mesmoSetor) {
                    throw new AccessDeniedException(
                            "Gestor só pode criar solicitações para usuários do seu setor."
                    );
                }
            }

            case ADMIN -> {
                boolean destinoEhOutroAdmin =
                        usuarioDestino.getPerfil() == PerfilUsuario.ADMIN;

                if (destinoEhOutroAdmin) {
                    throw new AccessDeniedException(
                            "Administrador não pode criar solicitação para outro administrador."
                    );
                }
            }
        }
    }

    private Solicitacao buscarSolicitacaoPermitida(
            Long id,
            Usuario usuarioLogado) {

        return switch (usuarioLogado.getPerfil()) {
            case ADMIN -> buscarPorId(id);

            case GESTOR -> solicitacaoRepository.findByIdAndUsuarioSetor(id, usuarioLogado.getSetor());

            case USUARIO -> solicitacaoRepository.findByIdAndUsuario(id, usuarioLogado);
        };
    }

    @Transactional
    public Solicitacao atualizar(Long id, SolicitacaoRequest request, Usuario usuario) {
        Solicitacao solicitacao = buscarSolicitacaoPermitida(id, usuario);

        if (solicitacao == null) {
            throw new EntidadeNaoEncontradaException(
                    "Solicitação não encontrada ou sem permissão de acesso."
            );
        }


        if (solicitacao.getStatus().equals(StatusSolicitacao.CONCLUIDA)) {
            throw new NegocioException("Esta solicitação já está concluída. Não é possível atualizá-la.");
        }

        if (request.tipo().id() != null && !request.tipo().id().equals(solicitacao.getTipo().getId())) {
            TipoSolicitacao tipoSolicitacao = tipoSolicitacaoService.buscarPorId(request.tipo().id());
            solicitacao.setTipo(tipoSolicitacao);
        }

        if (StringUtils.hasText(request.status())) {
            StatusSolicitacao novoStatus = StatusSolicitacao.fromString(request.status()).orElseThrow(() -> new NegocioException("Status inválido. Valores válidos: " +
                    "Status: " + Arrays.toString(StatusSolicitacao.values())));

            StatusSolicitacao statusAtual = solicitacao.getStatus();
            if (!statusAtual.equals(novoStatus) && !statusAtual.podeTransicionarPara(novoStatus)) {
                throw new NegocioException("Não é permitido alterar o status de %s para %s.".formatted(statusAtual,
                        novoStatus)
                );
            }

            solicitacao.setStatus(novoStatus);
        }

        if (StringUtils.hasText(request.prioridade())) {
            PrioridadeSolicitacao prioridade = PrioridadeSolicitacao.fromString(request.prioridade())
                    .orElseThrow(() -> new NegocioException("Prioridade inválida. Valores válidos: " +
                            "Prioridade: " + Arrays.toString(PrioridadeSolicitacao.values())));

            solicitacao.setPrioridade(prioridade);
        }

        solicitacao.setTitulo(request.titulo());
        solicitacao.setDescricao(request.descricao());

        return solicitacaoRepository.save(solicitacao);
    }

    @Transactional(readOnly = true)
    public Long contabilizaSolicitacoesPorStatus(StatusSolicitacao statusSolicitacao) {
        return solicitacaoRepository.countSolicitacaoByStatus(statusSolicitacao);
    }

    @Transactional(readOnly = true)
    public Long contabilizaSolicitacoesPorStatus(StatusSolicitacao statusSolicitacao,
                                                 PerfilUsuario perfilUsuario,
                                                 Setor setor,
                                                 Usuario usuarioLogado) {
        return switch (perfilUsuario) {
            case GESTOR -> solicitacaoRepository.countByStatusAndUsuarioSetor(statusSolicitacao, setor);
            case ADMIN -> solicitacaoRepository.countSolicitacaoByStatus(statusSolicitacao);
            case USUARIO -> solicitacaoRepository.countByStatusAndUsuario(statusSolicitacao, usuarioLogado);
        };
    }

    @Transactional(readOnly = true)
    public Long contabilizaSolicitacoes() {
        return solicitacaoRepository.count();
    }

    @Transactional(readOnly = true)
    public Long contabilizaSolicitacoes(PerfilUsuario perfilUsuario, Setor setor, Usuario usuarioLogado) {
        return switch (perfilUsuario) {
            case GESTOR -> solicitacaoRepository.countByUsuarioSetor(setor);
            case ADMIN -> solicitacaoRepository.count();
            case USUARIO -> solicitacaoRepository.countByUsuario(usuarioLogado);
        };
    }


}
