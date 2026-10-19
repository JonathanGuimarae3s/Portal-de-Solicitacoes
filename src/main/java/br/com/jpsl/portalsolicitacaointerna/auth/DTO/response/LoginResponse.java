package br.com.jpsl.portalsolicitacaointerna.auth.DTO.response;

public record LoginResponse(String token, Long expiresInMillis, UsuarioAutenticadoResponse usuarioAutenticado) {

}
