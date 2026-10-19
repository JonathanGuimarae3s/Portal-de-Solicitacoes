package br.com.jpsl.portalsolicitacaointerna.api.controller;

import br.com.jpsl.portalsolicitacaointerna.api.assembler.ApiMapper;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.PageResponse;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.dashboard.response.DashboardIndicadoresResponse;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.solicitacao.response.SolicitacaoResumoResponse;
import br.com.jpsl.portalsolicitacaointerna.dominio.component.UsuarioBean;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.PerfilUsuario;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.Setor;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.StatusSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
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
    private final UsuarioBean usuarioBean;

    public DashboardController(SolicitacaoService solicitacaoService, UsuarioService usuarioService, UsuarioBean usuarioBean) {
        this.solicitacaoService = solicitacaoService;
        this.usuarioService = usuarioService;
        this.usuarioBean = usuarioBean;
    }

    @GetMapping("/indicadores")
    @Operation(summary = "Consultar indicadores do dashboard",
            description = "Retorna as quantidades de solicitações, solicitações em aprovação, aprovadas e usuários ativos.")
    public DashboardIndicadoresResponse consultarIndicadores() {
        Usuario usuarioLogado = usuarioBean.getUsuarioLogado();
        PerfilUsuario perfilUsuario = usuarioLogado.getPerfil();
        Setor setor = usuarioLogado.getSetor();

        Long total = solicitacaoService.contabilizaSolicitacoes(perfilUsuario, setor, usuarioLogado);

        Long emAprovacao = solicitacaoService.contabilizaSolicitacoesPorStatus(
                StatusSolicitacao.EM_APROVACAO, perfilUsuario, setor, usuarioLogado);

        Long aprovadas = solicitacaoService.contabilizaSolicitacoesPorStatus(
                StatusSolicitacao.APROVADA, perfilUsuario, setor, usuarioLogado);

        Long usuariosAtivos = usuarioService.contabilizarUsuariosAtivos(perfilUsuario, setor, usuarioLogado);

        return ApiMapper.toDashboardIndicadoresResponse(total, emAprovacao, aprovadas, usuariosAtivos);
    }

    @GetMapping("/solicitacoes")
    @Operation(summary = "Listar solicitações do dashboard",
            description = "Retorna uma página contendo id, título, status e prioridade. Aceita filtros opcionais por status e prioridade.")
    public PageResponse<SolicitacaoResumoResponse> listarSolicitacoes(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "prioridade", required = false) String prioridade,
            Pageable pageable) {

        Usuario usuarioLogado = usuarioBean.getUsuarioLogado();
        PerfilUsuario perfilUsuario = usuarioLogado.getPerfil();

        Setor setor = usuarioLogado.getSetor();

        return PageResponse.from(solicitacaoService.retornaSolicitacoes(status, prioridade, pageable, perfilUsuario, setor, usuarioLogado)
                .map(ApiMapper::toResumoResponse));
    }
}
