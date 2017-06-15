package io.carvill.foundation.data.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.SQLQuery;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.transform.Transformers;
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

    @Transactional(readOnly = false, propagation = Propagation.MANDATORY)
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

    public T find(final ID primaryKey, final T defaultValue) {
        try {
            return this.find(primaryKey);
        } catch (final MissingException e) {
            return defaultValue;
        }
    }

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

    protected List<T> select(final String name, final Object... parameters) {
        return GenericDAO.this.select(name, this.type, parameters);
    }

    protected <S> List<S> select(final String name, final Class<S> type, final Object... parameters) {
        return GenericDAO.this.paginate(name, type, 0, -1, parameters);
    }

    protected List<T> paginate(final String name, final int maxResults, final int firstResult,
            final Object... parameters) {
        return GenericDAO.this.paginate(name, this.type, maxResults, firstResult, parameters);
    }

    protected <S> List<S> paginate(final String name, final Class<S> type, final int maxResults, final int firstResult,
            final Object... parameters) {
        return GenericDAO.this.paginate(name, type, maxResults, firstResult, null, parameters);
    }

    private <S> List<S> paginate(final String name, final Class<S> type, final int maxResults, final int firstResult,
            final LockModeType lockMode, final Object... parameters) {
        final TypedQuery<S> query = this.manager.createNamedQuery(name, type);
        this.setParameters(query, parameters);
        if (maxResults > 0) {
            query.setMaxResults(maxResults);
        }
        if (firstResult >= 0) {
            query.setFirstResult(firstResult);
        }

        if (lockMode != null) {
            query.setLockMode(lockMode);
        }
        return query.getResultList();
    }

    protected List<T> selectSQL(final String sql, final Object... parameters) {
        return GenericDAO.this.selectSQL(sql, this.type, parameters);
    }

    protected <S> List<S> selectSQL(final String sql, final Class<S> type, final Object... parameters) {
        return GenericDAO.this.paginateSQL(sql, type, 0, -1, parameters);
    }

    protected List<T> paginateSQL(final String sql, final int maxResults, final int firstResult,
            final Object... parameters) {
        return GenericDAO.this.paginateSQL(sql, this.type, maxResults, firstResult, parameters);
    }

    @SuppressWarnings("unchecked")
    protected <S> List<S> paginateSQL(final String sql, final Class<S> type, final int maxResults,
            final int firstResult, final Object... parameters) {
        final HibernateEntityManager hem = this.manager.unwrap(HibernateEntityManager.class);
        final SQLQuery query = hem.getSession().createSQLQuery(sql);
        query.setResultTransformer(Transformers.aliasToBean(type));
        if (maxResults > 0) {
            query.setMaxResults(maxResults);
        }
        if (firstResult >= 0) {
            query.setFirstResult(firstResult);
        }
        for (int index = 0; index < parameters.length; index++) {
            query.setParameter(index, parameters[index]);
        }
        return query.list();
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    protected int updateSQL(final String sql, final Object... parameters) {
        final Query query = this.manager.createNativeQuery(sql);
        this.setParameters(query, parameters);
        return query.executeUpdate();
    }

    protected T first(final String name, final Object... params) throws MissingException {
        return GenericDAO.this.first(name, this.type, params);
    }

    protected <S> S first(final String name, final Class<S> type, final Object... params) throws MissingException {
        return GenericDAO.this.first(name, type, null, params);
    }

    private <S> S first(final String name, final Class<S> type, final LockModeType lockMode, final Object... params)
            throws MissingException {
        final List<S> list = GenericDAO.this.paginate(name, type, 1, 0, lockMode, params);
        if (list.isEmpty()) {
            throw new MissingException("There are not result for query '%s' [type: %s]", name, type.getSimpleName());
        }
        return list.get(0);
    }

    protected T single(final String name, final Object... parameters) throws MissingException {
        return this.single(name, this.type, parameters);
    }

    protected T singleQuiet(final String name, final T defaultValue, final Object... parameters) {
        return this.singleQuiet(name, this.type, defaultValue, parameters);
    }

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

    protected <S> S single(final String name, final Class<S> type, final Object... parameters) throws MissingException {
        final TypedQuery<S> query = this.manager.createNamedQuery(name, type);
        this.setParameters(query, parameters);
        try {
            return query.getSingleResult();
        } catch (final javax.persistence.PersistenceException e) {
            throw new MissingException("Query '%s' has not produced result [type: %s]", e, name, type.getSimpleName());
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    protected T blockSingle(final String name, final Object... parameters) throws MissingException {
        return GenericDAO.this.blockSingle(name, this.type, parameters);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    protected <S> S blockSingle(final String name, final Class<S> type, final Object... parameters)
            throws MissingException {
        return GenericDAO.this.first(name, type, LockModeType.PESSIMISTIC_WRITE, parameters);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    protected List<T> blockingPaginate(final String name, final int maxResults, final int firstResult,
            final Object... parameters) {
        return GenericDAO.this.blockingPaginate(name, this.type, maxResults, firstResult, parameters);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    protected <S> List<S> blockingPaginate(final String name, final Class<S> type, final int maxResults,
            final int firstResult, final Object... parameters) {
        return GenericDAO.this.paginate(name, type, maxResults, firstResult, LockModeType.PESSIMISTIC_WRITE,
                parameters);
    }

    public T detach(final T entity) {
        this.manager.detach(entity);
        return entity;
    }

    protected void setParameters(final Query query, final Object... params) {
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

    protected EntityManager getManager() {
        return this.manager;
    }

    protected CriteriaQuery<T> createCriteria() {
        return this.createCriteria(this.type);
    }

    protected <S> CriteriaQuery<S> createCriteria(final Class<S> type) {
        return this.getCriteriaBuilder().createQuery(type);
    }

    protected CriteriaBuilder getCriteriaBuilder() {
        return this.manager.getCriteriaBuilder();
    }

}
