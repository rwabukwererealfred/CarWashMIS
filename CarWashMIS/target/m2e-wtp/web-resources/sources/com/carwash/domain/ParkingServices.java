package com.carwash.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

@Entity
public class ParkingServices {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String serviceName;
	@DecimalMin(value="100.0", message="parking price must be above 100 rwf or equal")
	private double price;
	@Min(value=5, message="time duration must be more than 5 minutes or equal")
	@Column
	private int timeDuration;
	
	@Transient
	private List<Vehicles>vehicles;
	
	@ManyToOne
	@JoinColumn(name="ikinamba")
	private Ikinamba ikinamba;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getTimeDuration() {
		return timeDuration;
	}

	public void setTimeDuration(int timeDuration) {
		this.timeDuration = timeDuration;
	}

	public List<Vehicles> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<Vehicles> vehicles) {
		this.vehicles = vehicles;
	}

	public Ikinamba getIkinamba() {
		return ikinamba;
	}

	public void setIkinamba(Ikinamba ikinamba) {
		this.ikinamba = ikinamba;
	}
	
	
}
