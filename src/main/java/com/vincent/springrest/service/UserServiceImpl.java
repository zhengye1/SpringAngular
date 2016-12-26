package com.vincent.springrest.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vincent.springrest.dao.UserDAO;
import com.vincent.springrest.model.User;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	UserDAO userDAO;
	
	@Override
	public User findById(Integer id) {
		// TODO Auto-generated method stub
		return userDAO.findById(id);
	}

	@Override
	public User findByUsername(String username) {
		// TODO Auto-generated method stub
		return userDAO.findByUsername(username);
	}

	@Override
	public List<User> findAllUsers() {
		// TODO Auto-generated method stub
		return userDAO.findAllUsers();
	}

	@Override
	public boolean exists(User user) {
		return userDAO.exists(user);
		
	}

	@Override
	public void create(User user) {
		// TODO Auto-generated method stub
		userDAO.create(user);
	}

}
