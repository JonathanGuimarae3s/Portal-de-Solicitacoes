package br.com.jpsl.portalsolicitacaointerna.api.assembler;


import br.com.jpsl.portalsolicitacaointerna.api.model.dto.solicitacao.response.QuantidadeSolicitacaoResponse;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.solicitacao.response.SolicitacaoResponse;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.solicitacao.response.SolicitacaoResumoResponse;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.tipoSolicitacao.response.TipoSolicitacaoResponse;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.usuario.response.UsuarioResponse;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.usuario.response.UsuarioResumoResponse;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Solicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.TipoSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;

public final class ApiMapper {

    private ApiMapper() {
    }

    public static TipoSolicitacaoResponse toResponse(TipoSolicitacao tipoSolicitacao) {
        return new TipoSolicitacaoResponse(
                tipoSolicitacao.getId(),
                tipoSolicitacao.getNome()
        );
    }

    public static UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSetor(),
                usuario.isAtivo()
        );
    }

    public static UsuarioResumoResponse toResumeResponse(Usuario usuario) {
        return new UsuarioResumoResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getSetor()
        );
    }

    public static SolicitacaoResponse toResponse(Solicitacao solicitacao) {
        return new SolicitacaoResponse(
                solicitacao.getId(),
                solicitacao.getTitulo(),
                solicitacao.getDescricao(),
                solicitacao.getPrioridade(),
                solicitacao.getStatus(),
                solicitacao.getDataCriacao(),
                toResumeResponse(solicitacao.getUsuario()),
                toResponse(solicitacao.getTipo())
        );
    }

    public static SolicitacaoResumoResponse toResumoResponse(Solicitacao solicitacao) {
        return new SolicitacaoResumoResponse(
                solicitacao.getId(),
                solicitacao.getTitulo(),
                solicitacao.getStatus(),
                solicitacao.getPrioridade()
        );
    }

    public static QuantidadeSolicitacaoResponse toResponse(Long total, Long emAprovacao, Long aprovadas, Long usuariosAtivos) {
        return new QuantidadeSolicitacaoResponse(
                total,
                emAprovacao,
                aprovadas,usuariosAtivos
        );
    }


}



