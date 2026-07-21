package br.com.jpsl.portalsolicitacaointerna.api.controller;

import br.com.jpsl.portalsolicitacaointerna.api.assembler.ApiMapper;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.PageResponse;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.tipoSolicitacao.request.TipoSolicitacaoRequest;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.tipoSolicitacao.response.TipoSolicitacaoResponse;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.TipoSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.repositorio.TipoSolicitacaoRepository;
import br.com.jpsl.portalsolicitacaointerna.dominio.service.TipoSolicitacaoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/tipos")
public class TiposController {

    private TipoSolicitacaoRepository tipoSolicitacaoRepositorio;

    private TipoSolicitacaoService tipoSolicitacaoService;

    public TiposController(TipoSolicitacaoRepository tipoSolicitacaoRepositorio, TipoSolicitacaoService tipoSolicitacaoService) {
        this.tipoSolicitacaoRepositorio = tipoSolicitacaoRepositorio;
        this.tipoSolicitacaoService = tipoSolicitacaoService;
    }

    @GetMapping
    public PageResponse<TipoSolicitacaoResponse> listarTipos(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return PageResponse.from(tipoSolicitacaoService.listarTipos(pageable).map(ApiMapper::toResponse));
    }

    @GetMapping("/{id}")
    public TipoSolicitacaoResponse buscarPorId(@PathVariable Long id) {
        return ApiMapper.toResponse(tipoSolicitacaoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<TipoSolicitacaoResponse> adicionarUsuario(@RequestBody @Valid TipoSolicitacaoRequest request) {
        TipoSolicitacao salva = tipoSolicitacaoService.salvarSolicitacao(novaTipoSolicitacao(request));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(salva.getId()).toUri();
        return ResponseEntity.created(location)
                .body(ApiMapper.toResponse(salva));
    }

    @PutMapping("/{id}")
    public TipoSolicitacaoResponse atualizar(@PathVariable Long id, @RequestBody @Valid TipoSolicitacaoRequest request) {
        TipoSolicitacao tipoSolicitacao = tipoSolicitacaoService.buscarPorId(id);
        aplicar(request, tipoSolicitacao);
        return ApiMapper.toResponse(tipoSolicitacaoService.salvarSolicitacao(tipoSolicitacao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        tipoSolicitacaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    private TipoSolicitacao novaTipoSolicitacao(TipoSolicitacaoRequest request) {
        TipoSolicitacao tipoSolicitacao = new TipoSolicitacao();
        String nome = StringUtils.trimAllWhitespace(request.nome());
        tipoSolicitacao.setNome(nome);
        return tipoSolicitacao;
    }

    private void aplicar(TipoSolicitacaoRequest request, TipoSolicitacao tipoSolicitacao) {
        String nome = StringUtils.trimAllWhitespace(request.nome());
        tipoSolicitacao.setNome(nome);
    }
}
