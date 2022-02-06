package com.garage.ticket.dto;

import java.util.List;

public class ParkCarStatusResponse {

	String plate;
	String colour;
	List<Integer> slotList;
	
	public ParkCarStatusResponse() {
		
	}

	public ParkCarStatusResponse(String plate, String colour, List<Integer> slotList) {
		this.plate = plate;
		this.colour = colour;
		this.slotList = slotList;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public List<Integer> getSlotList() {
		return slotList;
	}

	public void setSlotList(List<Integer> slotList) {
		this.slotList = slotList;
	}
	
}
