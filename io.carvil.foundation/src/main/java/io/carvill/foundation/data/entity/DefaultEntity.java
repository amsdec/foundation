package io.carvill.foundation.data.entity;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
@MappedSuperclass
public abstract class DefaultEntity implements Serializable {

    private static final long serialVersionUID = -2721818632466826007L;

}
