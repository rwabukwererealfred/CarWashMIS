package com.carwash.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;

import java.io.OutputStream;
import java.text.SimpleDateFormat;

import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import com.carwash.dao.UserDao;
import com.carwash.dao.WorkerPaymentDao;
import com.carwash.domain.User;
import com.carwash.domain.Vehicles;
import com.carwash.domain.Worker;
import com.carwash.domain.WorkerPayment;

@ManagedBean
@ViewScoped
public class ReportController extends Validation {

	private Date from = null;
	private Date end = null;
	
	private String category="";

	public List<WorkerPayment> workerPaymentList() {
		HttpSession session = Util.getSession();
		String username = session.getAttribute("username").toString();
		User us = new UserDao().getUsername(username);
		return new WorkerPaymentDao().getAll("FROM WorkerPayment").stream()
				.filter(i -> i.getVehicles().getCarWashService().getIkinamba().getId() == us.getIkinamba().getId())
				.collect(Collectors.toList());
	}

	public void vehicleListReport() {
		
		LocalDateTime fr = LocalDateTime.ofInstant(from.toInstant(), ZoneId.systemDefault());
		LocalDateTime ed = LocalDateTime.ofInstant(end.toInstant(), ZoneId.systemDefault());
		
		List<WorkerPayment> list = workerPaymentList().stream()
				.filter(i -> i.getVehicles().getRegistrationTime().isAfter(fr)
						&& i.getVehicles().getRegistrationTime().isBefore(ed))
				.collect(Collectors.toList());

		System.out.println("size: " + list.size());

		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			String fileName = " paymentReport.pdf";
			String contentType = "application/pdf";
			ec.responseReset();
			ec.setResponseContentType(contentType);
			ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

			OutputStream out = ec.getResponseOutputStream();
			Document doc = new Document();
			PdfWriter.getInstance(doc, out);

			LineSeparator ls = new LineSeparator();
			doc.open();

			Paragraph header = new Paragraph();

			Paragraph header1 = new Paragraph("CARWASH Rwanda,\nP.O.Box 2029 - Kigali,Rwanda\nPhone : (+250) 788 502945"
					+ "\nEmail : carwashmis@gmail.com");
			// header.setAlignment(Element.ALIGN_RIGHT);
			header.setAlignment(Image.ALIGN_LEFT + Element.ALIGN_RIGHT);
			header.add(header1);
			doc.add(header);
			doc.add(new Chunk(ls));
			doc.add(new Paragraph("                                          "));

			doc.add(new Paragraph("Date:" + new SimpleDateFormat("dd/MMM/yyyy").format(new Date())));
			String f = new SimpleDateFormat("dd/MMM/yyyy").format(from);
			String e = new SimpleDateFormat("dd/MMM/yyyy").format(end);
			Paragraph p = new Paragraph("Payment report From " + f + " To " + e,
					FontFactory.getFont(FontFactory.TIMES_BOLD, 10, Font.BOLD, BaseColor.DARK_GRAY));
			p.setAlignment(Element.ALIGN_CENTER);
			doc.add(p);
			doc.add(new Paragraph("                                          "));
			doc.add(new Paragraph("                                          "));

			PdfPTable table = new PdfPTable(7);
			table.setWidthPercentage(100);
			doc.add(table);
			BaseColor color = new BaseColor(10, 113, 156);

			Font font0 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.WHITE);

			PdfPCell namesCell = new PdfPCell(new Phrase("Plate No \n", font0));
			namesCell.setBackgroundColor(color);
			table.addCell(namesCell);

			PdfPCell assignedBy = new PdfPCell(new Phrase("Vehicle Category \n", font0));
			assignedBy.setBackgroundColor(color);
			table.addCell(assignedBy);

			PdfPCell province1 = new PdfPCell(new Phrase("Work Group \n", font0));
			province1.setBackgroundColor(color);
			table.addCell(province1);

			PdfPCell parking = new PdfPCell(new Phrase("Parking \n", font0));
			parking.setBackgroundColor(color);
			table.addCell(parking);

			PdfPCell carwash = new PdfPCell(new Phrase("Carwash \n", font0));
			carwash.setBackgroundColor(color);
			table.addCell(carwash);

			PdfPCell district1 = new PdfPCell(new Phrase("Amount produced \n", font0));
			district1.setBackgroundColor(color);
			table.addCell(district1);

			PdfPCell home = new PdfPCell(new Phrase("Amount Consumed \n", font0));
			home.setBackgroundColor(color);
			table.addCell(home);

			Double totalPaid = 0.0;
			Double totalConsumed = 0.0;
			Double carwashA = 0.0;
			Double parkingA = 0.0;

			for (WorkerPayment re : list) {
				PdfPCell id = new PdfPCell(new Phrase(re.getVehicles().getPlateNo() + ""));
				table.addCell(id);

				PdfPCell names = new PdfPCell(new Phrase(re.getVehicles().getCarWashService().getCarCategory() + ""));
				table.addCell(names);

				PdfPCell requestDate = new PdfPCell(new Phrase(re.getVehicles().getWorkerGroup().getName() + ""));
				table.addCell(requestDate);

				PdfPCell parkingAmount = new PdfPCell(new Phrase(
						re.getVehicles().getTotalAmount() - re.getVehicles().getCarWashService().getPrices() + " Rwf"));
				table.addCell(parkingAmount);

				PdfPCell carwashAmount = new PdfPCell(
						new Phrase(re.getVehicles().getCarWashService().getPrices() + "Rwf"));
				table.addCell(carwashAmount);

				PdfPCell issues = new PdfPCell(new Phrase(re.getVehicles().getTotalAmount() + "Rwf"));
				table.addCell(issues);

				PdfPCell status = new PdfPCell(new Phrase(re.getAmount() + "Rwf"));
				table.addCell(status);

				totalPaid = totalPaid + re.getVehicles().getTotalAmount();
				totalConsumed = totalConsumed + re.getAmount();

				carwashA += re.getVehicles().getCarWashService().getPrices();
				parkingA += (re.getVehicles().getTotalAmount() - re.getVehicles().getCarWashService().getPrices());

			}
			Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, BaseColor.BLACK);

			PdfPCell namesCel = new PdfPCell(new Phrase("", font1));
			namesCel.setBorder(Rectangle.BOTTOM);
			namesCel.setBorder(Rectangle.NO_BORDER);
			table.addCell(namesCel);

			PdfPCell assignedBy1 = new PdfPCell(new Phrase("", font1));
			assignedBy1.setBorder(Rectangle.BOTTOM);
			assignedBy1.setBorder(Rectangle.NO_BORDER);
			table.addCell(assignedBy1);

			PdfPCell givenDate1 = new PdfPCell(new Phrase("", font1));
			givenDate1.setBorder(Rectangle.BOTTOM);
			givenDate1.setBorder(Rectangle.NO_BORDER);
			table.addCell(givenDate1);

			PdfPCell park = new PdfPCell(new Phrase(parkingA + " Rwf", font1));
			park.setBorder(Rectangle.BOTTOM);
			park.setBorder(Rectangle.NO_BORDER);
			table.addCell(park);

			PdfPCell wash = new PdfPCell(new Phrase(carwashA + " Rwf", font1));
			wash.setBorder(Rectangle.BOTTOM);
			wash.setBorder(Rectangle.NO_BORDER);
			table.addCell(wash);

			PdfPCell quantity1 = new PdfPCell(new Phrase(totalPaid + " Rwf", font1));
			quantity1.setBorder(Rectangle.BOTTOM);
			quantity1.setBorder(Rectangle.NO_BORDER);
			table.addCell(quantity1);

			PdfPCell totalCost1 = new PdfPCell(new Phrase(totalConsumed + " Rwf", font1));
			totalCost1.setBorder(Rectangle.BOTTOM);
			totalCost1.setBorder(Rectangle.NO_BORDER);
			table.addCell(totalCost1);

			doc.add(table);
			doc.add(new Paragraph("                                          "));
			double re = totalPaid - totalConsumed;

			Paragraph amount = new Paragraph("Remain Amount: " + re + " RWF");
			amount.setAlignment(Element.ALIGN_RIGHT);
			doc.add(amount);
			doc.add(new Paragraph("                                          "));
			String d = new SimpleDateFormat("dd/MMM/yyyy HH:mm").format(new Date());
			Paragraph printedOn = new Paragraph("Printed On:" + d);
			printedOn.setAlignment(Element.ALIGN_RIGHT);
			doc.add(printedOn);

			doc.close();

			fc.responseComplete();

		} catch (Exception ex) {
			System.err.println("Error:" + ex.getMessage());
			errorMessage("Error:", ex.getMessage());
		}

	}

	public void printVehicle(Vehicles vehicle) {

		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			String fileName = " vehicletReport.pdf";
			String contentType = "application/pdf";
			ec.responseReset();
			ec.setResponseContentType(contentType);
			ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

			OutputStream out = ec.getResponseOutputStream();
			Document doc = new Document();
			PdfWriter.getInstance(doc, out);

			LineSeparator ls = new LineSeparator();
			doc.open();

			Paragraph header = new Paragraph();

			Paragraph header1 = new Paragraph("CARWASH Rwanda,\nP.O.Box 2029 - Kigali,Rwanda\nPhone : (+250) 788 502945"
					+ "\nEmail : carwashmis@gmail.com");
			// header.setAlignment(Element.ALIGN_RIGHT);
			header.setAlignment(Image.ALIGN_LEFT + Element.ALIGN_RIGHT);
			header.add(header1);
			doc.add(header);
			doc.add(new Chunk(ls));
			doc.add(new Paragraph("                                          "));

			doc.add(new Paragraph("Date:" + new SimpleDateFormat("dd/MMM/yyyy").format(new Date())));

			Paragraph p = new Paragraph("Payment reciept for vehicle plate No: " + vehicle.getPlateNo(),
					FontFactory.getFont(FontFactory.TIMES_BOLD, 14, Font.BOLD, BaseColor.DARK_GRAY));
			p.setAlignment(Element.ALIGN_CENTER);
			doc.add(p);
			doc.add(new Paragraph("                                          "));
			doc.add(new Paragraph("                                          "));

			PdfPTable table = new PdfPTable(6);
			table.setWidthPercentage(100);
			doc.add(table);
			BaseColor color = new BaseColor(10, 113, 156);

			Font font0 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.WHITE);

			PdfPCell name = new PdfPCell(new Phrase("Driver Name \n", font0));
			name.setBackgroundColor(color);
			table.addCell(name);

			PdfPCell Phone = new PdfPCell(new Phrase("Phone Number \n", font0));
			Phone.setBackgroundColor(color);
			table.addCell(Phone);

			PdfPCell assignedBy = new PdfPCell(new Phrase("Vehicle Category \n", font0));
			assignedBy.setBackgroundColor(color);
			table.addCell(assignedBy);

			PdfPCell parking = new PdfPCell(new Phrase("Parking \n", font0));
			parking.setBackgroundColor(color);
			table.addCell(parking);

			PdfPCell carwash = new PdfPCell(new Phrase("Carwash \n", font0));
			carwash.setBackgroundColor(color);
			table.addCell(carwash);

			PdfPCell total = new PdfPCell(new Phrase("Total Amount \n", font0));
			total.setBackgroundColor(color);
			table.addCell(total);

			PdfPCell driverName = new PdfPCell(new Phrase(vehicle.getDriverName() + ""));
			table.addCell(driverName);

			PdfPCell id = new PdfPCell(new Phrase(vehicle.getPhoneNumber() + ""));
			table.addCell(id);

			PdfPCell names = new PdfPCell(new Phrase(vehicle.getCarWashService().getCarCategory() + ""));
			table.addCell(names);

			PdfPCell parkingAmount = new PdfPCell(
					new Phrase(vehicle.getTotalAmount() - vehicle.getCarWashService().getPrices() + " Rwf"));
			table.addCell(parkingAmount);

			PdfPCell carwashAmount = new PdfPCell(new Phrase(vehicle.getCarWashService().getPrices() + "Rwf"));
			table.addCell(carwashAmount);

			PdfPCell Amount = new PdfPCell(new Phrase(vehicle.getTotalAmount() + "Rwf"));
			table.addCell(Amount);

			doc.add(table);
			doc.add(new Paragraph("                                          "));

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

			if (vehicle.getParkingService() != null) {
				String dateFrom = vehicle.getTimeFrom().format(formatter);
				Paragraph Time = new Paragraph("Parking Time started: " + dateFrom);
				Time.setAlignment(Element.ALIGN_RIGHT);
				doc.add(Time);

				String dateEnd = vehicle.getTimeEnd().format(formatter);
				Paragraph Times = new Paragraph("Parking Time Ended: " + dateEnd);
				Times.setAlignment(Element.ALIGN_RIGHT);
				doc.add(Times);
			}

			doc.add(new Paragraph("                                          "));
			String d = new SimpleDateFormat("dd/MMM/yyyy HH:mm").format(new Date());
			Paragraph printedOn = new Paragraph("Printed On:" + d);

			printedOn.setAlignment(Element.ALIGN_RIGHT);
			doc.add(printedOn);

			doc.close();

			fc.responseComplete();

		} catch (Exception ex) {
			System.err.println("Error:" + ex.getMessage());
			errorMessage("Error:", ex.getMessage());
		}

	}
	public Integer count(String plateNo){
		List<WorkerPayment>work = workerPaymentList().stream().filter(i->i.getVehicles().getPlateNo().equals(plateNo)).collect(Collectors.toList());
		return work.size();
	}
	
	public Double totalAmount(String plateNo){
		double res =0.0;
		List<WorkerPayment>work = workerPaymentList().stream().filter(i->i.getVehicles().getPlateNo().equals(plateNo)).collect(Collectors.toList());
		for(WorkerPayment w : work){
			res +=w.getVehicles().getTotalAmount();
		}
		
		return res;
	}

	
public void vehicleRatingReport() {
		
		Set<String> list = new HashSet<>();
		List<WorkerPayment>list1 = workerPaymentList().stream().filter(i->list.add(i.getVehicles().getPlateNo())).collect(Collectors.toList());

		System.out.println("size: " + list.size());

		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ExternalContext ec = fc.getExternalContext();
			String fileName = " vehicleRatingReport.pdf";
			String contentType = "application/pdf";
			ec.responseReset();
			ec.setResponseContentType(contentType);
			ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

			OutputStream out = ec.getResponseOutputStream();
			Document doc = new Document();
			PdfWriter.getInstance(doc, out);

			LineSeparator ls = new LineSeparator();
			doc.open();

			Paragraph header = new Paragraph();

			Paragraph header1 = new Paragraph("CARWASH Rwanda,\nP.O.Box 2029 - Kigali,Rwanda\nPhone : (+250) 788 502945"
					+ "\nEmail : carwashmis@gmail.com");
			// header.setAlignment(Element.ALIGN_RIGHT);
			header.setAlignment(Image.ALIGN_LEFT + Element.ALIGN_RIGHT);
			header.add(header1);
			doc.add(header);
			doc.add(new Chunk(ls));
			doc.add(new Paragraph("                                          "));

			doc.add(new Paragraph("Date:" + new SimpleDateFormat("dd/MMM/yyyy").format(new Date())));
			
			Paragraph p = new Paragraph("Rating Report ",
					FontFactory.getFont(FontFactory.TIMES_BOLD, 10, Font.BOLD, BaseColor.DARK_GRAY));
			p.setAlignment(Element.ALIGN_CENTER);
			doc.add(p);
			doc.add(new Paragraph("                                          "));
			doc.add(new Paragraph("                                          "));

			PdfPTable table = new PdfPTable(5);
			table.setWidthPercentage(100);
			doc.add(table);
			BaseColor color = new BaseColor(10, 113, 156);

			Font font0 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.WHITE);

			PdfPCell namesCell = new PdfPCell(new Phrase("Plate No \n", font0));
			namesCell.setBackgroundColor(color);
			table.addCell(namesCell);

			PdfPCell assignedBy = new PdfPCell(new Phrase("Vehicle Category \n", font0));
			assignedBy.setBackgroundColor(color);
			table.addCell(assignedBy);

			PdfPCell parking = new PdfPCell(new Phrase("Driver \n", font0));
			parking.setBackgroundColor(color);
			table.addCell(parking);

			PdfPCell carwash = new PdfPCell(new Phrase("Total Amount \n", font0));
			carwash.setBackgroundColor(color);
			table.addCell(carwash);
			
			PdfPCell province1 = new PdfPCell(new Phrase("Rating \n", font0));
			province1.setBackgroundColor(color);
			table.addCell(province1);
			

			for (WorkerPayment re : list1) {
				
				
				PdfPCell id = new PdfPCell(new Phrase(re.getVehicles().getPlateNo() + ""));
				table.addCell(id);
				
				PdfPCell names = new PdfPCell(new Phrase(re.getVehicles().getCarWashService().getCarCategory() + ""));
				table.addCell(names);	

				PdfPCell parkingAmount = new PdfPCell(new Phrase(re.getVehicles().getDriverName() + ""));
				table.addCell(parkingAmount);

				PdfPCell carwashAmount = new PdfPCell(
						new Phrase(totalAmount(re.getVehicles().getPlateNo()) + "Rwf"));
				table.addCell(carwashAmount);
				
				
				PdfPCell requestDate = new PdfPCell(new Phrase(count(re.getVehicles().getPlateNo())+""));
				table.addCell(requestDate);
				
			}
			
			doc.add(table);
			doc.add(new Paragraph("                                          "));
			String d = new SimpleDateFormat("dd/MMM/yyyy HH:mm").format(new Date());
			Paragraph printedOn = new Paragraph("Printed On:" + d);
			printedOn.setAlignment(Element.ALIGN_RIGHT);
			doc.add(printedOn);

			doc.close();

			fc.responseComplete();

		} catch (Exception ex) {
			System.err.println("Error:" + ex.getMessage());
			errorMessage("Error:", ex.getMessage());
		}

	}
public void workerReport() {
	
	
	
	ListController listController = new ListController();
	List<Worker>list1 = listController.workerList();

	

	try {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		String fileName = " workerReport.pdf";
		String contentType = "application/pdf";
		ec.responseReset();
		ec.setResponseContentType(contentType);
		ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

		OutputStream out = ec.getResponseOutputStream();
		Document doc = new Document();
		PdfWriter.getInstance(doc, out);

		LineSeparator ls = new LineSeparator();
		doc.open();

		Paragraph header = new Paragraph();

		Paragraph header1 = new Paragraph("CARWASH Rwanda,\nP.O.Box 2029 - Kigali,Rwanda\nPhone : (+250) 788 502945"
				+ "\nEmail : carwashmis@gmail.com");
		// header.setAlignment(Element.ALIGN_RIGHT);
		header.setAlignment(Image.ALIGN_LEFT + Element.ALIGN_RIGHT);
		header.add(header1);
		doc.add(header);
		doc.add(new Chunk(ls));
		doc.add(new Paragraph("                                          "));

		doc.add(new Paragraph("Date:" + new SimpleDateFormat("dd/MMM/yyyy").format(new Date())));
		
		Paragraph p = new Paragraph("Report of Workers:",
				FontFactory.getFont(FontFactory.TIMES_BOLD, 10, Font.BOLD, BaseColor.DARK_GRAY));
		p.setAlignment(Element.ALIGN_CENTER);
		doc.add(p);
		doc.add(new Paragraph("                                          "));
		doc.add(new Paragraph("                                          "));

		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(100);
		doc.add(table);
		BaseColor color = new BaseColor(10, 113, 156);

		Font font0 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.WHITE);

		PdfPCell namesCell = new PdfPCell(new Phrase("Names \n", font0));
		namesCell.setBackgroundColor(color);
		table.addCell(namesCell);

		PdfPCell assignedBy = new PdfPCell(new Phrase("National ID \n", font0));
		assignedBy.setBackgroundColor(color);
		table.addCell(assignedBy);

		PdfPCell parking = new PdfPCell(new Phrase("Phone Number \n", font0));
		parking.setBackgroundColor(color);
		table.addCell(parking);

		PdfPCell carwash = new PdfPCell(new Phrase("Group \n", font0));
		carwash.setBackgroundColor(color);
		table.addCell(carwash);
		
		
		for (Worker re : list1) {
			
			
			PdfPCell id = new PdfPCell(new Phrase(re.getFirstName() + " "+ re.getLastName()));
			table.addCell(id);
			
			PdfPCell names = new PdfPCell(new Phrase(re.getNatinalId() + ""));
			table.addCell(names);	

			PdfPCell parkingAmount = new PdfPCell(new Phrase(re.getPhoneNumber() + ""));
			table.addCell(parkingAmount);

			PdfPCell carwashAmount = new PdfPCell(
					new Phrase(re.getWorkGroup().getName() + ""));
			table.addCell(carwashAmount);
				
			
		}
		
		doc.add(table);
		doc.add(new Paragraph("                                          "));
		String d = new SimpleDateFormat("dd/MMM/yyyy HH:mm").format(new Date());
		Paragraph printedOn = new Paragraph("Printed On:" + d);
		printedOn.setAlignment(Element.ALIGN_RIGHT);
		doc.add(printedOn);

		doc.close();

		fc.responseComplete();

	} catch (Exception ex) {
		System.err.println("Error:" + ex.getMessage());
		errorMessage("Error:", ex.getMessage());
	}

}
public void vehicleByCategoryReport() {
	
	Set<String> list = new HashSet<>();
	List<WorkerPayment>list1 = workerPaymentList().stream().filter(i->list.add(i.getVehicles().getPlateNo())).collect(Collectors.toList());

	System.out.println("size: " + list.size());

	try {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		String fileName = " vehicleByCategoryReport.pdf";
		String contentType = "application/pdf";
		ec.responseReset();
		ec.setResponseContentType(contentType);
		ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

		OutputStream out = ec.getResponseOutputStream();
		Document doc = new Document();
		PdfWriter.getInstance(doc, out);

		LineSeparator ls = new LineSeparator();
		doc.open();

		Paragraph header = new Paragraph();

		Paragraph header1 = new Paragraph("CARWASH Rwanda,\nP.O.Box 2029 - Kigali,Rwanda\nPhone : (+250) 788 502945"
				+ "\nEmail : carwashmis@gmail.com");
		// header.setAlignment(Element.ALIGN_RIGHT);
		header.setAlignment(Image.ALIGN_LEFT + Element.ALIGN_RIGHT);
		header.add(header1);
		doc.add(header);
		doc.add(new Chunk(ls));
		doc.add(new Paragraph("                                          "));

		doc.add(new Paragraph("Date:" + new SimpleDateFormat("dd/MMM/yyyy").format(new Date())));
		
		Paragraph p = new Paragraph("Report by Category :"+ category,
				FontFactory.getFont(FontFactory.TIMES_BOLD, 10, Font.BOLD, BaseColor.DARK_GRAY));
		p.setAlignment(Element.ALIGN_CENTER);
		doc.add(p);
		doc.add(new Paragraph("                                          "));
		doc.add(new Paragraph("                                          "));

		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100);
		doc.add(table);
		BaseColor color = new BaseColor(10, 113, 156);

		Font font0 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, BaseColor.WHITE);

		PdfPCell namesCell = new PdfPCell(new Phrase("Plate No \n", font0));
		namesCell.setBackgroundColor(color);
		table.addCell(namesCell);

		PdfPCell assignedBy = new PdfPCell(new Phrase("Vehicle Category \n", font0));
		assignedBy.setBackgroundColor(color);
		table.addCell(assignedBy);

		PdfPCell parking = new PdfPCell(new Phrase("Driver \n", font0));
		parking.setBackgroundColor(color);
		table.addCell(parking);

		PdfPCell carwash = new PdfPCell(new Phrase("Total Amount \n", font0));
		carwash.setBackgroundColor(color);
		table.addCell(carwash);
		
		PdfPCell province1 = new PdfPCell(new Phrase("Rating \n", font0));
		province1.setBackgroundColor(color);
		table.addCell(province1);
		

		for (WorkerPayment re : list1.stream().filter(i->i.getVehicles().getCarWashService().getCarCategory().
				equals(category)).collect(Collectors.toList())) {
			
			
			PdfPCell id = new PdfPCell(new Phrase(re.getVehicles().getPlateNo() + ""));
			table.addCell(id);
			
			PdfPCell names = new PdfPCell(new Phrase(re.getVehicles().getCarWashService().getCarCategory() + ""));
			table.addCell(names);	

			PdfPCell parkingAmount = new PdfPCell(new Phrase(re.getVehicles().getDriverName() + ""));
			table.addCell(parkingAmount);

			PdfPCell carwashAmount = new PdfPCell(
					new Phrase(totalAmount(re.getVehicles().getPlateNo()) + "Rwf"));
			table.addCell(carwashAmount);
			
			
			PdfPCell requestDate = new PdfPCell(new Phrase(count(re.getVehicles().getPlateNo())+""));
			table.addCell(requestDate);
			
		}
		
		doc.add(table);
		doc.add(new Paragraph("                                          "));
		String d = new SimpleDateFormat("dd/MMM/yyyy HH:mm").format(new Date());
		Paragraph printedOn = new Paragraph("Printed On:" + d);
		printedOn.setAlignment(Element.ALIGN_RIGHT);
		doc.add(printedOn);

		doc.close();

		fc.responseComplete();

	} catch (Exception ex) {
		System.err.println("Error:" + ex.getMessage());
		errorMessage("Error:", ex.getMessage());
	}

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

}
