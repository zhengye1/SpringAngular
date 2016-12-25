package com.vincent.springrest.service;

import static org.junit.Assert.*;

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
    	User user = new User(1, "admin", "admin", "Admin", "Admin", "vincentcheng787@gmail.com", 
				new LocalDate(1990, 12, 1));
        Mockito.when(userDAO.findByUsername("admin")).thenReturn(user);
    }

    @After
    public void verify() {
        Mockito.verify(userDAO, VerificationModeFactory.times(1)).findByUsername(Mockito.anyString());
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
