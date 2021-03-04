package com.carwash.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.carwash.dao.CarWashServiceDao;
import com.carwash.dao.ParkingServicesDao;
import com.carwash.dao.UserDao;
import com.carwash.domain.CarWashServices;
import com.carwash.domain.ParkingServices;
import com.carwash.domain.User;

@ManagedBean
@ViewScoped
public class UpdateController extends Validation {

	private CarWashServices carwash;
	private ParkingServices parking;
	private User user;
	
	public UpdateController(){
		this.carwash = new CarWashServices();
		this.parking = new ParkingServices();
		this.user = new User();
	}
	
	public void carwashDialog(CarWashServices car){
		this.carwash = car;
	}
	public void parkingDialog(ParkingServices park){
		this.parking = park;
	}
	
	public void userDialog(User user){
		this.user = user;
	}
	
	public void userUpdate(){
		try {
			User us = new UserDao().getOne(this.user.getUserId());
			us.setFirstName(user.firstName);
			us.setLastName(user.getLastName());
			us.setPhoneNumber(user.getPhoneNumber());
			us.setRole(user.getRole());
			new UserDao().update(us);
			successMessage("success", "well successfull updated");
			this.user = new User();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateCarwash(){
		try {
			CarWashServices ca = new CarWashServiceDao().getOne(this.carwash.getId());
			ca.setPrices(this.carwash.getPrices());
			new CarWashServiceDao().update(ca);
			successMessage("success", "well successfull updated");
			this.carwash = new CarWashServices();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void updateParking(){
		try {
			ParkingServices pa = new ParkingServicesDao().getOne(parking.getId());
			pa.setPrice(this.parking.getPrice());
			pa.setTimeDuration(this.parking.getTimeDuration());
			new ParkingServicesDao().update(pa);
			successMessage("success", "well successfull updated");
			this.parking = new ParkingServices();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public CarWashServices getCarwash() {
		return carwash;
	}

	public void setCarwash(CarWashServices carwash) {
		this.carwash = carwash;
	}

	public ParkingServices getParking() {
		return parking;
	}

	public void setParking(ParkingServices parking) {
		this.parking = parking;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
