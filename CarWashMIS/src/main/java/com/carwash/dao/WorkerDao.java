package com.carwash.dao;

import org.hibernate.Session;

import com.carwash.domain.Worker;

public class WorkerDao extends Source<Worker> {

	public Worker getOne(String id) {
		Session s = SessionManager.getSession();
		s.beginTransaction();
		Worker us = (Worker)s.get(Worker.class, id);
		s.close();
		return us;
	}
}
