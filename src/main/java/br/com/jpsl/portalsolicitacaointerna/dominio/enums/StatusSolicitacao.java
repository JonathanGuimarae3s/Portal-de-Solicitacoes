package br.com.jpsl.portalsolicitacaointerna.dominio.enums;

import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
public enum StatusSolicitacao {
    ABERTA("Aberta"),
    EM_APROVACAO("Em aprovacao"),
    APROVADA("Aprovada"),
    REJEITADA("Rejeitada"),
    CONCLUIDA("Concluído");

    private String descricao;

    StatusSolicitacao(String descricao) {
        this.descricao = descricao;
    }

    public boolean podeTransicionarPara(StatusSolicitacao statusSolicitacao) {
        return switch (this) {
            case ABERTA -> statusSolicitacao == EM_APROVACAO;
            case EM_APROVACAO -> statusSolicitacao == APROVADA || statusSolicitacao == REJEITADA;
            case APROVADA -> statusSolicitacao == CONCLUIDA;
            case REJEITADA, CONCLUIDA -> false;
        };
    }

    public static Optional<StatusSolicitacao> fromString(String valor) {
        if (valor == null || valor.isBlank()) {
            return Optional.empty();
        }

        return Stream.of(values())
                .filter(s -> s.name().equalsIgnoreCase(valor.trim()))
                .findFirst();
    }
}
