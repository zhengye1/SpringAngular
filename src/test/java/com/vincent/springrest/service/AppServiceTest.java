package com.vincent.springrest.service;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vincent.springrest.dao.UserDAO;
import com.vincent.springrest.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class AppServiceTest {


	@Autowired
	UserService userService; 

	@Autowired
	UserDAO userDAO;

	@Before
	public void setup() {
		User user1 = new User(1, "admin", "admin", "Admin", "Admin", "vincentcheng787@gmail.com", 
				new LocalDate(1990, 12, 1));
		User user2 = new User(2, "yukirin", "Yuki0715", "Yuki", "Kashiwagi", "yuki.kashiwagi@akb.co.jp", 
				new LocalDate(1991, 7, 15));
		List<User> expected = Arrays.asList(user1, user2);
		Mockito.when(userDAO.findByUsername("admin")).thenReturn(user1);
		Mockito.when(userDAO.findById(1)).thenReturn(user1);
		Mockito.when(userDAO.findAllUsers()).thenReturn(expected);
	}

	@After
	public void verify() {
		// This is allowed here: using container injected mocks
		Mockito.reset(userDAO);
	}

	@Test
	public void testFindByUsernameSuccess(){
		User user = userService.findByUsername("admin");
		assertEquals((Integer)1, user.getId());
		assertEquals("admin", user.getUsername());
		assertEquals("Admin", user.getFirstName());
		assertEquals("Admin", user.getLastName());
		assertEquals("vincentcheng787@gmail.com", user.getEmail());
		assertEquals("1990-12-01", user.getDateOfBirth().toString());
		Mockito.verify(userDAO, VerificationModeFactory.times(1)).findByUsername(Mockito.anyString());
	}
	
	@Test
	public void testFindByUsernameFail(){
		User user = userService.findByUsername("yukirin");
		assertNull(user);
		Mockito.verify(userDAO, VerificationModeFactory.times(1)).findByUsername(Mockito.anyString());
	}
	
	@Test
	public void testFindByIdSuccess(){
		User user = userService.findById(1);
		assertEquals((Integer)1, user.getId());
		assertEquals("admin", user.getUsername());
		assertEquals("Admin", user.getFirstName());
		assertEquals("Admin", user.getLastName());
		assertEquals("vincentcheng787@gmail.com", user.getEmail());
		assertEquals("1990-12-01", user.getDateOfBirth().toString());
		Mockito.verify(userDAO, VerificationModeFactory.times(1)).findById(Mockito.anyInt());
	}
	
	@Test
	public void testFindByIdFail(){
		User user = userService.findById(2);
		assertNull(user);
		Mockito.verify(userDAO, VerificationModeFactory.times(1)).findById(Mockito.anyInt());
	}

	@Test
	public void testFindAll(){
		List<User> users = userService.findAllUsers();
		User user1 = new User(1, "admin", "admin", "Admin", "Admin", "vincentcheng787@gmail.com", 
				new LocalDate(1990, 12, 1));
		User user2 = new User(2, "yukirin", "Yuki0715", "Yuki", "Kashiwagi", "yuki.kashiwagi@akb.co.jp", 
				new LocalDate(1991, 7, 15));
		assertEquals(Arrays.asList(user1, user2), users);
		Mockito.verify(userDAO, VerificationModeFactory.times(1)).findAllUsers();
	}

	@Configuration
	static class UserServiceTestContextConfiguration {

		@Bean
		public UserService userService() {
			return new UserServiceImpl();
		}

		@Bean
		public UserDAO userDAO() {
			return Mockito.mock(UserDAO.class);
		}
	}
}
