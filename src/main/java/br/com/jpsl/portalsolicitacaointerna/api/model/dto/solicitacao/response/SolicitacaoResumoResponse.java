package br.com.jpsl.portalsolicitacaointerna.api.model.dto.solicitacao.response;

import br.com.jpsl.portalsolicitacaointerna.dominio.enums.PrioridadeSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.StatusSolicitacao;

public record SolicitacaoResumoResponse(
        Long id,
        String titulo,
        StatusSolicitacao status,
        PrioridadeSolicitacao prioridade) {
}
