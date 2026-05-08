package br.com.jpsl.portalsolicitacaointerna.api.controller;

import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Solicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.repositorio.SolicitacaoRepository;
import br.com.jpsl.portalsolicitacaointerna.dominio.service.SolicitacaoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitacoes")
public class SolicitacaoController {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private SolicitacaoService solicitacaoService;

    @GetMapping
    public List<Solicitacao> listarTipoSolicitacao() {
        return solicitacaoRepository.findAll();
    }


    @GetMapping("/{id}")
    public Solicitacao buscarPorId(@PathVariable Long id) {
        return solicitacaoService.buscarPorId(id);
    }

    @PostMapping
    public Solicitacao salvarSolicitacao(@RequestBody Solicitacao solicitacao) {
        return solicitacaoService.salvar(solicitacao);
    }

    @PutMapping("/{id}")
    public Solicitacao atualizarSolicitacao(@PathVariable Long id, @RequestBody Solicitacao solicitacao) {
        Solicitacao solicitacaoSalva = solicitacaoService.buscarPorId(id);

        BeanUtils.copyProperties(solicitacao, solicitacaoSalva, "id");

        return solicitacaoService.salvar(solicitacaoSalva);
    }

}
