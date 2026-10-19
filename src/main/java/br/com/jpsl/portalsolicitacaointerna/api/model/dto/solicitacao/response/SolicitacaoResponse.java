package br.com.jpsl.portalsolicitacaointerna.api.model.dto.solicitacao.response;

import br.com.jpsl.portalsolicitacaointerna.api.model.dto.tipoSolicitacao.response.TipoSolicitacaoResponse;
import br.com.jpsl.portalsolicitacaointerna.api.model.dto.usuario.response.UsuarioResumoResponse;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.PrioridadeSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.StatusSolicitacao;

import java.time.LocalDateTime;

public record SolicitacaoResponse(
        Long id,
        String titulo,
        String descricao,
        PrioridadeSolicitacao prioridade,
        StatusSolicitacao status,
        LocalDateTime dataCriacao,
        UsuarioResumoResponse usuario,
        TipoSolicitacaoResponse tipo) {
}

