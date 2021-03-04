package com.carwash.dao;



import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

public class Source<T> {

	public void record(T t)
	{
		Session s  = SessionManager.getSession();
		s.beginTransaction();
		s.save(t);
		s.getTransaction().commit();
        s.close();
	}
	
	public void update(T t)
	{
		Session s = SessionManager.getSession();
		s.beginTransaction();
		s.update(t);
		s.getTransaction().commit();
		s.close();
	}
	
	public void delete(T t)
	{
		Session s = SessionManager.getSession();
		s.beginTransaction();
		s.delete(t);
		s.getTransaction().commit();
		s.close();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getAll(String query)
	{
		Session s =SessionManager.getSession();
		List<T> list = new ArrayList<T>();
		list = s.createQuery(query).list();
		s.close();
		return list;
	}
	
}
