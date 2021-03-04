package com.carwash.dao;


import org.hibernate.Query;
import org.hibernate.Session;

import com.carwash.domain.User;


public class UserDao extends Source<User> {

	public User getOne(int id) {
		Session s = SessionManager.getSession();
		s.beginTransaction();
		User us = (User)s.get(User.class, id);
		s.close();
		return us;
	}
	
	public User getUsername(String username) {
		Session s = SessionManager.getSession();
		s.beginTransaction();
		Query q = s.createQuery("FROM User where username =:username");
		q.setString("username", username);
		User user = (User)q.uniqueResult();
		s.close();
		return user;
	}
}
