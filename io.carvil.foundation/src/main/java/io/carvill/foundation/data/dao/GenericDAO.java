package io.carvill.foundation.data.dao;

import javax.persistence.Query;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import io.carvill.foundation.data.entity.GenericEntity;
import io.carvill.foundation.data.exception.ExpectedResourceException;
import io.carvill.foundation.data.exception.MissingException;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
public class GenericDAO<T extends GenericEntity<ID>, ID extends Number> extends DefaultDAO<T> {

    public GenericDAO(final Class<T> type) {
        super(type);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean deleteById(final ID id) {
        final String ql = String.format("delete from %s E where E.id = ?1", this.getType().getSimpleName());
        final Query query = this.getManager().createQuery(ql);
        this.setParameters(query, id);
        return query.executeUpdate() == 1;
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
            final T entity = this.getManager().find(this.getType(), primaryKey);
            if (entity != null) {
                return entity;
            }
        } catch (final RuntimeException e) {
        }
        throw new MissingException("%s with id %d does not exist", this.getType().getSimpleName(), primaryKey);
    }

    public T get(final ID primaryKey) {
        try {
            return this.find(primaryKey);
        } catch (final MissingException e) {
            throw new ExpectedResourceException("Unable to locate %s with id %d", e, this.getType().getSimpleName(),
                    primaryKey);
        }
    }

}
