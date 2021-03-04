package com.carwash.controller;

import java.io.IOException;
import java.util.Optional;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.carwash.dao.UserDao;
import com.carwash.domain.User;

@ManagedBean
@SessionScoped
public class AuthenticationController extends Validation {

	private User user;
	private String username;
	private String password;
	private String repassword;
	private String pages ="";

	public AuthenticationController() {
		user = new User();
		username = "";
		password = "";
		repassword = "";
	}

	public void login() {
		HttpSession session = Util.getSession();
		try {
			if (username.equals("admin") && password.equals("kaka")) {
				pages = "carWash";
				FacesContext.getCurrentInstance().getExternalContext().redirect("IkinambaRegistration.xhtml");
			} else {
				User user = new UserDao().getUsername(username);
				if (user != null) {
					if (user.getPassword().equals(md5(password))) {
						session.setAttribute("username", this.username);
						this.user = user;
						if (user.getRole().equals("Admin")) {
							pages = "workerPage";
							FacesContext.getCurrentInstance().getExternalContext().redirect("WorkerGroup.xhtml");
						} else {
							pages = "vehiclePage";
							FacesContext.getCurrentInstance().getExternalContext().redirect("VehicleList.xhtml");
						}

					} else {
						errorMessage("error", "wrong password");
					}
				} else {
					errorMessage("error", "username does not exist");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void logout(){
		try {
			
			HttpSession session = Util.getSession();
			session.invalidate();
			username = "";
			password = "";
			user = new User();
			FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createAccount() {
		try {
			
			if (user.getPassword().equals(repassword)) {
				Optional<User> us = new UserDao().getAll("FROM User").stream()
						.filter(i -> i.getPhoneNumber().equals(user.getPhoneNumber())).findAny();
				if (us.isPresent()) {
					User use = new UserDao().getUsername(user.getUsername());
					if (use == null && !user.getUsername().equals("admin")) {
						us.get().setUsername(user.getUsername());
						us.get().setPassword(md5(user.getPassword()));
						new UserDao().update(us.get());
						successMessage("success","account is well successfull created");
						user = new User();
						repassword = "";
					} else {
						errorMessage("error", "username is arleady exist");
					}
				} else {
					errorMessage("error", "phone number does not registered");
				}
			} else {
				errorMessage("error", "please re-type your password");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void carWashPage() throws IOException{
		pages = "carWash";
		FacesContext.getCurrentInstance().getExternalContext().redirect("IkinambaRegistration.xhtml");
	}
	public void wokerPage() throws IOException{
		pages = "workerPage";
		FacesContext.getCurrentInstance().getExternalContext().redirect("WorkerGroup.xhtml");
	}
	public void vehiclePage() throws IOException{
		pages = "vehiclePage";
		FacesContext.getCurrentInstance().getExternalContext().redirect("VehicleList.xhtml");
	}
	public void cashierPage() throws IOException{
		pages = "cashierPage";
		FacesContext.getCurrentInstance().getExternalContext().redirect("CashierRegistration.xhtml");
	}
	public void paymentPage() throws IOException{
		pages = "paymentPage";
		FacesContext.getCurrentInstance().getExternalContext().redirect("Payment.xhtml");
	}
	public void userPage() throws IOException{
		pages = "userPage";
		FacesContext.getCurrentInstance().getExternalContext().redirect("user.xhtml");
	}
	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

}
