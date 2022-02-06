package com.garage.ticket.dto;

import java.util.ArrayList;
import java.util.List;

import com.garage.ticket.constants.CarType;
import com.garage.ticket.model.Car;

public class ParkCarDTOMapper {

	public static Car convertParkCarReq(ParkCarRequest carReq) {
		return new Car(carReq.getPlate(), carReq.getColour(), CarType.forNameIgnoreCase(carReq.getCarType()), 0, new ArrayList<Integer>());
	}

	public static String convertParkCarResp(int numberOfSlots) {
		return (numberOfSlots == 0) ? "Garage is full."
				: "Allocated " + String.valueOf(numberOfSlots) + ((numberOfSlots == 1) ? " slot." : " slots.");
	}

	public static List<ParkCarStatusResponse> convertParkedCarList(List<Car> parkedList) {

		List<ParkCarStatusResponse> ParkCarStatusResponseList = new ArrayList<>();

		for (Car car : parkedList) {
			ParkCarStatusResponseList
					.add(new ParkCarStatusResponse(car.getPlate(), car.getColour(), car.getSlotList()));
		}

		return ParkCarStatusResponseList;

	}

}
