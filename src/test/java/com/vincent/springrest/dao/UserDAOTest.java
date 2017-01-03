package com.vincent.springrest.dao;

import java.io.InputStream;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.dbunit.DBTestCase;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vincent.springrest.configuration.HibernateTestConfiguration;
import com.vincent.springrest.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { HibernateTestConfiguration.class })
@Transactional
public class UserDAOTest extends DBTestCase {
	@Autowired
	UserDAO userDAO;

	@Autowired
	DataSource dataSource;

	static final Logger logger = LoggerFactory.getLogger(UserDAOTest.class);
	@Before
	public void setUp() throws Exception {
		IDatabaseConnection dbConn = new DatabaseDataSourceConnection(
				dataSource);
		IDataSet dataSet = getDataSet();
		logger.error("Dataset: {}", dataSet);
		DatabaseOperation.CLEAN_INSERT.execute(dbConn, getDataSet());
	}


	protected IDataSet getDataSet() throws Exception{
		InputStream input = this.getClass().getClassLoader().getResourceAsStream("User.xml");
		IDataSet dataSet =new FlatXmlDataSetBuilder().build(input);
		return dataSet;
	}


	@Test
	public void testFindById(){
		Assert.assertNotNull(userDAO.findById(1));
		Assert.assertNull(userDAO.findById(3));
	}


	@Test
	public void testCreateUser(){
		userDAO.create(getSampleUser());
		Assert.assertEquals(userDAO.findAllUsers().size(), 3);
	}

	@Test
	public void testDeleteUser(){
		userDAO.delete(1);
		Assert.assertEquals(userDAO.findAllUsers().size(), 1);
	}


	@Test
	public void testFindAllUsers(){
		Assert.assertEquals(userDAO.findAllUsers().size(), 2);
	}

	@Test
	public void testFindUserByUsername(){
		Assert.assertNotNull(userDAO.findByUsername("adminT"));
		Assert.assertNull(userDAO.findByUsername("14545"));
	}

	public User getSampleUser(){
		User user = new User();
		user.setId(3);
		user.setUsername("sashiT");
		user.setFirstName("Rino");
		user.setLastName("Test");
		user.setEmail("rino.sashihara@akb.co.jp");
		user.setPassword("sashi1121");
		user.setDateOfBirth(new LocalDate("1992-11-21"));
		return user;
	}
}
