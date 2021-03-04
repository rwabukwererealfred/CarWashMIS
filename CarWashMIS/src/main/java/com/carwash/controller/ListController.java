package com.carwash.controller;



import java.util.List;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.servlet.http.HttpSession;

import com.carwash.dao.CarWashServiceDao;
import com.carwash.dao.IkinambaDao;
import com.carwash.dao.ParkingServicesDao;
import com.carwash.dao.UserDao;
import com.carwash.dao.VehiclesDao;
import com.carwash.dao.WorkerDao;
import com.carwash.dao.WorkerGroupDao;
import com.carwash.dao.WorkerPaymentDao;
import com.carwash.domain.CarWashServices;
import com.carwash.domain.Ikinamba;
import com.carwash.domain.ParkingServices;
import com.carwash.domain.User;
import com.carwash.domain.Vehicles;
import com.carwash.domain.Vehicles.CarStatus;
import com.carwash.domain.Worker;
import com.carwash.domain.WorkerGroup;
import com.carwash.domain.WorkerPayment;

@ManagedBean
@RequestScoped
public class ListController {
	
	private List<User>userList;
	
	public ListController(){
		this.userList = new UserDao().getAll("FROM User");
	}
	
	
	public List<Ikinamba> ikinambaList() {
		return new IkinambaDao().getAll("FROM Ikinamba");
	}
	
	public List<Worker>workerList(){
		HttpSession session = Util.getSession();
		String username = session.getAttribute("username").toString();
		User us = new UserDao().getUsername(username);
		return new WorkerDao().getAll("FROM Worker").stream().filter(i->i.getWorkGroup().getIkinamba().getId() == us.getIkinamba().getId()).collect(Collectors.toList());
	}
	
	
	public List<WorkerGroup>workerGroupList(){
		HttpSession session = Util.getSession();
		String username = session.getAttribute("username").toString();
		User us = new UserDao().getUsername(username);
		return new WorkerGroupDao().getAll("FROM WorkerGroup")
				.stream().filter(i->i.getIkinamba().getId() == us.getIkinamba().getId()).collect(Collectors.toList());
	}
	
	public List<CarWashServices>carwashList(){
		HttpSession session = Util.getSession();
		String username = session.getAttribute("username").toString();
		User us = new UserDao().getUsername(username);
		return new CarWashServiceDao().getAll("FROM CarWashServices")
				.stream().filter(i->i.getIkinamba().getId() == us.getIkinamba().getId()).collect(Collectors.toList());
	}
	
	public List<ParkingServices>parkingServiceList(){
		HttpSession session = Util.getSession();
		String username = session.getAttribute("username").toString();
		User us = new UserDao().getUsername(username);
		return new ParkingServicesDao().getAll("FROM ParkingServices")
				.stream().filter(i->i.getIkinamba().getId() == us.getIkinamba().getId()).collect(Collectors.toList());
	}
	
	
	public List<Vehicles>vehicleList(){
		HttpSession session = Util.getSession();
		String username = session.getAttribute("username").toString();
		User us = new UserDao().getUsername(username);
		return new VehiclesDao().getAll("FROM Vehicles").stream().filter(i->i.getStatus() == CarStatus.PENDING
				&& i.getCarWashService().getIkinamba().getId() == us.getIkinamba().getId()).collect(Collectors.toList());
	}
	public List<Vehicles>vehiclePaidList(){
		HttpSession session = Util.getSession();
		String username = session.getAttribute("username").toString();
		User us = new UserDao().getUsername(username);
		return new VehiclesDao().getAll("FROM Vehicles").stream().filter(i->i.getStatus() == CarStatus.PAID
				&& i.getCarWashService().getIkinamba().getId() == us.getIkinamba().getId()).collect(Collectors.toList());
	}
	public List<Vehicles>vehicleClosedList(){
		HttpSession session = Util.getSession();
		String username = session.getAttribute("username").toString();
		User us = new UserDao().getUsername(username);
		return new VehiclesDao().getAll("FROM Vehicles").stream().filter(i->i.getStatus() == CarStatus.CLOSED
				&& i.getCarWashService().getIkinamba().getId() == us.getIkinamba().getId()).
				sorted((x,y)->y.getRegistrationTime().compareTo(x.getRegistrationTime())).collect(Collectors.toList());
	}
	public List<User>cashierList(){
		HttpSession session = Util.getSession();
		String username = session.getAttribute("username").toString();
		User us = new UserDao().getUsername(username);
		return new UserDao().getAll("FROM User").stream().filter(i->i.getRole().equals("Cashier") && i.getIkinamba().getId() == us.getIkinamba().getId())
				.collect(Collectors.toList());
	}
	
	public List<WorkerPayment>workerPaymentList(){
		HttpSession session = Util.getSession();
		String username = session.getAttribute("username").toString();
		User us = new UserDao().getUsername(username);
		return new WorkerPaymentDao().getAll("FROM WorkerPayment").stream().filter(i->i.getVehicles().getCarWashService().getIkinamba().getId()
				== us.getIkinamba().getId()).collect(Collectors.toList());
	}



	public List<User> getUserList() {
		return userList;
	}



	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	
	
}
