package br.com.jpsl.portalsolicitacaointerna.dominio.service;

import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.EntidadeEmUsoException;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.EntidadeNaoEncontradaException;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.NegocioException;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.TipoSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.repositorio.TipoSolicitacaoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class TipoSolicitacaoService {

    private TipoSolicitacaoRepository tipoSolicitacaoRepositorio;

    public TipoSolicitacaoService(TipoSolicitacaoRepository tipoSolicitacaoRepositorio) {
        this.tipoSolicitacaoRepositorio = tipoSolicitacaoRepositorio;
    }

    @Transactional(readOnly = true)
    public TipoSolicitacao buscarPorId(Long id) {
        return tipoSolicitacaoRepositorio.findById(id).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Tipo de solicitação não encontrado com o ID: " + id)
        );
    }

    @Transactional
    public TipoSolicitacao salvarSolicitacao(TipoSolicitacao tipoSolicitacao) {

        if (!StringUtils.hasLength(tipoSolicitacao.getNome())) {
            throw new NegocioException("O nome do tipo de solicitação é obrigatório.");
        }

        return tipoSolicitacaoRepositorio.save(tipoSolicitacao);
    }

    @Transactional(readOnly = true)
    public Page<TipoSolicitacao> listarTipos(Pageable pageable) {
        return tipoSolicitacaoRepositorio.findAll(pageable);
    }

    @Transactional
    public void excluir(Long id) {
        TipoSolicitacao tipoSolicitacao = buscarPorId(id);
        try {
            tipoSolicitacaoRepositorio.delete(tipoSolicitacao);
            tipoSolicitacaoRepositorio.flush();
        } catch (DataIntegrityViolationException exception) {
            throw new EntidadeEmUsoException("O tipo de solicitação de código %d está em uso.".formatted(id));
        }
    }
}
