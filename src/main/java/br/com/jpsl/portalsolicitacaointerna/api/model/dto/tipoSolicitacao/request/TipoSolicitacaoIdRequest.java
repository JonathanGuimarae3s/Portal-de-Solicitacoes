package br.com.jpsl.portalsolicitacaointerna.api.model.dto.tipoSolicitacao.request;

import jakarta.validation.constraints.NotNull;

public record TipoSolicitacaoIdRequest(@NotNull Long id) {
}
