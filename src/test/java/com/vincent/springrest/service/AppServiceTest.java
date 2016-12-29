package com.vincent.springrest.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
 
import static org.mockito.Mockito.when;
 
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.annotation.Rollback;

import com.vincent.springrest.dao.UserDAO;
import com.vincent.springrest.model.User;

public class AppServiceTest {

	@InjectMocks
	UserServiceImpl userService; 

	@Mock
	UserDAO userDAO;

	@Spy
	List<User> users = new ArrayList<User>();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		users = getUserList();
	}


	@Test
	public void testFindByUsernameSuccess(){
		User user = getUser("admin");
		when(userDAO.findByUsername("admin")).thenReturn(user);
		Assert.assertEquals(user, userService.findByUsername(user.getUsername()));
	}

	@Test
	public void testFindByUsernameFail(){
		User user = getUser("null");
		when(userDAO.findByUsername("null")).thenReturn(user);
		Assert.assertNull(userService.findByUsername("null"));
	}

	@Test
	public void testFindByIdSuccess(){
		User user = users.get(0);
		when(userDAO.findById(1)).thenReturn(user);
		Assert.assertEquals(user, userService.findById(user.getId()));
	}

	@Test
	public void testFindByIdFail(){
		when(userDAO.findById(3)).thenReturn(null);
		Assert.assertNull(userService.findById(anyInt()));
	}

	@Test
	public void testFindAll(){
		when(userDAO.findAllUsers()).thenReturn(users);
		Assert.assertEquals(users, userService.findAllUsers());
	}


	@Rollback(true)
	@Test
	public void testCreateUser(){
		doNothing().when(userDAO).create(any(User.class));
		userService.create(new User());
		verify(userDAO, atLeastOnce()).create(any(User.class));
	}
	
	@Test
	public void testExistsUser(){
		User user = users.get(0);
		when(userDAO.findByUsername(anyString())).thenReturn(user);
		Assert.assertTrue(userService.existsUsername(user.getUsername()));
	}

	@Test
	public void testNonExitUserWithNull(){
		User user = null;
		when(userDAO.findByUsername(anyString())).thenReturn(user);
		Assert.assertFalse(userService.existsUsername("null"));
	}
	
	@Test
	public void testNonExitUserWithUsernameDiff(){
		User user = users.get(0);
		when(userDAO.findByUsername(anyString())).thenReturn(user);
		Assert.assertFalse(userService.existsUsername("null"));
	}
	
	
	@Test
	public void testUpdateUser(){
		User user = users.get(0);
		when(userDAO.findById(anyInt())).thenReturn(user);
		userService.update(user);
		verify(userDAO, atLeastOnce()).findById(anyInt());
	}
	
	@Test
	public void testUpdateNonExistsUser(){
		User user = new User();
		user.setId(5);
		user.setUsername("null");
		when(userDAO.findById(anyInt())).thenReturn(null);
		userService.update(user);
		verify(userDAO, atLeastOnce()).findById(anyInt());
	}
	
	@Rollback(true)
	@Test
	public void testDeleteUser(){
		User user = users.get(0);
		doNothing().when(userDAO).delete(anyInt());
		userService.delete(user.getId());
		verify(userDAO, atLeastOnce()).delete(anyInt());
	}
	

	public List<User> getUserList(){
		User user1 = new User(1, "admin", "admin", "Admin", "Admin", "vincentcheng787@gmail.com", 
				new LocalDate(1990, 12, 1));
		User user2 = new User(2, "yukirin", "Yuki0715", "Yuki", "Kashiwagi", "yuki.kashiwagi@akb.co.jp", 
				new LocalDate(1991, 7, 15));
		List<User> expected = Arrays.asList(user1, user2);
		return expected;
	}

	private User getUser(String username){
		for (User u : users){
			if (u.getUsername().equals(username)){
				return u;
			}
		}
		return null;
	}
}
