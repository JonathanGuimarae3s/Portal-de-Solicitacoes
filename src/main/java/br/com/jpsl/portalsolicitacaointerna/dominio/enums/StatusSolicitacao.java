package br.com.jpsl.portalsolicitacaointerna.dominio.enums;

import lombok.Getter;

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

}