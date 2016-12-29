package com.vincent.springrest.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.vincent.springrest.model.User;

@Repository("userDAO")
public class UserDAOImpl implements UserDAO {
	
	@PersistenceContext
	EntityManager entityManager;

	static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
	
	@Override
	public User findById(Integer id) {
		// TODO Auto-generated method stub
		return entityManager.find( User.class, id );
	}

	@Override
	public User findByUsername(String username) {
		return findOneByField("username", username);
	}


	private User findOneByField(String colName, String colValue) {
		// TODO Auto-generated method stub
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> crit = cb.createQuery(User.class);
		Root<User> root = crit.from(User.class);
		crit.where(cb.equal(root.get(colName), colValue));
		try{
			return entityManager.createQuery(crit).getSingleResult();
		}
		catch(Exception e){
			return null;
		}
	}

	@Override
	public List<User> findAllUsers() {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> crit = cb.createQuery(User.class);
		Root<User> root = crit.from(User.class);
		crit.select(root);
		return entityManager.createQuery(crit).getResultList();
	}


	@Override
	public void create(User user) {
		// TODO Auto-generated method stub
		 logger.debug("Calling DB for create user: {}", user);
		entityManager.merge(user);
	}

	@Override
	public void delete(int id) {
		User user = findById(id);
		entityManager.remove( user );
	}

}
