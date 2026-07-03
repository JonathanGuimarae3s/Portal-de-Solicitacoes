package br.com.jpsl.portalsolicitacaointerna.dominio.repositorio;

import br.com.jpsl.portalsolicitacaointerna.dominio.enums.PrioridadeSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.StatusSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Solicitacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SolicitacaoRepositoryQueries {
    Page<Solicitacao> filtrar(StatusSolicitacao statusEnum, PrioridadeSolicitacao prioridadeEnum, Pageable pageable);
}
