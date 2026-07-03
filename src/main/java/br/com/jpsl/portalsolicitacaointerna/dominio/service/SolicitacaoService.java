package br.com.jpsl.portalsolicitacaointerna.dominio.service;

import br.com.jpsl.portalsolicitacaointerna.api.model.dto.solicitacao.request.SolicitacaoRequest;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.PrioridadeSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.StatusSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.EntidadeNaoEncontradaException;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.NegocioException;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Solicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.TipoSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
import br.com.jpsl.portalsolicitacaointerna.dominio.repositorio.SolicitacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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

    public Solicitacao buscarPorId(Long id) {
        return solicitacaoRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Solicitacao não encontrado com id: " + id));
    }

    @Transactional
    public Solicitacao salvar(SolicitacaoRequest request) {
        Long idUsuario = request.usuario().id();
        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        if (!usuario.isAtivo()){
            throw new NegocioException("Usúario inativo não pode abrir solicitacão!");
        }

        Solicitacao solicitacao = new Solicitacao();

        if (!StringUtils.hasLength(request.titulo())) {
            throw new NegocioException("O titulo é obrigatório.");
        }

        if (!StringUtils.hasLength(request.descricao())) {
            throw new NegocioException("A descricao é obrigatória.");
        }

        if (StringUtils.hasLength(request.prioridade())) {
            solicitacao.setPrioridade(PrioridadeSolicitacao.valueOf(request.prioridade().toUpperCase()));
        }

        if (StringUtils.hasLength(request.status())) {
            solicitacao.setStatus(StatusSolicitacao.valueOf(request.status().toUpperCase()));
        }

        Long idTipoSolicitacao = request.tipo().id();

        TipoSolicitacao tipoSolicitacao = tipoSolicitacaoService.buscarPorId(idTipoSolicitacao);

        solicitacao.setTitulo(request.titulo());
        solicitacao.setDescricao(request.descricao());
        solicitacao.setTipo(tipoSolicitacao);
        solicitacao.setUsuario(usuario);

        return solicitacaoRepository.saveAndFlush(solicitacao);
    }

    public Page<Solicitacao> filtrarSolicitacoes(String status, String prioridade, Pageable pageable) {
        try {
            StatusSolicitacao statusEnum = null;
            if (StringUtils.hasLength(status)) {
                statusEnum = StatusSolicitacao.valueOf(status.toUpperCase());
            }

            PrioridadeSolicitacao prioridadeEnum = null;
            if (StringUtils.hasLength(prioridade)) {
                prioridadeEnum = PrioridadeSolicitacao.valueOf(prioridade.toUpperCase());
            }

            return solicitacaoRepository.filtrar(statusEnum, prioridadeEnum, pageable);
        } catch (IllegalArgumentException e) {
            throw new NegocioException("Status ou Prioridade inválidos. Valores válidos: " + "Status: " + Arrays.toString(StatusSolicitacao.values()) + ", " + "Prioridade: " + Arrays.toString(PrioridadeSolicitacao.values()));
        }
    }

    public Page<Solicitacao> listarTodas(Pageable pageable) {
        return solicitacaoRepository.findAll(pageable);
    }

    @Transactional
    public Solicitacao atualizar(Long id, SolicitacaoRequest request) {
        Solicitacao solicitacao = buscarPorId(id);

        if (request.tipo().id() != null && !request.tipo().id().equals(solicitacao.getTipo().getId())) {
            TipoSolicitacao tipoSolicitacao = tipoSolicitacaoService.buscarPorId(request.tipo().id());
            solicitacao.setTipo(tipoSolicitacao);
        }

        if (request.usuario().id() != null && !request.usuario().id().equals(solicitacao.getUsuario().getId())) {
            Usuario usuario = usuarioService.buscarPorId(request.usuario().id());
            solicitacao.setUsuario(usuario);
        }

        PrioridadeSolicitacao prioridadeSolicitacao = PrioridadeSolicitacao.valueOf(request.prioridade());
        StatusSolicitacao statusEnum = StatusSolicitacao.valueOf(request.status().toUpperCase());

        solicitacao.setPrioridade(prioridadeSolicitacao);
        solicitacao.setStatus(statusEnum);
        solicitacao.setTitulo(request.titulo());
        solicitacao.setDescricao(request.descricao());

        return solicitacaoRepository.save(solicitacao);

    }
}
