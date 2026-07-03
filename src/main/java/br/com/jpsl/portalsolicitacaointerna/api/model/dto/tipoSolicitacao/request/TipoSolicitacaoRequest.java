package br.com.jpsl.portalsolicitacaointerna.api.model.dto.tipoSolicitacao.request;

import jakarta.validation.constraints.NotBlank;

public record TipoSolicitacaoRequest(@NotBlank String nome) {
}
