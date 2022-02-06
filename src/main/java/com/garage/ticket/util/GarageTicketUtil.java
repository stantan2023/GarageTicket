package com.garage.ticket.util;

import com.garage.ticket.constants.CarType;
import com.garage.ticket.constants.GarageTicketConstants;

public class GarageTicketUtil {

	private GarageTicketUtil() {
		throw new IllegalStateException("Utility Class");
	}

	public static int getCarTypeSlotLength(CarType carType) {

		switch (carType) {
		case Car:
			return GarageTicketConstants.CAR_SLOT_LENGTH;
		case Jeep:
			return GarageTicketConstants.JEEP_SLOT_LENGTH;
		case Truck:
			return GarageTicketConstants.TRUCK_SLOT_LENGTH;
		default:
			return 0;
		}

	}

	public static String getCarTypeSlotPattern(CarType carType) {

		switch (carType) {
		case Car:
			return GarageTicketConstants.CAR_SLOT_PATTERN;
		case Jeep:
			return GarageTicketConstants.JEEP_SLOT_PATTERN;
		case Truck:
			return GarageTicketConstants.TRUCK_SLOT_PATTERN;
		default:
			return GarageTicketConstants.EMPTY_STRING;
		}

	}

	public static String changeSlotMap(String slotMap, char changeChar, int indexOfStartChanging, int changingLength) {

		char[] slotChrArr = slotMap.toCharArray();
		changingLength = (changingLength > slotChrArr.length) ? changingLength - 1 : changingLength;
		for (int k = indexOfStartChanging; k < changingLength; k++) {
			slotChrArr[k] = changeChar;
		}

		return String.copyValueOf(slotChrArr);

	}
	
	
}
