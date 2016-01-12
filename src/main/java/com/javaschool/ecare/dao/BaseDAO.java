package com.javaschool.ecare.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

/**
 * Describes main dao methods.
 *
 * @param <T> class of entity
 */
@Repository
public interface BaseDAO<T> {
    /**
     * Add or update entity in database.
     *
     * @param entity merging entity
     * @return added or updated entity
     */
    T merge(T entity);

    /**
     * Get all entities with given type from database.
     *
     * @param type entity class
     * @return list of entities
     */
    List<T> getAll(Class<T> type);

    /**
     * Get entity with given type and id from database.
     *
     * @param type entity class
     * @param id   id of entity in database
     * @return found entity
     */
    T getById(Class<T> type, long id);

    List<T> getQueryResult (String name, Class<T> type, Map<String, Object> params);
}
