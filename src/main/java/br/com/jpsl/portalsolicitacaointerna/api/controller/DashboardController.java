package br.com.jpsl.portalsolicitacaointerna.api.controller;

import br.com.jpsl.portalsolicitacaointerna.api.assembler.ApiMapper;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.PageResponse;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.dashboard.response.DashboardIndicadoresResponse;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.solicitacao.response.SolicitacaoResumoResponse;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.StatusSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.service.SolicitacaoService;
import br.com.jpsl.portalsolicitacaointerna.dominio.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboard", description = "Indicadores e dados resumidos do dashboard")
public class DashboardController {

    private final SolicitacaoService solicitacaoService;
    private final UsuarioService usuarioService;

    public DashboardController(SolicitacaoService solicitacaoService, UsuarioService usuarioService) {
        this.solicitacaoService = solicitacaoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/indicadores")
    @Operation(summary = "Consultar indicadores do dashboard",
            description = "Retorna as quantidades de solicitações, solicitações em aprovação, aprovadas e usuários ativos.")
    public DashboardIndicadoresResponse consultarIndicadores() {
        Long total = solicitacaoService.contabilizaSolicitacoes();
        Long emAprovacao = solicitacaoService.contabilizaSolicitacoesPorStatus(StatusSolicitacao.EM_APROVACAO);
        Long aprovadas = solicitacaoService.contabilizaSolicitacoesPorStatus(StatusSolicitacao.APROVADA);
        Long usuariosAtivos = usuarioService.contabilizarUsuariosAtivos();

        return ApiMapper.toDashboardIndicadoresResponse(total, emAprovacao, aprovadas, usuariosAtivos);
    }

    @GetMapping("/solicitacoes")
    @Operation(summary = "Listar solicitações do dashboard",
            description = "Retorna uma página contendo id, título, status e prioridade. Aceita filtros opcionais por status e prioridade.")
    public PageResponse<SolicitacaoResumoResponse> listarSolicitacoes(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "prioridade", required = false) String prioridade,
            Pageable pageable) {

        if (StringUtils.hasLength(status) || StringUtils.hasLength(prioridade)) {
            return PageResponse.from(solicitacaoService.filtrarSolicitacoes(status, prioridade, pageable)
                    .map(ApiMapper::toResumoResponse));
        }

        return PageResponse.from(solicitacaoService.listarTodas(pageable)
                .map(ApiMapper::toResumoResponse));
    }
}
