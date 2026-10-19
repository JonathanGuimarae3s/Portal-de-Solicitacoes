package br.com.jpsl.portalsolicitacaointerna.auth.DTO.response;

public record UsuarioAutenticadoResponse(
        Long id,
        String nome,
        String email,
        String perfil,
        String setor
) {
}