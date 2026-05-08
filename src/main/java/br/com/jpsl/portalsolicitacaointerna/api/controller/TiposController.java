package br.com.jpsl.portalsolicitacaointerna.api.controller;

import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.TipoSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.repositorio.TipoSolicitacaoRepository;
import br.com.jpsl.portalsolicitacaointerna.dominio.service.TipoSolicitacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tipos")
public class TiposController {

    @Autowired
    private TipoSolicitacaoRepository tipoSolicitacaoRepositorio;

    @Autowired
    private TipoSolicitacaoService tipoSolicitacaoService;

    @GetMapping
    public List<TipoSolicitacao> listarUsuarios() {
        return tipoSolicitacaoRepositorio.findAll();
    }

    @PostMapping
    public TipoSolicitacao adicionarUsuario(@RequestBody TipoSolicitacao tipoSolicitacao) {
        return tipoSolicitacaoService.salvarSolicitacao(tipoSolicitacao);
    }


}
