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


        List<Predicate> predicates = criarPredicates(statusEnum, prioridadeEnum, criteriaBuilder, root);
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
criteriaQuery.orderBy(criteriaBuilder.asc(root.get("")));

        TypedQuery<Solicitacao> query = entityManager.createQuery(criteriaQuery);

        List<Solicitacao> resultado = query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();


        Long total = contarTotal(statusEnum, prioridadeEnum, criteriaBuilder);

        return new PageImpl<>(resultado, pageable, total);
    }

    private Long contarTotal(StatusSolicitacao statusEnum, PrioridadeSolicitacao prioridadeEnum,
                             CriteriaBuilder criteriaBuilder) {

        CriteriaQuery<Long> countCriteria = criteriaBuilder.createQuery(Long.class);
        Root<Solicitacao> countRoot = countCriteria.from(Solicitacao.class);
        List<Predicate> countPredicates = criarPredicates(statusEnum, prioridadeEnum, criteriaBuilder, countRoot);

        countCriteria.select(criteriaBuilder.count(countRoot));
        countCriteria.where(countPredicates.toArray(new Predicate[0]));

        return entityManager.createQuery(countCriteria).getSingleResult();
    }

    private List<Predicate> criarPredicates(StatusSolicitacao statusEnum, PrioridadeSolicitacao prioridadeEnum,
                                            CriteriaBuilder criteriaBuilder, Root<Solicitacao> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (statusEnum != null) {
            predicates.add(criteriaBuilder.equal(root.get("status"), statusEnum));
        }

        if (prioridadeEnum != null) {
            predicates.add(criteriaBuilder.equal(root.get("prioridade"), prioridadeEnum));
        }

        return predicates;
    }
}
