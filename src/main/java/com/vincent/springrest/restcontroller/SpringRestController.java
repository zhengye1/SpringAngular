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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.vincent.springrest.model.User;
import com.vincent.springrest.service.UserService;

@RestController
@RequestMapping("/api/v1")
public class SpringRestController {

	@Autowired
	UserService userService;

	static final Logger logger = LoggerFactory.getLogger(SpringRestController.class);

	@RequestMapping(value="/users", method=RequestMethod.GET,
				produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<List<User>> listAllUsers(){
		List<User> users = userService.findAllUsers();
		if(users.isEmpty()){
			return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
		}
		logger.info("Retrived all the users: {}", users);
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	@RequestMapping(value="/search", method=RequestMethod.GET, 
			produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
			
	public ResponseEntity<User> getUserByUsername(@RequestParam("username") String username){
		User user = userService.findByUsername(username);
		if (user == null){
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		logger.info("Retrive the user info for username {} : {}", username, user);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@RequestMapping(value="/users/{id}", method=RequestMethod.GET, 
			produces = {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<User> getUserById(@PathVariable("id") Integer id){
		User user = userService.findById(id);
		if (user == null){
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		logger.info("Retrive the user info for id {} : {}", id, user);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@RequestMapping(value="/users", method=RequestMethod.POST)
	public ResponseEntity<Void> create(@RequestBody User user, UriComponentsBuilder ucBuilder){
		logger.info("creating new user: {}", user);
		
		if (userService.existsUsername(user.getUsername())){
            logger.info("a user with name " + user.getUsername() + " already exists");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }

        userService.create(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/user/").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value="/users/{id}", method=RequestMethod.PUT)
	public ResponseEntity<User> update(@PathVariable int id, @RequestBody User user){
		logger.info("Updating the information for user: {} ", user);
		User current = userService.findById(id);
		if (current == null){
			logger.info("User {} not found", user);
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		
		if (current.getId() != user.getId()){
			logger.error("Someone attempt to modify id");
			return new ResponseEntity<User>(HttpStatus.CONFLICT);
		}
		userService.update(user);
		user = userService.findById(user.getId());
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@RequestMapping(value="/users/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable("id") int id){
        logger.info("deleting user with id: {}", id);
        User user = userService.findById(id);

        if (user == null){
            logger.info("Unable to delete. User with id {} not found", id);
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }

        userService.delete(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
