
package br.com.jpsl.portalsolicitacaointerna.dominio.repositorio;

import br.com.jpsl.portalsolicitacaointerna.dominio.enums.PrioridadeSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.StatusSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Solicitacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long>, SolicitacaoRepositoryQueries {

    @Override
    Page<Solicitacao> findAll(Pageable pageable);


    @Override
    Page<Solicitacao> filtrar(StatusSolicitacao statusEnum, PrioridadeSolicitacao prioridadeEnum, Pageable pageable);

    Long countSolicitacaoByStatus(StatusSolicitacao status);


}

