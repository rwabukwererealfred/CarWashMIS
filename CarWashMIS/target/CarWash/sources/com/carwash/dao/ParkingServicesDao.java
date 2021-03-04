package com.carwash.dao;

import org.hibernate.Session;

import com.carwash.domain.ParkingServices;

public class ParkingServicesDao extends Source<ParkingServices> {

	public ParkingServices getOne(int id) {
		Session s = SessionManager.getSession();
		s.beginTransaction();
		ParkingServices us = (ParkingServices)s.get(ParkingServices.class, id);
		s.close();
		return us;
	}
}
