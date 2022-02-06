package com.garage.ticket.service;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Service;

import com.garage.ticket.constants.CarType;
import com.garage.ticket.db.GarageTicketInMemoryDB;
import com.garage.ticket.dto.ParkCarRequest;
import com.garage.ticket.dto.ParkCarResponse;
import com.garage.ticket.model.Car;
import com.garage.ticket.repository.CarRepository;

@Service
public class TicketService {

	ReentrantLock lock = new ReentrantLock();

	private CarRepository ICarRepository;

	public TicketService() {
		this.ICarRepository = new GarageTicketInMemoryDB();
	}

	private String validateCarInput(ParkCarRequest carReq) {

		if (carReq.getPlate() == null || carReq.getPlate().isEmpty()) {
			return "plate must not be empty!";
		} else if (carReq.getColour() == null || carReq.getColour().isEmpty()) {
			return "colour must not be empty!";
		} else if (carReq.getCarType() == null || carReq.getCarType().isEmpty()) {
			return "cartype must not be empty!";
		} else if (CarType.forNameIgnoreCase(carReq.getCarType()) == null) {
			return "cartype must be \"Car-Jeep-Truck!\"";
		}

		return ICarRepository.validateExistsPlateOfCar(carReq.getPlate());

	}

	public ParkCarResponse parkCar(ParkCarRequest carReq) {

		lock.lock();
		try {

			ParkCarResponse result = new ParkCarResponse();
			result.setValidationMessage(this.validateCarInput(carReq));
			if (result.getValidationMessage().isEmpty()) {
				result.setNumberOfSlots(this.ICarRepository.park(carReq));
			}

			return result;
		} finally {
			lock.unlock();
		}

	}

	public boolean leaveCar(int ticketNum) {

		lock.lock();
		try {
			return this.ICarRepository.leave(ticketNum);
		} finally {
			lock.unlock();
		}

	}

	public List<Car> statusCar() {
		return this.ICarRepository.status();
	}

}
