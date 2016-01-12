package com.javaschool.ecare.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;

/**
 * Implements main methods for dao.
 *
 * @param <T> entity class
 */
@Repository
public class BaseDAOImpl<T> implements BaseDAO<T> {

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    public void init() {
        return;
    }

    /**
     * Merge entity in database.
     *
     * @param entity merging entity
     * @return added or updated entity
     */
    public T merge(final T entity) {
        return entityManager.merge(entity);
    }

    /**
     * Get list of all entities with asked type.
     *
     * @param type entity class
     * @return list of objects
     */
    public List<T> getAll(final Class<T> type) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(type);

        Root<T> table = query.from(type);

        query.select(table);

        return entityManager.createQuery(query).getResultList();
    }

    /**
     * Get entity by id from database.
     *
     * @param type entity class
     * @param id   id of object in database
     * @return found object
     */
    public T getById(final Class<T> type, final long id) {
        return entityManager.find(type, id);
    }

    public List<T> getQueryResult(String name, Class<T> type, Map<String, Object> params) {

        TypedQuery<T> namedQuery = entityManager.createNamedQuery(name, type);
        for (String pName : params.keySet()) {
            namedQuery.setParameter(pName, params.get(pName));
        }

        return namedQuery.getResultList();
    }
}
