package com.vincent.springrest.restcontroller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincent.springrest.filter.CORSFilter;
import com.vincent.springrest.model.User;
import com.vincent.springrest.restcontroller.SpringRestController;
import com.vincent.springrest.service.UserService;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class AppControllerUnitTest {

	private static final Integer UNKNOWN_ID = Integer.MAX_VALUE;

	private MockMvc mockMvc;

	@Mock
	private UserService userService;

	@InjectMocks
	private SpringRestController appController;

	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders
				.standaloneSetup(appController)
				.addFilters(new CORSFilter())
				.build();
	}

	//========================== Get All Users ================================
	@Test
	public void test_get_all_success() throws Exception {
		List<User> users = Arrays.asList(
				new User(1, "admin", "admin", "Admin", "Admin", "vincentcheng787@gmail.com", 
						new LocalDate(1990, 12, 1)),
				new User(2, "yukirin", "Yuki0715", "Yuki", "Kashiwagi", "yuki.kashiwagi@akb.co.jp", 
						new LocalDate(1991, 7, 15)));
		when(userService.findAllUsers()).thenReturn(users);
		mockMvc.perform(get("/users/"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$", hasSize(2)))
		.andExpect(jsonPath("$[0].id", is(1)))
		.andExpect(jsonPath("$[0].username", is("admin")))
		.andExpect(jsonPath("$[0].firstName", is("Admin")))
		.andExpect(jsonPath("$[0].lastName", is("Admin")))
		.andExpect(jsonPath("$[0].email", is("vincentcheng787@gmail.com")))
		.andExpect(jsonPath("$[0].dateOfBirth", is(new LocalDate(1990, 12, 1).toString())))
		// second one
		.andExpect(jsonPath("$[1].id", is(2)))
		.andExpect(jsonPath("$[1].username", is("yukirin")))
		.andExpect(jsonPath("$[1].firstName", is("Yuki")))
		.andExpect(jsonPath("$[1].lastName", is("Kashiwagi")))
		.andExpect(jsonPath("$[1].email", is("yuki.kashiwagi@akb.co.jp")))
		.andExpect(jsonPath("$[1].dateOfBirth", is(new LocalDate(1991, 7, 15).toString())));
		verify(userService, times(1)).findAllUsers();
		verifyNoMoreInteractions(userService);
	}

	//========================== Get All Users with empty =====================
	@Test
	public void test_get_all_with_204() throws Exception {
		List<User> users = new ArrayList<>();
		when(userService.findAllUsers()).thenReturn(users);
		mockMvc.perform(get("/users/"))
		.andExpect(status().isNoContent());
		verify(userService, times(1)).findAllUsers();
		verifyNoMoreInteractions(userService);
	}

	//========================== Get User by Id ===============================
	@Test
	public void test_get_by_id_success() throws Exception {
		User user = new User(1, "admin", "admin", "Admin", "Admin", "vincentcheng787@gmail.com", 
				new LocalDate(1990, 12, 1));

		when(userService.findById(1)).thenReturn(user);

		mockMvc.perform(get("/users/Id{id}", 1))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.id", is(1)))
		.andExpect(jsonPath("$.username", is("admin")))
		.andExpect(jsonPath("$.firstName", is("Admin")))
		.andExpect(jsonPath("$.lastName", is("Admin")))
		.andExpect(jsonPath("$.email", is("vincentcheng787@gmail.com")))
		.andExpect(jsonPath("$.dateOfBirth", is(new LocalDate(1990, 12, 1).toString())));

		verify(userService, times(1)).findById(1);
		verifyNoMoreInteractions(userService);
	}

	//========================== Get User by Id with 404 ======================
	@Test
	public void test_get_by_id_fail_404_not_found() throws Exception {

		when(userService.findById(1)).thenReturn(null);

		mockMvc.perform(get("/users/Id{id}", 1))
		.andExpect(status().isNotFound());

		verify(userService, times(1)).findById(1);
		verifyNoMoreInteractions(userService);
	}

	//========================== Get User by username ===============================
	@Test
	public void test_get_by_username_success() throws Exception {
		User user = new User(1, "admin", "admin", "Admin", "Admin", "vincentcheng787@gmail.com", 
				new LocalDate(1990, 12, 1));

		when(userService.findByUsername("admin")).thenReturn(user);

		mockMvc.perform(get("/users/{username}", "admin"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.id", is(1)))
		.andExpect(jsonPath("$.username", is("admin")))
		.andExpect(jsonPath("$.firstName", is("Admin")))
		.andExpect(jsonPath("$.lastName", is("Admin")))
		.andExpect(jsonPath("$.email", is("vincentcheng787@gmail.com")))
		.andExpect(jsonPath("$.dateOfBirth", is(new LocalDate(1990, 12, 1).toString())));

		verify(userService, times(1)).findByUsername("admin");
		verifyNoMoreInteractions(userService);
	}

	//========================== Get User by username with 404 ================
	@Test
	public void test_get_by_username_fail_404_not_found() throws Exception {

		when(userService.findById(1)).thenReturn(null);

		mockMvc.perform(get("/users/{username}", "admin"))
		.andExpect(status().isNotFound());

		verify(userService, times(1)).findByUsername("admin");
		verifyNoMoreInteractions(userService);
	}

	//========================== Post user ====================================
	@Test
	public void test_create_user_success() throws Exception{
		User user = new User(3, "zhengye1", "password", "Vincent", "Zheng", "vincentcheng787@gmail.com", 
				new LocalDate(1990, 12, 1));
		when(userService.existsUsername(user.getUsername())).thenReturn(false);
		doNothing().when(userService).create(user);

		mockMvc.perform(
				post("/users/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(user)))
		.andExpect(status().isCreated())
		.andExpect(header().string("location", containsString("http://localhost/user/")));

		verify(userService, times(1)).existsUsername(user.getUsername());
		verify(userService, times(1)).create(user);
		verifyNoMoreInteractions(userService);
	}
	
	@Test
	public void test_create_user_withUsername_exists() throws Exception{
		User user = new User(1, "admin", "admin", "Admin", "Admin", "vincentcheng787@gmail.com", 
				new LocalDate(1990, 12, 1));	
		when(userService.existsUsername(user.getUsername())).thenReturn(true);
		doNothing().when(userService).create(user);
		mockMvc.perform(
				post("/users/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(user)))
		.andExpect(status().isConflict());
		verify(userService, times(1)).existsUsername(user.getUsername());
		verify(userService, times(0)).create(user);
		verifyNoMoreInteractions(userService);
	}
	
	//========================== PUT USER======================================

    @Test
    public void test_update_user_success() throws Exception {
		User user = new User(1, "admin", "admin", "Vincent", "Admin", "vincentcheng787@gmail.com", 
				new LocalDate(1990, 12, 1));	

        when(userService.findById(user.getId())).thenReturn(user);
        doNothing().when(userService).update(user);

        mockMvc.perform(
                put("/users/Id{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isOk());

        verify(userService, times(1)).findById(user.getId());
        verify(userService, times(1)).update(user);
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void test_update_user_fail_404_not_found() throws Exception {
        User user = new User();
        user.setId(UNKNOWN_ID);
        user.setUsername("NotExitst");

        when(userService.findById(user.getId())).thenReturn(null);

        mockMvc.perform(
                put("/users/Id{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).findById(user.getId());
        verifyNoMoreInteractions(userService);
    }

	private static String asJsonString(final Object obj) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(obj);
	}
}
