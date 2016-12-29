package com.vincent.springrest.dao;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Repository;

import com.vincent.springrest.model.User;

@Repository("userDAO")
public class UserDAOImpl implements UserDAO {

	private static List<User> users;

	static{
		users= populateDummyUsers();
	}

	@Override
	public User findById(Integer id) {
		// TODO Auto-generated method stub
		for(User user : users){
			if (user.getId().equals(id)){
				return user;
			}
		}
		return null;
	}

	@Override
	public User findByUsername(String username) {
		for(User user : users){
			if (user.getUsername().equalsIgnoreCase(username)){
				return user;
			}	
		}
		return null;
	}

	@Override
	public List<User> findAllUsers() {
		// TODO Auto-generated method stub
		return users;
	}

	private static List<User> populateDummyUsers(){
		List<User> users = new ArrayList<User>();
		users.add(new User(1, "admin", "admin", "Admin", "Admin", "vincentcheng787@gmail.com", 
				new LocalDate(1990, 12, 1)));
		users.add(new User(2, "yukirin", "Yuki0715", "Yuki", "Kashiwagi", "yuki.kashiwagi@akb.co.jp", 
				new LocalDate(1991, 7, 15)));
		return users;
	}

	@Override
	public void create(User user) {
		// TODO Auto-generated method stub
		users.add(user);
	}

	@Override
	public void delete(int id) {
		// TODO Auto-generated method stub
		User user =findById(id);
		users.remove(user);
	}




}
