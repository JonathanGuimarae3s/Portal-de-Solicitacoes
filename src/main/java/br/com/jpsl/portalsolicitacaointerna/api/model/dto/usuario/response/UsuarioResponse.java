package br.com.jpsl.portalsolicitacaointerna.api.model.dto.usuario.response;


public record UsuarioResponse(Long id, String nome, String email, String setor, boolean ativo) {
}