package br.com.jpsl.portalsolicitacaointerna.dominio.enums;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum PrioridadeSolicitacao {

    BAIXA("Baixa"),
    MEDIA("Média"),
    ALTA("Alta");

    private String descricao;

    PrioridadeSolicitacao(String descricao) {
        this.descricao = descricao;
    }

    public static Optional<PrioridadeSolicitacao> fromString(String valor) {
        if (valor == null || valor.isBlank()) {
            return Optional.empty();
        }

        return Stream.of(values())
                .filter(s -> s.name().equalsIgnoreCase(valor.trim()))
                .findFirst();
    }
}
