package br.com.jpsl.portalsolicitacaointerna.dominio.enums;

import java.util.Optional;
import java.util.stream.Stream;

public enum PerfilUsuario {
    ADMIN("Administrador"),
    GESTOR("Gestor"),
    USUARIO("Usuario.js");

    String descricao;

    PerfilUsuario(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
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