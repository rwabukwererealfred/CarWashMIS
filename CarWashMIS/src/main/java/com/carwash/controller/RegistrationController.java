package com.carwash.controller;

import java.util.Optional;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;

import com.carwash.dao.IkinambaDao;
import com.carwash.dao.UserDao;
import com.carwash.dao.WorkerDao;
import com.carwash.dao.WorkerGroupDao;
import com.carwash.domain.Ikinamba;
import com.carwash.domain.User;
import com.carwash.domain.Worker;
import com.carwash.domain.WorkerGroup;

@ManagedBean
@ViewScoped
public class RegistrationController extends Validation {

	private User user, user1;
	private Worker worker;
	private Ikinamba ikinamba;
	private int ikinambaId;
	private WorkerGroup workerGroup;
	private int workeGroupId;
	

	public RegistrationController() {
		this.workerGroup = new WorkerGroup();
		ikinamba = new Ikinamba();
		user = new User();
		worker = new Worker();
		user1 = new User();
	}

	public void saveUserAdmin() {
		Optional<User> us = new UserDao().getAll("FROM User").stream()
				.filter(i -> i.getPhoneNumber().equals(user.getPhoneNumber())).findAny();
		if(!us.isPresent()){
		Ikinamba ikId = new IkinambaDao().getOne(ikinambaId);
		user.setIkinamba(ikId);
		user.setRole("Admin");
		user.setActive(true);
		new UserDao().record(user);
		user = new User();
		successMessage("success","well successfull saved");
		}else{
			errorMessage("error", "phone number is arleady used");
		}

		System.out.println("result are done");

	}
	public void saveCashier(){
		
		try {
			Optional<User> use = new UserDao().getAll("FROM User").stream()
					.filter(i -> i.getPhoneNumber().equals(user.getPhoneNumber())).findAny();
			if(!use.isPresent()){
			HttpSession session = Util.getSession();
			String username = session.getAttribute("username").toString();
			User us = new UserDao().getUsername(username);
			user.setActive(true);
			user.setRole("Cashier");
			user.setIkinamba(us.getIkinamba());
			new UserDao().record(user);
			user = new User();
			successMessage("success","well successfull saved");
			}else{
				errorMessage("error", "phone number is arleady used");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void registrationAdminDialog(Ikinamba id) {
		ikinambaId = id.getId();
		System.out.println("id: " + ikinambaId + " ids: " + id);
	}

	public void adminViewDialog(Ikinamba iki) {
		this.user1 = new UserDao().getAll("FROM User").stream().filter(i->i.getIkinamba().getId() == iki.getId() && i.getRole().equals("Admin"))
				.findAny().get();
	}

	public boolean checkCarwashUser(Ikinamba ik) {
		Optional<User> user = new UserDao().getAll("FROM User").stream()
				.filter(i -> i.getIkinamba().getId() == ik.getId() && i.getRole().equals("Admin")).findAny();
		if (!user.isPresent()) {
			return false;
		} else {
			return true;
		}
	}

	public void saveIkinamba() {
		try {
			
		new IkinambaDao().record(ikinamba);
		ikinamba = new Ikinamba();
		
		successMessage("success","well successfull saved");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void saveWorkerGroup(){
		Optional<WorkerGroup>id = new WorkerGroupDao().getAll("From WorkerGroup").stream().filter(i->i.getName().toLowerCase()
				.equals(workerGroup.getName().toLowerCase())).findAny();
		if(id.isPresent()){
			errorMessage("error", "Work group name is arleady exist");
		}else{
		HttpSession session = Util.getSession();
		String username = session.getAttribute("username").toString();
		User us = new UserDao().getUsername(username);
		workerGroup.setIkinamba(us.getIkinamba());
		new WorkerGroupDao().record(workerGroup);
		workerGroup = new WorkerGroup();
		successMessage("success","well successfull saved");
		}

	}
	
	public void saveWorker(){
		try {
			
		WorkerGroup wo = new WorkerGroupDao().getOne(workeGroupId);
		worker.setWorkGroup(wo);
		new WorkerDao().record(worker);
		
		successMessage("success","well successfull saved");
		System.out.println("successfull saved worker"+worker.getFirstName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		worker = new Worker();

	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	public Ikinamba getIkinamba() {
		return ikinamba;
	}

	public void setIkinamba(Ikinamba ikinamba) {
		this.ikinamba = ikinamba;
	}

	public int getIkinambaId() {
		return ikinambaId;
	}

	public void setIkinambaId(int ikinambaId) {
		this.ikinambaId = ikinambaId;
	}

	public User getUser1() {
		return user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public WorkerGroup getWorkerGroup() {
		return workerGroup;
	}

	public void setWorkerGroup(WorkerGroup workerGroup) {
		this.workerGroup = workerGroup;
	}

	public int getWorkeGroupId() {
		return workeGroupId;
	}

	public void setWorkeGroupId(int workeGroupId) {
		this.workeGroupId = workeGroupId;
	}

}
