package br.com.jpsl.portalsolicitacaointerna.api.controller;

import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.EntidadeNaoEncontradaException;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.NegocioException;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Solicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.repositorio.SolicitacaoRepository;
import br.com.jpsl.portalsolicitacaointerna.dominio.service.SolicitacaoService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
    public List<Solicitacao> listar(@RequestParam(value = "status", required = false) String status,
                                    @RequestParam(value = "prioridade", required = false) String prioridade) {

        if (StringUtils.hasLength(status) || StringUtils.hasLength(prioridade)) {
            return solicitacaoService.filtrarSolicitacoes(status, prioridade);
        }

        return solicitacaoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Solicitacao buscarPorId(@PathVariable Long id) {
        return solicitacaoService.buscarPorId(id);
    }

    @PostMapping
    public Solicitacao salvarSolicitacao(@RequestBody Solicitacao solicitacao) {
        try {
            return solicitacaoService.salvar(solicitacao);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Solicitacao atualizarSolicitacao(@PathVariable Long id, @RequestBody Solicitacao solicitacao) {
        Solicitacao solicitacaoSalva = solicitacaoService.buscarPorId(id);

        try {
            BeanUtils.copyProperties(solicitacao, solicitacaoSalva, "id");

            return solicitacaoService.salvar(solicitacaoSalva);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }

    }

}
