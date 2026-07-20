package br.com.jpsl.portalsolicitacaointerna.api.model.dto.solicitacao.request;

import br.com.jpsl.portalsolicitacaointerna.api.model.dto.tipoSolicitacao.request.TipoSolicitacaoIdRequest;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.usuario.request.UsuarioIdRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SolicitacaoRequest(
        @NotBlank String titulo,
        @NotBlank String descricao,
        String prioridade,
        String status,
        @Valid @NotNull UsuarioIdRequest usuario,
        @Valid @NotNull TipoSolicitacaoIdRequest tipo) {
}


