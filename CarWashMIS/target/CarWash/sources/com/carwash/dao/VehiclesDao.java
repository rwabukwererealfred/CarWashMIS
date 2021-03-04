package com.carwash.dao;

import org.hibernate.Session;

import com.carwash.domain.Vehicles;

public class VehiclesDao extends Source<Vehicles> {

	public Vehicles getOne(int id) {
		Session s = SessionManager.getSession();
		s.beginTransaction();
		Vehicles us = (Vehicles)s.get(Vehicles.class, id);
		s.close();
		return us;
	}
}
