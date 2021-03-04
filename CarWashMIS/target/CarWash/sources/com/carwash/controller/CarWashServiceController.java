package com.carwash.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpSession;

import com.carwash.dao.CarWashServiceDao;
import com.carwash.dao.ParkingServicesDao;
import com.carwash.dao.UserDao;
import com.carwash.dao.VehiclesDao;
import com.carwash.dao.WorkerGroupDao;
import com.carwash.dao.WorkerPaymentDao;
import com.carwash.domain.CarWashServices;
import com.carwash.domain.ParkingServices;
import com.carwash.domain.User;
import com.carwash.domain.Vehicles;
import com.carwash.domain.Vehicles.CarStatus;
import com.carwash.domain.WorkerGroup;
import com.carwash.domain.WorkerPayment;

@ManagedBean
@ViewScoped
public class CarWashServiceController extends Validation {

	private CarWashServices carWashService;
	private ParkingServices parkingService;
	private String serviceSelection = "";
	private Vehicles vehicle, vehicle1, vehicle2;
	private int carWashServiceId;
	private int workerGroupId;
	private String showParkingDetails;
	private long minutes;
	private double parkingPrice, totalAmount;
	private Date from = null, end = null;
	private WorkerPayment workerPayment;

	public CarWashServiceController() {
		vehicle2 = new Vehicles();
		workerPayment = new WorkerPayment();
		minutes = 0;
		parkingPrice = 0.0;
		totalAmount = 0.0;
		vehicle1 = new Vehicles();
		vehicle = new Vehicles();
		carWashService = new CarWashServices();
		parkingService = new ParkingServices();

	}

	public void calculteAmount() {
		//
		HttpSession session = Util.getSession();
		String username = session.getAttribute("username").toString();
		User us = new UserDao().getUsername(username);
		Optional<ParkingServices> park = new ParkingServicesDao().getAll("FROM ParkingServices").stream()
				.filter(i -> i.getIkinamba().getId() == us.getIkinamba().getId()).findAny();
		if (vehicle1 != null) {

			// LocalDateTime fr = LocalDateTime.ofInstant(from.toInstant(),
			// ZoneId.systemDefault());
			LocalDateTime ed = LocalDateTime.ofInstant(end.toInstant(), ZoneId.systemDefault());
			minutes = ChronoUnit.MINUTES.between(vehicle1.getTimeFrom(), ed);
			int cal = (int) minutes / park.get().getTimeDuration();
			parkingPrice = park.get().getPrice() * cal;
			totalAmount = parkingPrice + vehicle1.getCarWashService().getPrices();
			System.out
					.println("minutes: " + minutes + " parkingPrice: " + parkingPrice + " totalAmount: " + totalAmount);
			// vehicle1.setTimeFrom(fr);
			vehicle1.setTimeEnd(ed);
		} else {

			errorMessage("error", "please select vehicle on table");
		}
		System.out.println("the result: " + minutes);
	}

	public void customerApprovePayment() {

		try {
			
		
		double workerAmount = (vehicle1.getCarWashService().getPrices() * 20) / 100;
		if(parkingPrice <=0 && vehicle1.getTimeFrom() !=null){
			errorMessage("error", "please calculate amount before submit");
		}else{
		if (vehicle1 != null) {
			if (vehicle1.getTimeFrom() == null) {
				vehicle1.setTimeFrom(LocalDateTime.now());
				vehicle1.setTimeEnd(LocalDateTime.now());
				totalAmount = vehicle1.getCarWashService().getPrices();
				vehicle1.setParkingService(null);

			} else {
				Optional<ParkingServices> park = new ParkingServicesDao().getAll("From ParkingServices").stream()
						.filter(i -> i.getIkinamba().getId() == vehicle1.getWorkerGroup().getIkinamba().getId())
						.findAny();
				LocalDateTime ed = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
				vehicle1.setParkingService(park.get());
				vehicle1.setTimeEnd(ed);
				vehicle1.setParkingService(vehicle1.getParkingService());
				System.out.println("resulldkalkdjfkkkkkkkkkkkkkkkkkkkkkkk" + from + "  " + end);
			}
			
			vehicle1.setStatus(CarStatus.PAID);
			vehicle1.setTotalAmount(totalAmount);
			new VehiclesDao().update(vehicle1);

			workerPayment.setAmount(workerAmount);
			workerPayment.setVehicles(vehicle1);
			new WorkerPaymentDao().record(workerPayment);
			workerPayment = new WorkerPayment();
			this.vehicle1 = new Vehicles();
			successMessage("success", "well succesfull paid");
		}
		}
		} catch (Exception e) {
			errorMessage("error", e.getMessage());
		}
	}

	public void saveCarwashService() {
		HttpSession session = Util.getSession();
		String username = session.getAttribute("username").toString();
		User us = new UserDao().getUsername(username);
		carWashService.setIkinamba(us.getIkinamba());
		carWashService.setServiceName(serviceSelection);
		new CarWashServiceDao().record(carWashService);
		carWashService = new CarWashServices();
		serviceSelection = "";

		successMessage("success", "well successfull saved");
	}

	public void saveParkingService() {
		HttpSession session = Util.getSession();
		String username = session.getAttribute("username").toString();
		User us = new UserDao().getUsername(username);
		Optional<ParkingServices> park = new ParkingServicesDao().getAll("FROM ParkingServices").stream()
				.filter(i -> i.getIkinamba().getId() == us.getIkinamba().getId()).findAny();
		if (park.isPresent()) {
			errorMessage("error", "parking services price is arleady saved!! any change please go and update");
		} else {
			parkingService.setIkinamba(us.getIkinamba());
			parkingService.setServiceName(serviceSelection);
			new ParkingServicesDao().record(parkingService);
			parkingService = new ParkingServices();
			serviceSelection = "";
			successMessage("success", "well successfull saved");
		}
	}

	public void saveVehicle() {
		List<WorkerGroup> list = new WorkerGroupDao().getAll("From WorkerGroup").stream().filter(i -> i.isActive())
				.collect(Collectors.toList());
		if (list.size() == 0){
			new WorkerGroupDao().getAll("From WorkerGroup").stream().forEach(i -> {
				i.setActive(true);
				new WorkerGroupDao().update(i);
			});
		}
		
			for (WorkerGroup w : list) {
				CarWashServices car = new CarWashServiceDao().getOne(carWashServiceId);
				vehicle.setRegistrationTime(LocalDateTime.now());
				vehicle.setWorkerGroup(w);
				vehicle.setCarWashService(car);
				vehicle.setStatus(CarStatus.PENDING);
				new VehiclesDao().record(vehicle);
				vehicle = new Vehicles();
				successMessage("success", "This Vehicle is well assigned to Group: "+w.getName());
				w.setActive(false);
				new WorkerGroupDao().update(w);
				break;

			
		}

		
	}

	public void payDialog(Vehicles v) {
		this.vehicle2 = v;
	}

	public void workerPaymentMethod(Vehicles vehicle) {

		Vehicles v = new VehiclesDao().getOne(vehicle.getVehicleId());
		v.setStatus(CarStatus.CLOSED);
		new VehiclesDao().update(v);

		successMessage("success", "well successfull approved");
	}

	public void updateTimeFrom() {
		LocalDateTime fr = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		Vehicles v = new VehiclesDao().getOne(vehicle2.getVehicleId());
		v.setTimeFrom(fr);
		new VehiclesDao().update(v);
		successMessage("success", "well successfull updated");
		from = null;
		v = new Vehicles();
	}

	public CarWashServices getCarWashService() {
		return carWashService;
	}

	public void setCarWashService(CarWashServices carWashService) {
		this.carWashService = carWashService;
	}

	public ParkingServices getParkingService() {
		return parkingService;
	}

	public void setParkingService(ParkingServices parkingService) {
		this.parkingService = parkingService;
	}

	public String getServiceSelection() {
		return serviceSelection;
	}

	public void setServiceSelection(String serviceSelection) {
		this.serviceSelection = serviceSelection;
	}

	public Vehicles getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicles vehicle) {
		this.vehicle = vehicle;
	}

	public int getCarWashServiceId() {
		return carWashServiceId;
	}

	public void setCarWashServiceId(int carWashServiceId) {
		this.carWashServiceId = carWashServiceId;
	}

	public int getWorkerGroupId() {
		return workerGroupId;
	}

	public void setWorkerGroupId(int workerGroupId) {
		this.workerGroupId = workerGroupId;
	}

	public Vehicles getVehicle1() {
		return vehicle1;
	}

	public void setVehicle1(Vehicles vehicle1) {
		this.vehicle1 = vehicle1;
	}

	public String getShowParkingDetails() {
		return showParkingDetails;
	}

	public void setShowParkingDetails(String showParkingDetails) {
		this.showParkingDetails = showParkingDetails;
	}

	public long getMinutes() {
		return minutes;
	}

	public void setMinutes(long minutes) {
		this.minutes = minutes;
	}

	public double getParkingPrice() {
		return parkingPrice;
	}

	public void setParkingPrice(double parkingPrice) {
		this.parkingPrice = parkingPrice;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Vehicles getVehicle2() {
		return vehicle2;
	}

	public void setVehicle2(Vehicles vehicle2) {
		this.vehicle2 = vehicle2;
	}

	public WorkerPayment getWorkerPayment() {
		return workerPayment;
	}

	public void setWorkerPayment(WorkerPayment workerPayment) {
		this.workerPayment = workerPayment;
	}

}
