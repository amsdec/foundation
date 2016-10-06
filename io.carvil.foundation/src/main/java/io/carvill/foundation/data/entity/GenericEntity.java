package io.carvill.foundation.data.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author Carlos Carpio, carlos.carpio07@gmail.com
 */
@MappedSuperclass
public abstract class GenericEntity<ID extends Number> implements Serializable {

	private static final long serialVersionUID = -1751245041654970590L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private ID id;

	public ID getId() {
		return this.id;
	}

	public void setId(final ID id) {
		this.id = id;
	}

}
