package com.carwash.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

@Entity
public class Ikinamba {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@NotBlank(message =" car wash name must not be empty")
	private String name;
	
	@Transient
	public List<WorkerGroup>workerGroup;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<WorkerGroup> getWorkerGroup() {
		return workerGroup;
	}
	public void setWorkerGroup(List<WorkerGroup> workerGroup) {
		this.workerGroup = workerGroup;
	}
	
	
}
