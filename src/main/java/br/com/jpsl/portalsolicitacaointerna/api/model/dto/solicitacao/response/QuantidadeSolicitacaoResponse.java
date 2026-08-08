package br.com.jpsl.portalsolicitacaointerna.api.model.dto.solicitacao.response;

public record QuantidadeSolicitacaoResponse(
        Long total,
        Long emAprovacao,
        Long aprovadas,
        Long usuariosAtivos) {
}