package com.vincent.springrest.service;

import java.util.List;

import com.vincent.springrest.model.User;

public interface UserService {
	User findById(Integer id);
	User findByUsername(String username);
	List<User> findAllUsers();
	boolean exists(User user);
	void create(User user);
}
