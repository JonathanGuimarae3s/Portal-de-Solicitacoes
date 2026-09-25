package br.com.jpsl.portalsolicitacaointerna.api.controller;

import br.com.jpsl.portalsolicitacaointerna.api.assembler.ApiMapper;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.PageResponse;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.solicitacao.request.SolicitacaoRequest;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.solicitacao.response.SolicitacaoResponse;
import br.com.jpsl.portalsolicitacaointerna.dominio.component.UsuarioBean;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.PerfilUsuario;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.Setor;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.EntidadeNaoEncontradaException;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.NegocioException;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Solicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
import br.com.jpsl.portalsolicitacaointerna.dominio.service.SolicitacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/solicitacoes")
@Tag(name = "Solicitações", description = "Cadastro e consulta de solicitações internas")
public class SolicitacaoController {


    private final UsuarioBean usuarioBean;
    private final SolicitacaoService solicitacaoService;

    public SolicitacaoController(SolicitacaoService solicitacaoService, UsuarioBean usuarioBean) {
        this.solicitacaoService = solicitacaoService;
        this.usuarioBean = usuarioBean;
    }

    @GetMapping
    public PageResponse<SolicitacaoResponse> listar(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "prioridade", required = false) String prioridade,
            Pageable pageable) {

        Usuario usuarioLogado = usuarioBean.getUsuarioLogado();
        PerfilUsuario perfilUsuario = usuarioLogado.getPerfil();

        Setor setor = usuarioLogado.getSetor();

        return PageResponse.from(solicitacaoService.retornaSolicitacoes(status, prioridade, pageable, perfilUsuario, setor, usuarioLogado)
                .map(ApiMapper::toResponse));

    }


    @GetMapping("/{id}")
    public SolicitacaoResponse buscarPorId(@PathVariable Long id) {
        Usuario usuarioLogado = usuarioBean.getUsuarioLogado();
        PerfilUsuario perfilUsuario = usuarioLogado.getPerfil();

        Setor setor = usuarioLogado.getSetor();

        return ApiMapper.toResponse(solicitacaoService.retornaSolicitacaoPorId(id, setor, usuarioLogado, perfilUsuario));
    }

    @PostMapping
    public ResponseEntity<SolicitacaoResponse> salvarSolicitacao(@RequestBody @Valid SolicitacaoRequest request) {
        try {

            Usuario usuarioLogado = usuarioBean.getUsuarioLogado();
            Solicitacao salva = solicitacaoService.salvar(request, usuarioLogado);

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

            Usuario usuarioLogado = usuarioBean.getUsuarioLogado();

            return ApiMapper.toResponse(solicitacaoService.atualizar(id, request, usuarioLogado));
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage());
        }

    }

}
