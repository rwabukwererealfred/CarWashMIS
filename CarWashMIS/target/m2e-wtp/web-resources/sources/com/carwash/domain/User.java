package com.carwash.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class User {

	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	public int userId;
	@NotBlank(message="first name must not be empty")
	public String firstName;
	@NotBlank(message="last name must not be empty")
	public String lastName;
	
	public String username;
	
	public String password;
	public String role;
	@NotBlank(message="phone number must not be empty")
	@Size(min=10, message="phone number size must be 10 digit")
	public String phoneNumber;
	public boolean active;
	
	@OneToOne
	@JoinColumn(name="ikinamba")
	public Ikinamba ikinamba;
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Ikinamba getIkinamba() {
		return ikinamba;
	}
	public void setIkinamba(Ikinamba ikinamba) {
		this.ikinamba = ikinamba;
	}
	
	
	
}
