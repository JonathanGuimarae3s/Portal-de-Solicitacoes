package br.com.jpsl.portalsolicitacaointerna.dominio.service;

import br.com.jpsl.portalsolicitacaointerna.dominio.enums.PrioridadeSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.StatusSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.EntidadeNaoEncontradaException;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.NegocioException;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Solicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.TipoSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
import br.com.jpsl.portalsolicitacaointerna.dominio.repositorio.SolicitacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@Service
public class SolicitacaoService {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private TipoSolicitacaoService tipoSolicitacaoService;

    @Autowired
    private UsuarioService usuarioService;

    public Solicitacao buscarPorId(Long id) {
        return solicitacaoRepository.findById(id).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Solicitacao não encontrado com id: " + id)
        );
    }

    public Solicitacao salvar(Solicitacao solicitacao) {

        if (!StringUtils.hasLength(solicitacao.getTitulo())) {
            throw new NegocioException("O titulo é obrigatório.");
        }

        if (!StringUtils.hasLength(solicitacao.getDescricao())) {
            throw new NegocioException("A descricao é obrigatório.");
        }

        Long idTipoSolicitacao = solicitacao.getTipo().getId();
        Long idUsuario = solicitacao.getUsuario().getId();

        TipoSolicitacao tipoSolicitacao = tipoSolicitacaoService.buscarPorId(idTipoSolicitacao);
        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        solicitacao.setUsuario(usuario);
        solicitacao.setTipo(tipoSolicitacao);

        return solicitacaoRepository.save(solicitacao);
    }

    public List<Solicitacao> filtrarSolicitacoes(String status, String prioridade) {
        try {
            StatusSolicitacao statusEnum = StatusSolicitacao.valueOf(status.toUpperCase());
            if (StringUtils.hasLength(status)) {
                statusEnum = StatusSolicitacao.valueOf(status.toUpperCase());
            }

            PrioridadeSolicitacao prioridadeEnum = null;
            if (StringUtils.hasLength(prioridade)) {
                prioridadeEnum = PrioridadeSolicitacao.valueOf(prioridade.toUpperCase());
            }

            List<Solicitacao> solicitacaoList = solicitacaoRepository.filtrar(statusEnum, prioridadeEnum);

            return solicitacaoList;
        } catch (IllegalArgumentException e) {
            throw new NegocioException("Status ou Prioridade inválidos. Valores válidos: " +
                    "Status: " + Arrays.toString(StatusSolicitacao.values()) + ", " +
                    "Prioridade: " + Arrays.toString(PrioridadeSolicitacao.values()));
        }
    }
}
