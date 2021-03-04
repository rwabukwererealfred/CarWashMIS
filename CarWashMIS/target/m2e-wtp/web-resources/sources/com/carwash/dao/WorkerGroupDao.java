package com.carwash.dao;

import org.hibernate.Session;

import com.carwash.domain.WorkerGroup;

public class WorkerGroupDao extends Source<WorkerGroup> {

	public WorkerGroup getOne(int id) {
		Session s = SessionManager.getSession();
		s.beginTransaction();
		WorkerGroup us = (WorkerGroup)s.get(WorkerGroup.class, id);
		s.close();
		return us;
	}
}
