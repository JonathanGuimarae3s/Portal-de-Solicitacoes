package br.com.jpsl.portalsolicitacaointerna.dominio.service;

import br.com.jpsl.portalsolicitacaointerna.dominio.enums.PrioridadeSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.EntidadeNaoEncontradaException;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Solicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.TipoSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
import br.com.jpsl.portalsolicitacaointerna.dominio.repositorio.SolicitacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolicitacaoService {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private TipoSolicitacaoService tipoSolicitacaoService;

    @Autowired
    private UsuarioService usuarioService;


    public Solicitacao buscarPorId(Long id){
        return  solicitacaoRepository.findById(id).orElseThrow(
                ()-> new EntidadeNaoEncontradaException("Solicitacao não encontrado com id: " + id)
        );
    }

    public Solicitacao salvar(Solicitacao solicitacao) {
        Long idTipoSolicitacao = solicitacao.getTipo().getId();
        Long idUsuario = solicitacao.getUsuario().getId();

        TipoSolicitacao tipoSolicitacao = tipoSolicitacaoService.buscarPorId(idTipoSolicitacao);
        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        solicitacao.setUsuario(usuario);
        solicitacao.setTipo(tipoSolicitacao);

        return solicitacaoRepository.save(solicitacao);
    }
}
