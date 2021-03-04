package com.carwash.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Vehicles {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int vehicleId;
	@NotBlank(message="plate no must not be empty")
	private String plateNo;
	@NotBlank(message="driver name must not be empty")
	private String driverName;
	@NotBlank(message="phone number must not be empty")
	@Size(min=10, message="phone number size must be 10 digit")
	private String phoneNumber;
	private double totalAmount;
	
	private LocalDateTime timeFrom;
	
	private LocalDateTime timeEnd;
	
	private LocalDateTime registrationTime;
	
	
	
	@Enumerated(EnumType.STRING)
	private CarStatus status;
	
	public static enum CarStatus{
		PENDING,PAID,CLOSED
	}
	
	@ManyToOne
	@JoinColumn(name="parkingService")
	private ParkingServices parkingService;
	
	@ManyToOne
	@JoinColumn(name="carWashService")
	private CarWashServices carWashService;
	
	@ManyToOne
	@JoinColumn(name="workerGroup")
	private WorkerGroup workerGroup;
	
	
	public int getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(int vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getPlateNo() {
		return plateNo;
	}
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public LocalDateTime getTimeFrom() {
		return timeFrom;
	}
	public void setTimeFrom(LocalDateTime timeFrom) {
		this.timeFrom = timeFrom;
	}
	public LocalDateTime getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(LocalDateTime timeEnd) {
		this.timeEnd = timeEnd;
	}
	
	
	public CarStatus getStatus() {
		return status;
	}
	public void setStatus(CarStatus status) {
		this.status = status;
	}
	public ParkingServices getParkingService() {
		return parkingService;
	}
	public void setParkingService(ParkingServices parkingService) {
		this.parkingService = parkingService;
	}
	public CarWashServices getCarWashService() {
		return carWashService;
	}
	public void setCarWashService(CarWashServices carWashService) {
		this.carWashService = carWashService;
	}
	public WorkerGroup getWorkerGroup() {
		return workerGroup;
	}
	public void setWorkerGroup(WorkerGroup workerGroup) {
		this.workerGroup = workerGroup;
	}
	public LocalDateTime getRegistrationTime() {
		return registrationTime;
	}
	public void setRegistrationTime(LocalDateTime registrationTime) {
		this.registrationTime = registrationTime;
	}
	
	
}
