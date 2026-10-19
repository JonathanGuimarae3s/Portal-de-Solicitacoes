package br.com.jpsl.portalsolicitacaointerna.dominio.infraestrutura.repositorio;

import br.com.jpsl.portalsolicitacaointerna.dominio.enums.PrioridadeSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.Setor;
import br.com.jpsl.portalsolicitacaointerna.dominio.enums.StatusSolicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.excecao.NegocioException;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Solicitacao;
import br.com.jpsl.portalsolicitacaointerna.dominio.modelo.Usuario;
import br.com.jpsl.portalsolicitacaointerna.dominio.repositorio.SolicitacaoRepositoryQueries;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class SolicitacaoRepositoryImpl implements SolicitacaoRepositoryQueries {


    private static final Set<String> camposPermitidos =
            Set.of("id", "titulo", "status", "prioridade", "dataCriacao");


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Solicitacao> filtrar(StatusSolicitacao statusEnum, PrioridadeSolicitacao prioridadeEnum,
                                     Usuario usuario, Setor setor, Pageable pageable) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Solicitacao> criteriaQuery = criteriaBuilder.createQuery(Solicitacao.class);

        Root<Solicitacao> root = criteriaQuery.from(Solicitacao.class);


        List<Predicate> predicates = criarPredicates(statusEnum, prioridadeEnum, setor, usuario, criteriaBuilder, root);
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        verificaOrdenadores(pageable, criteriaBuilder, root, criteriaQuery);

        TypedQuery<Solicitacao> query = entityManager.createQuery(criteriaQuery);

        List<Solicitacao> resultado = query
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();


        Long total = contarTotal(statusEnum, prioridadeEnum, criteriaBuilder, usuario, setor);

        return new PageImpl<>(resultado, pageable, total);
    }

    private static void verificaOrdenadores(Pageable pageable, CriteriaBuilder criteriaBuilder, Root<Solicitacao> root, CriteriaQuery<Solicitacao> criteriaQuery) {
        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            List<Order> orders = sort.stream()
                    .map(order -> {
                        String propriedade = order.getProperty();

                        if (!camposPermitidos.contains(propriedade)) {
                            throw new NegocioException(
                                    "Campo de ordenação inválido: " + propriedade
                            );
                        }

                        return order.isAscending()
                                ? criteriaBuilder.asc(root.get(propriedade))
                                : criteriaBuilder.desc(root.get(propriedade));


                    })
                    .toList();

            criteriaQuery.orderBy(orders);
        }
    }

    private Long contarTotal(StatusSolicitacao statusEnum, PrioridadeSolicitacao prioridadeEnum,
                             CriteriaBuilder criteriaBuilder, Usuario usuario, Setor setor) {

        CriteriaQuery<Long> countCriteria = criteriaBuilder.createQuery(Long.class);
        Root<Solicitacao> countRoot = countCriteria.from(Solicitacao.class);
        List<Predicate> countPredicates = criarPredicates(statusEnum, prioridadeEnum, setor, usuario, criteriaBuilder, countRoot);

        countCriteria.select(criteriaBuilder.count(countRoot));
        countCriteria.where(countPredicates.toArray(new Predicate[0]));

        return entityManager.createQuery(countCriteria).getSingleResult();
    }

    private List<Predicate> criarPredicates(StatusSolicitacao statusEnum, PrioridadeSolicitacao prioridadeEnum,
                                            Setor setor, Usuario usuario, CriteriaBuilder criteriaBuilder, Root<Solicitacao> root) {
        List<Predicate> predicates = new ArrayList<>();

        if (statusEnum != null) {
            predicates.add(criteriaBuilder.equal(root.get("status"), statusEnum));
        }

        if (prioridadeEnum != null) {
            predicates.add(criteriaBuilder.equal(root.get("prioridade"), prioridadeEnum));
        }

        if (usuario != null) {
            predicates.add(criteriaBuilder.equal(root.get("usuario"), usuario));
        }

        if (setor != null) {
            predicates.add(criteriaBuilder.equal(root.get("usuario").get("setor"), setor));
        }

        return predicates;
    }
}
