package br.com.jpsl.portalsolicitacaointerna.dominio.repositorio;

import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.TipoSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoSolicitacaoRepository extends JpaRepository<TipoSolicitacao, Long> {

}
