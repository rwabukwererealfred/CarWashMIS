package com.carwash.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;

@Entity
public class WorkerGroup {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int workerGroupId;
	@NotBlank(message="worker group name must not be empty")
	private String name;
	
	private boolean active;
	
	@Transient
	private List<Worker>worker;
	
	@ManyToOne
	@JoinColumn(name="ikinamba")
	private Ikinamba ikinamba;
	
	public int getWorkerGroupId() {
		return workerGroupId;
	}
	public void setWorkerGroupId(int workerGroupId) {
		this.workerGroupId = workerGroupId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Worker> getWorker() {
		return worker;
	}
	public void setWorker(List<Worker> worker) {
		this.worker = worker;
	}
	public Ikinamba getIkinamba() {
		return ikinamba;
	}
	public void setIkinamba(Ikinamba ikinamba) {
		this.ikinamba = ikinamba;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
	
	
}
