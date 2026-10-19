package br.com.jpsl.portalsolicitacaointerna.api.model.dto.dashboard.response;

public record DashboardIndicadoresResponse(
        Long total,
        Long emAprovacao,
        Long aprovadas,
        Long usuariosAtivos) {
}
