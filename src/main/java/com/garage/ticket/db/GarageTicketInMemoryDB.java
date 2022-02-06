package com.garage.ticket.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.garage.ticket.constants.CarType;
import com.garage.ticket.constants.GarageTicketConstants;
import com.garage.ticket.dto.ParkCarDTOMapper;
import com.garage.ticket.dto.ParkCarRequest;
import com.garage.ticket.model.Car;
import com.garage.ticket.repository.CarRepository;
import com.garage.ticket.util.GarageTicketUtil;

public class GarageTicketInMemoryDB implements CarRepository {

	static String slotMap;
	static List<Car> carList;
	static List<Integer> ticketNumberList;

	static {
		slotMap = new String("0000000000");
		carList = new ArrayList<>(Arrays.asList(null, null, null, null, null));
		ticketNumberList = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
	}

	@Override
	public String validateExistsPlateOfCar(String plate) {

		String result = GarageTicketConstants.EMPTY_STRING;
		if ((carList.stream().filter(c -> c != null && c.getPlate().trim().equalsIgnoreCase(plate.trim()))
				.count()) > 0) {
			result = "the plate of car is in the garage!";
		}

		return result;
	}

	@Override
	public int park(ParkCarRequest carReq) {

		List<Car> carFilteredList = carList.stream().filter(c -> c != null).collect(Collectors.toList());

		int totalUsingSlot = 0;
		for (Car car : carFilteredList) {
			totalUsingSlot += GarageTicketUtil.getCarTypeSlotLength(car.getCarType())
					+ GarageTicketConstants.NEXT_ONE_EMPTY_SLOT_VALUE;
		}

		CarType carType = CarType.forNameIgnoreCase(carReq.getCarType());

		int slotLengthOfTheCar = GarageTicketUtil.getCarTypeSlotLength(carType);
		String willSearchPattern = GarageTicketUtil.getCarTypeSlotPattern(carType);
		
		int indexOfPattern = slotMap.indexOf(willSearchPattern);
		if(indexOfPattern < 0) {
			return 0;
		}
		
		int controlIndex = (indexOfPattern + slotLengthOfTheCar);
		int addedNextSlotValue = 0;
		if (controlIndex < slotMap.length() && "1".equals(slotMap.substring(controlIndex, controlIndex + 1))) {
			addedNextSlotValue = GarageTicketConstants.NEXT_ONE_EMPTY_SLOT_VALUE;
		}

		if ((GarageTicketConstants.MAX_GARAGE_SLOT_COUNT - totalUsingSlot) >= 
				(slotLengthOfTheCar	+ addedNextSlotValue)) {/* Available Slot Exists */

			int ticketNum = ticketNumberList.stream().mapToInt(v -> v).min().getAsInt();

			Car parkingCar = ParkCarDTOMapper.convertParkCarReq(carReq);
			parkingCar.setTickeNum(ticketNum);
			int slotValue = indexOfPattern + 1;
			for (int j = 0; j < slotLengthOfTheCar; j++) {
				parkingCar.getSlotList().add(slotValue++);
			}

			/* UPDATE GARAGE SLOT MAP PATTERN */
			slotMap = GarageTicketUtil.changeSlotMap(slotMap, '1', indexOfPattern,
					(slotLengthOfTheCar + indexOfPattern + GarageTicketConstants.NEXT_ONE_EMPTY_SLOT_VALUE));
			/* System.out.println(slotMap) */
			/*--------------------------*/

			/* UPDATE GARAGE CAR LIST */
			int carIndex = carList.indexOf(null);
			carList.set(carIndex, parkingCar);
			/*------------------------*/

			/* Remove Ticket Number from List */
			ticketNumberList.remove(new Integer(ticketNum));
			/*------------------------------*/

			return slotLengthOfTheCar;
		}

		return 0;

	}

	@Override
	public boolean leave(int ticketNum) {

		Optional<Car> optWillLeaveCar = carList.stream().filter(c -> c != null && c.getTickeNum() == ticketNum)
				.findFirst();

		if (optWillLeaveCar.isPresent()) {

			Car willLeaveCar = optWillLeaveCar.get();
			int indexOfCar = carList.indexOf(willLeaveCar);
			int slotIndex = willLeaveCar.getSlotList().get(0) - 1;
			int slotLengthOfTheCar = GarageTicketUtil.getCarTypeSlotLength(willLeaveCar.getCarType());

			/* UPDATE GARAGE SLOT MAP PATTERN */
			slotMap = GarageTicketUtil.changeSlotMap(slotMap, '0', slotIndex,
					(slotLengthOfTheCar + slotIndex + GarageTicketConstants.NEXT_ONE_EMPTY_SLOT_VALUE));
			/* System.out.println(slotMap) */
			/*--------------------------*/

			/* UPDATE GARAGE CAR LIST */
			carList.set(indexOfCar, null);
			/*--------------------------*/

			/* put the Ticket into the ticket List again */
			ticketNumberList.add(ticketNum);
			ticketNumberList = ticketNumberList.stream().sorted().collect(Collectors.toList());
			/*-----------------------*/

			return true;
		}

		return false;

	}

	@Override
	public List<Car> status() {
		return carList.stream().filter(c -> c != null)
				.sorted((c1, c2) -> (c1.getSlotList().get(0) - c2.getSlotList().get(0))).collect(Collectors.toList());
	}

}
