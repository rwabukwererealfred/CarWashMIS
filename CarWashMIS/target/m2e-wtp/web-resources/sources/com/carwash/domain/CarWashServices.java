package com.carwash.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class CarWashServices {

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@NotBlank(message="service name must not be empty")
	private String serviceName;
	
	@DecimalMin(value="1000.0", message="minimum price must be 1000 rwf")
	private double prices;
	
	private String carCategory;
	
	@ManyToOne
	@JoinColumn(name="ikinamba")
	private Ikinamba ikinamba;
	
	@Transient
	private List<Vehicles>vehicles;
	
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
	public double getPrices() {
		return prices;
	}
	public void setPrices(double prices) {
		this.prices = prices;
	}
	public String getCarCategory() {
		return carCategory;
	}
	public void setCarCategory(String carCategory) {
		this.carCategory = carCategory;
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
