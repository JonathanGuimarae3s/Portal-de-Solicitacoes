package br.com.jpsl.portalsolicitacaointerna.dominio.enums;

import lombok.Getter;

@Getter
public enum PrioridadeSolicitacao {

    BAIXA("Baixa"),
    MEDIA("Média"),
    ALTA("Alta");

    private String descricao;

    PrioridadeSolicitacao(String descricao) {
        this.descricao = descricao;
    }
}
