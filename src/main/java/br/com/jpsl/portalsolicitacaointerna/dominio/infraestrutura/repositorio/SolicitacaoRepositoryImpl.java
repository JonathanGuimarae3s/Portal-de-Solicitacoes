package br.com.jpsl.portalsolicitacaointerna.dominio.infraestrutura.repositorio;

import br.com.jpsl.portalsolicitacaointerna.dominio.enums.PrioridadeSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.StatusSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Solicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.repositorio.SolicitacaoRepositoryQueries;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SolicitacaoRepositoryImpl implements SolicitacaoRepositoryQueries {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Solicitacao> filtrar(StatusSolicitacao statusEnum, PrioridadeSolicitacao prioridadeEnum,
                                     Pageable pageable) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Solicitacao> criteriaQuery = criteriaBuilder.createQuery(Solicitacao.class);

        Root<Solicitacao> root = criteriaQuery.from(Solicitacao.class);

        var predicates = new ArrayList<>();

        if (statusEnum != null) {
            predicates.add(criteriaBuilder.equal(root.get("status"), statusEnum));
        }

        if (prioridadeEnum != null) {
            predicates.add(criteriaBuilder.equal(root.get("prioridade"), prioridadeEnum));
        }


        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        TypedQuery<Solicitacao> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults((int) pageable.getPageSize());

        List<Solicitacao> solicitacaoList = query.getResultList();

        return new PageImpl(solicitacaoList, pageable, solicitacaoList.size());
    }
}
