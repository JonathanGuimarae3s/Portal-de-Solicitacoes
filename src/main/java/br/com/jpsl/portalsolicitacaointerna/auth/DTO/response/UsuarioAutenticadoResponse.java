package br.com.jpsl.portalsolicitacaointerna.auth.DTO.response;

public record UsuarioAutenticadoResponse(
        String nome,
        String email,
        String perfil,
        String setor
){}