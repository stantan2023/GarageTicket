package com.garage.ticket.repository;

import java.util.List;

import com.garage.ticket.dto.ParkCarRequest;
import com.garage.ticket.model.Car;

public interface CarRepository {

	String validateExistsPlateOfCar(String plate);
	
	int park(ParkCarRequest carReq);

	boolean leave(int ticketNum);

	List<Car> status();

}
