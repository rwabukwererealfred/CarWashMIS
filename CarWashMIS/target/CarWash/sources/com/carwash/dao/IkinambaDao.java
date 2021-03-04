package com.carwash.dao;

import org.hibernate.Session;

import com.carwash.domain.Ikinamba;

public class IkinambaDao extends Source<Ikinamba> {

	public Ikinamba getOne(int id) {
		Session s = SessionManager.getSession();
		s.beginTransaction();
		Ikinamba us = (Ikinamba)s.get(Ikinamba.class, id);
		s.close();
		return us;
	}
}
