package com.vincent.springrest.dao;

import java.util.List;

import com.vincent.springrest.model.User;

public interface UserDAO {
	User findById(Integer id);
	User findByUsername(String username);
	List<User> findAllUsers();
	void create(User user);
}
