package br.com.jpsl.portalsolicitacaointerna.api.model.dto.usuario.request;

import jakarta.validation.constraints.NotNull;

public record UsuarioStatusRequest(@NotNull Boolean ativo) {
}
