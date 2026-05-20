package br.com.jpsl.portalsolicitacaointerna.infraestrutura.repository;

import br.com.jpsl.portalsolicitacaointerna.dominio.enums.PrioridadeSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.StatusSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Solicitacao;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SolicitacaoRepositoryImpl {

    @PersistenceContext
    private EntityManager em;

    public List<Solicitacao> filtrar(StatusSolicitacao status, PrioridadeSolicitacao prioridade) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

        CriteriaQuery<Solicitacao> criteriaQuery = criteriaBuilder.createQuery(Solicitacao.class);
        Root<Solicitacao> root = criteriaQuery.from(Solicitacao.class);

        var predicates = new ArrayList<>();

        if (status != null) {
            predicates.add(criteriaBuilder.equal(root.get("status"), status));
        }

        if (prioridade != null) {
            predicates.add(criteriaBuilder.equal(root.get("prioridade"), prioridade));
        }

        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        return em.createQuery(criteriaQuery)
                .getResultList();
    }


}
