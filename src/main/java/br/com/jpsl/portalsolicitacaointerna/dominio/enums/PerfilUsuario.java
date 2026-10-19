package br.com.jpsl.portalsolicitacaointerna.dominio.enums;

import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.NegocioException;

import java.util.Optional;
import java.util.stream.Stream;

public enum PerfilUsuario {
    ADMIN("Administrador"),
    GESTOR("Gestor"),
    USUARIO("Usuario");

    String descricao;

    PerfilUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }


    public static PerfilUsuario converterPerfilUsuarioSTRING(String perfil) {
        return  PerfilUsuario.fromString(perfil)
                .orElseThrow(() -> new NegocioException(
                        "Perfil Usuário inválido. Valores válidos: " + java.util.Arrays.toString(PerfilUsuario.values())));
    }

    public static Optional<PerfilUsuario> fromString(String valor) {
        if (valor == null || valor.isBlank()) {
            return Optional.empty();
        }

        return Stream.of(values())
                .filter(s -> s.name().equalsIgnoreCase(valor.trim()))
                .findFirst();
    }
}