package com.carwash.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity 
public class Worker {

	@GeneratedValue(strategy= GenerationType.AUTO)
	@Id
	private int workerId;
	@NotBlank(message="first name must not be empty")
	private String firstName;
	@NotBlank(message="last name must not be empty")
	private String lastName;
	@NotBlank(message="phone Number must not be empty")
	@Size(min=10, message="phone number size must be 10 digit")
	private String phoneNumber;
	private String natinalId;
	
	@ManyToOne
	@JoinColumn(name="workGroup")
	private WorkerGroup workGroup;
	
	
	public WorkerGroup getWorkGroup() {
		return workGroup;
	}
	public void setWorkGroup(WorkerGroup workGroup) {
		this.workGroup = workGroup;
	}
	public int getWorkerId() {
		return workerId;
	}
	public void setWorkerId(int workerId) {
		this.workerId = workerId;
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
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getNatinalId() {
		return natinalId;
	}
	public void setNatinalId(String natinalId) {
		this.natinalId = natinalId;
	}
	
	
	
}
