
package br.com.jpsl.portalsolicitacaointerna.dominio.repositorio;

import br.com.jpsl.portalsolicitacaointerna.dominio.enums.PrioridadeSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.Setor;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.StatusSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Solicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long>, SolicitacaoRepositoryQueries {

    @Override
    Page<Solicitacao> findAll(Pageable pageable);

    Page<Solicitacao> findAllByUsuario(Usuario usuario, Pageable pageable);

    Page<Solicitacao> findAllByUsuarioSetor(Setor setor, Pageable pageable);

    Solicitacao findByIdAndUsuario(Long id, Usuario usuario);

    Solicitacao findByIdAndUsuarioSetor(Long id, Setor setor);


    @Override
    Page<Solicitacao> filtrar(StatusSolicitacao statusEnum, PrioridadeSolicitacao prioridadeEnum, Usuario usuario, Setor setor, Pageable pageable);

    Long countSolicitacaoByStatus(StatusSolicitacao status);

    Long countByUsuario(Usuario usuario);

    Long countByUsuarioSetor(Setor setor);

    Long countByStatusAndUsuario(StatusSolicitacao status, Usuario usuario);

    Long countByStatusAndUsuarioSetor(StatusSolicitacao status, Setor setor);


}

