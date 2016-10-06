package io.carvill.foundation.data.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.carvill.foundation.data.entity.GenericEntity;
import io.carvill.foundation.data.exception.ExpectedResourceException;
import io.carvill.foundation.data.exception.MissingException;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class GenericDAO<T extends GenericEntity<ID>, ID extends Number> {

    @Autowired
    @PersistenceContext
    private EntityManager manager;

    private final Class<T> type;

    public GenericDAO(final Class<T> type) {
        this.type = type;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public T persist(final T entity) {
        this.manager.persist(entity);
        return entity;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public T merge(final T entity) {
        return this.manager.merge(entity);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void delete(final T entity) {
        this.manager.remove(entity);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean deleteById(final ID id) {
        final String ql = String.format("delete from %s E where E.id = ?1", this.type.getSimpleName());
        final Query query = this.manager.createQuery(ql);
        this.setParameters(query, id);
        return query.executeUpdate() == 1;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    protected int update(final String name, final Object... parameters) {
        final Query query = this.manager.createNamedQuery(name);
        this.setParameters(query, parameters);
        return query.executeUpdate();
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public T find(final ID primaryKey, final T defaultValue) {
        try {
            return this.find(primaryKey);
        } catch (final MissingException e) {
            return defaultValue;
        }
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public T find(final ID primaryKey) throws MissingException {
        try {
            final T entity = this.manager.find(this.type, primaryKey);
            if (entity != null) {
                return entity;
            }
        } catch (final RuntimeException e) {
        }
        throw new MissingException("%s with id %d does not exist", this.type.getSimpleName(), primaryKey);
    }

    public T get(final ID primaryKey) {
        try {
            return this.find(primaryKey);
        } catch (final MissingException e) {
            throw new ExpectedResourceException("Unable to locate %s with id %d", e, this.type.getSimpleName(),
                    primaryKey);
        }
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    protected List<T> select(final String name, final Object... parameters) {
        return this.select(name, this.type, parameters);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    protected <S> List<S> select(final String name, final Class<S> type, final Object... parameters) {
        return this.paginate(name, type, 0, -1, parameters);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    protected List<T> paginate(final String name, final int maxResults, final int firstResult,
            final Object... parameters) {
        return this.paginate(name, this.type, maxResults, firstResult, parameters);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    protected <S> List<S> paginate(final String name, final Class<S> type, final int maxResults, final int firstResult,
            final Object... parameters) {
        final TypedQuery<S> query = this.manager.createNamedQuery(name, type);
        this.setParameters(query, parameters);
        if (maxResults > 0) {
            query.setMaxResults(maxResults);
        }
        if (firstResult >= 0) {
            query.setFirstResult(firstResult);
        }
        return query.getResultList();
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    protected T first(final String name, final Object... params) throws MissingException {
        return this.first(name, this.type, params);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    protected <S> S first(final String name, final Class<S> type, final Object... params) throws MissingException {
        final List<S> list = this.paginate(name, type, 1, 0, params);
        if (list.isEmpty()) {
            throw new MissingException("There are not result for query '%s' [type: %s]", name, type.getSimpleName());
        }
        return list.get(0);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    protected T single(final String name, final Object... parameters) throws MissingException {
        return this.single(name, this.type, parameters);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    protected T singleQuiet(final String name, final T defaultValue, final Object... parameters) {
        return this.singleQuiet(name, this.type, defaultValue, parameters);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    protected <S> S singleQuiet(final String name, final Class<S> type, final S defaultValue,
            final Object... parameters) {
        try {
            final S value = this.single(name, type, parameters);
            if (value != null) {
                return value;
            }
        } catch (final MissingException e) {
        }
        return defaultValue;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    protected <S> S single(final String name, final Class<S> type, final Object... parameters) throws MissingException {
        final TypedQuery<S> query = this.manager.createNamedQuery(name, type);
        this.setParameters(query, parameters);
        try {
            return query.getSingleResult();
        } catch (final javax.persistence.PersistenceException e) {
            throw new MissingException("Query '%s' has not produced result [type: %s]", e, name, type.getSimpleName());
        }
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public T detach(final T entity) {
        this.manager.detach(entity);
        return entity;
    }

    private void setParameters(final Query query, final Object... params) {
        if (ArrayUtils.isNotEmpty(params)) {
            int position = 1;
            for (final Object param : params) {
                query.setParameter(position++, param);
            }
        }
    }

    protected Class<T> getType() {
        return this.type;
    }

}
