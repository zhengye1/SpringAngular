package com.vincent.springrest.service;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vincent.springrest.dao.UserDAO;
import com.vincent.springrest.model.User;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{

	static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	UserDAO userDAO;

	@Autowired
	private PasswordEncoder encoder;
	
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
	public boolean existsUsername(String username) {
		User user = findByUsername(username);
		return (user != null) && (user.getUsername().equals(username));
	}

	@Override
	public void create(User user) {
		// TODO Auto-generated method stub
		user.setPassword(encoder.encode(user.getPassword()));
		userDAO.create(user);
	}

	//Since method is running with transactional, no need to use DAO
	@Override
	public void update(User user) {
		// TODO Auto-generated method stub
		logger.info("Try to update User: {}", user);
		User entity = userDAO.findById(user.getId());
		if (entity != null){
			entity.setId(user.getId());
			entity.setUsername(user.getUsername());
			entity.setFirstName(user.getFirstName());
			entity.setLastName(user.getLastName());
			entity.setPassword(encoder.encode(user.getPassword()));
			entity.setDateOfBirth(user.getDateOfBirth());
			entity.setEmail(user.getEmail());
		}
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		userDAO.delete(id);
	}

}
