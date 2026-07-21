package br.com.jpsl.portalsolicitacaointerna.api.controller;

import br.com.jpsl.portalsolicitacaointerna.api.assembler.ApiMapper;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.PageResponse;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.solicitacao.request.SolicitacaoRequest;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.solicitacao.response.SolicitacaoResponse;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.EntidadeNaoEncontradaException;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.NegocioException;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Solicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.service.SolicitacaoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/solicitacoes")
public class SolicitacaoController {


    private SolicitacaoService solicitacaoService;

    public SolicitacaoController(SolicitacaoService solicitacaoService) {
        this.solicitacaoService = solicitacaoService;
    }

    @GetMapping
    public PageResponse<SolicitacaoResponse> listar(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "prioridade", required = false) String prioridade,
            Pageable pageable) {

        if (StringUtils.hasLength(status) || StringUtils.hasLength(prioridade)) {
            return PageResponse.from(solicitacaoService.filtrarSolicitacoes(status, prioridade, pageable)
                    .map(ApiMapper::toResponse));
        }

        return PageResponse.from(solicitacaoService.listarTodas(pageable)
                .map(ApiMapper::toResponse));
    }

    @GetMapping("/{id}")
    public SolicitacaoResponse buscarPorId(@PathVariable Long id) {
        return ApiMapper.toResponse(solicitacaoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<SolicitacaoResponse> salvarSolicitacao(@RequestBody @Valid SolicitacaoRequest request) {
        try {
            Solicitacao salva = solicitacaoService.salvar(request);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(salva.getId())
                    .toUri();

            return ResponseEntity.created(location)
                    .body(ApiMapper.toResponse(salva));

        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public SolicitacaoResponse atualizarSolicitacao(@PathVariable Long id,
                                                    @RequestBody @Valid SolicitacaoRequest request) {

        try {
            return ApiMapper.toResponse(solicitacaoService.atualizar(id, request));
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }

    }

}
