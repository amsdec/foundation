package io.carvill.foundation.data.dao;

import org.springframework.stereotype.Repository;

import io.carvill.foundation.data.entity.User;

@Repository
public class UserDAO extends GenericDAO<User, Integer> {

	public UserDAO() {
		super(User.class);
	}

}
