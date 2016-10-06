package io.carvill.foundation.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User extends GenericEntity<Integer> {

	private static final long serialVersionUID = 6887244784137909837L;

	@Column(name = "username", nullable = false)
	private String username;

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "User [username=" + username + "]";
	}

}
