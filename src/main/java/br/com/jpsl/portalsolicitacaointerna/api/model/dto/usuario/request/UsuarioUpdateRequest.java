package br.com.jpsl.portalsolicitacaointerna.api.model.dto.usuario.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record UsuarioUpdateRequest(@NotBlank String nome,
                                   @NotBlank @Email String email,
                                   String senha,
                                   String perfil,
                                   @NotBlank String setor) {
}
