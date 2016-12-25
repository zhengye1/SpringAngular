package com.vincent.springrest.restcontroller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.vincent.springrest.model.User;
import com.vincent.springrest.service.UserService;

@RestController
public class SpringRestController {

	@Autowired
	UserService userService;

	static final Logger logger = LoggerFactory.getLogger(SpringRestController.class);

	@RequestMapping(value="/user/", method=RequestMethod.GET)
	public ResponseEntity<List<User>> listAllUsers(){
		List<User> users = userService.findAllUsers();
		if(users.isEmpty()){
			return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
		}
		logger.info("Retrived all the users: {}", users);
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	@RequestMapping(value="/user/{username}", method=RequestMethod.GET, 
			produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
			
	//@RequestMapping(value="/user/{username}", method=RequestMethod.GET)
	public ResponseEntity<User> getUserByUsername(@PathVariable("username") String username){
		User user = userService.findByUsername(username);
		if (user == null){
			return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
		}
		logger.info("Retrive the user info for username {} : {}", username, user);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@RequestMapping(value="/user/Id{id}", method=RequestMethod.GET, 
			produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<User> getUserById(@PathVariable("id") Integer id){
		User user = userService.findById(id);
		if (user == null){
			return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
		}
		logger.info("Retrive the user info for id {} : {}", id, user);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
}
