package br.com.jpsl.portalsolicitacaointerna.dominio.service;

import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.EntidadeNaoEncontradaException;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.NegocioException;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.TipoSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.repositorio.TipoSolicitacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TipoSolicitacaoService {

    @Autowired
    private TipoSolicitacaoRepository tipoSolicitacaoRepositorio;


    public TipoSolicitacao buscarPorId(Long id){
        return  tipoSolicitacaoRepositorio.findById(id).orElseThrow(
                ()-> new EntidadeNaoEncontradaException("Tipo de Solicitacao não encontrado com id: " + id)
        );
    }

    public TipoSolicitacao salvarSolicitacao(TipoSolicitacao tipoSolicitacao) {

        if (!StringUtils.hasLength(tipoSolicitacao.getNome())) {
            throw new NegocioException("O nome da solicitacao é obrigatório.");
        }

        return tipoSolicitacaoRepositorio.save(tipoSolicitacao);
    }
}
