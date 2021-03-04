package com.carwash.dao;

import org.hibernate.Session;

import com.carwash.domain.CarWashServices;

public class CarWashServiceDao extends Source<CarWashServices> {

	public CarWashServices getOne(int id) {
		Session s = SessionManager.getSession();
		s.beginTransaction();
		CarWashServices us = (CarWashServices)s.get(CarWashServices.class, id);
		s.close();
		return us;
	}
}
